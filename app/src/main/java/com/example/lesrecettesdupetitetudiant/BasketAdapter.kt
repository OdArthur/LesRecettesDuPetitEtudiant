package com.example.lesrecettesdupetitetudiant

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class BasketAdapter(
    context: Context,
    private val items: ArrayList<String>,
    private val numbers: ArrayList<Int>,
    private val isFavs: ArrayList<Boolean>
) : ArrayAdapter<String>(context, R.layout.list_item_ingredient, items) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.list_fridge_ingredient, parent, false)

        val favoriteToggle = rowView.findViewById<ImageView>(R.id.fav_toggle)
        val quantityIngredient = rowView.findViewById<TextView>(R.id.quantIngredient)
        val itemTextView = rowView.findViewById<TextView>(R.id.itemName)
        val trashImage = rowView.findViewById<ImageView>(R.id.trashfridge)

        sortItemsAlphabeticallyWithFavoritesFirst(items, numbers, isFavs)

        val number = if (numbers.size > position) numbers[position] else 0
        quantityIngredient.text = number.toString() // Set the number
        itemTextView.text = items[position] // Set the item text

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

        trashImage.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            var message = "Combien de "
            message += items[position]
            message += " voulez-vous retirer du frigo?"

            builder.setMessage(message)

            val editText = EditText(context)

            val filter = InputFilter { source, _, _, _, _, _ ->
                for (i in source.indices) {
                    if (!Character.isDigit(source[i])) {
                        return@InputFilter ""
                    }
                }
                null
            }

            editText.filters = arrayOf(filter)

            builder.setView(editText)

            builder.setPositiveButton("Retirer") { dialog, which ->
                var decrementNumber = editText.text.toString().toInt()
                numbers[position] -= decrementNumber
                if (numbers[position] < 0)
                    numbers[position] = 0

                val ingredientsMap = HashMap<String, Int>()
                ingredientsMap[items[position]] = decrementNumber
                db.removeIngredientsFromFridge(ingredientsMap)
                notifyDataSetChanged()
            }
            builder.setNegativeButton("Annuler", null)

// Créer et afficher la boîte de dialogue
            val dialog = builder.create()
            dialog.show()
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