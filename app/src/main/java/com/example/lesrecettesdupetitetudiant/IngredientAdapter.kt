package com.example.lesrecettesdupetitetudiant

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*

class IngredientAdapter(
    context: Context,
    private val items: ArrayList<String>,
    private val numbers: ArrayList<Int>,
    private val highlightedItems: MutableSet<String>,
    private val selectedIngredients: HashMap<String, Int>,
    private val isFavs: ArrayList<Boolean>,
    private val onItemClick: (selectedIngredient: String) -> Unit,
    private val showDeleteConfirmationDialog: (ingredientName: String) -> Unit
) : ArrayAdapter<String>(context, R.layout.list_item_ingredient, items) {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.list_item_ingredient, parent, false)

        val favoriteToggle = rowView.findViewById<ImageView>(R.id.favorite_toggle)
        val numberEditText = rowView.findViewById<EditText>(R.id.itemClickCountEditText)
        val itemTextView = rowView.findViewById<TextView>(R.id.itemNameTextView)
        val trashImage = rowView.findViewById<ImageView>(R.id.trash)

        sortItemsAlphabeticallyWithFavoritesFirst(items, numbers, isFavs)

        val number = if (numbers.size > position) numbers[position] else 0
        numberEditText.setText(number.toString()) // Set the number
        itemTextView.text = items[position] // Set the item text

        if (highlightedItems.contains(items[position])) {
            rowView!!.setBackgroundColor(Color.LTGRAY)
        } else {
            rowView!!.setBackgroundColor(Color.TRANSPARENT)
        }

        val db = MaBDHelper(context)

        if(isFavs[position])
        {
            favoriteToggle.setImageResource(R.drawable.ic_filled_star)
        }
        else
        {
            favoriteToggle.setImageResource(R.drawable.ic_empty_star)
        }


        favoriteToggle.setOnClickListener{
            if(isFavs[position])
            {
                favoriteToggle.setImageResource(R.drawable.ic_empty_star)
                db.removeFavIngredient(items[position])
                isFavs[position] = false
            }
            else
            {
                favoriteToggle.setImageResource(R.drawable.ic_filled_star)
                db.addFavIngredient(items[position])
                isFavs[position] = true
            }
            sortItemsAlphabeticallyWithFavoritesFirst(items, numbers, isFavs)
        }

        itemTextView.setOnClickListener  {
            val selectedIngredient = getItem(position) as String
            onItemClick(selectedIngredient)
        }

        numberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                // Update the selectedIngredients map with the new value
                if (s.isNullOrEmpty() || s.toString().toInt() == 0) {
                    if (selectedIngredients.containsKey(items[position]))
                        selectedIngredients.remove(items[position])
                    numbers[position] = 0
                    rowView.setBackgroundColor(Color.TRANSPARENT)
                    highlightedItems.remove(items[position])
                } else {
                    selectedIngredients[items[position]] = s.toString().toInt()
                    numbers[position] = s.toString().toInt()
                    rowView.setBackgroundColor(Color.LTGRAY)
                    highlightedItems.add(items[position])
                }
            }
        })

        numberEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && numberEditText.text.toString() == "0") {
                numberEditText.setText("")
            }
        }

        numberEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val text = numberEditText.text.toString()
                if (text.isEmpty()) {
                    numberEditText.setText("0")
                }
                numberEditText.clearFocus()
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(numberEditText.windowToken, 0)
                true
            } else {
                false
            }
        }


        trashImage.setOnClickListener{
            showDeleteConfirmationDialog(getItem(position) as String)
        }

        return rowView
    }

    private fun sortItemsAlphabeticallyWithFavoritesFirst(items: ArrayList<String>, numbers: ArrayList<Int>, isFavs: ArrayList<Boolean>) {
        // Créer une liste d'indices triés en fonction du nom de l'élément
        val sortedIndices = items.indices.sortedBy { items[it] }

        // Créer une liste temporaire de n-uplets triés par ordre alphabétique
        val temp = sortedIndices.map { Triple(items[it], numbers[it], isFavs[it]) }.sortedBy { it.first }

        // Remplie les listes d'entrée triées en fonction de la liste temporaire
        for (i in temp.indices) {
            items[i] = temp[i].first
            numbers[i] = temp[i].second
            isFavs[i] = temp[i].third
        }

        // Déplace les éléments favoris au début de la liste
        var favIndex = 0
        for (i in 0 until isFavs.size) {
            if (isFavs[i]) {
                if (i != favIndex) {
                    items[favIndex] = items[i].also { items[i] = items[favIndex] }
                    numbers[favIndex] = numbers[i].also { numbers[i] = numbers[favIndex] }
                    isFavs[favIndex] = isFavs[i].also { isFavs[i] = isFavs[favIndex] }
                }
                favIndex++
            }
        }
        notifyDataSetChanged()
    }



}
