package com.example.lesrecettesdupetitetudiant

import android.app.AlertDialog
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
    private val onItemClick: (selectedIngredient: String) -> Unit,
    private val showDeleteConfirmationDialog: (ingredientName: String) -> Unit
) : ArrayAdapter<String>(context, R.layout.list_item_ingredient, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var rowView = convertView
        if(rowView == null)
        {
            rowView = inflater.inflate(R.layout.list_item_ingredient, parent, false)
        }

        val numberEditText = rowView!!.findViewById<EditText>(R.id.itemClickCountEditText)
        val itemTextView = rowView!!.findViewById<TextView>(R.id.itemNameTextView)
        val trashImage = rowView!!.findViewById<ImageView>(R.id.trash)

        val number = if (numbers.size > position) numbers[position] else 0
        numberEditText.setText(number.toString()) // Set the number
        itemTextView.text = items[position] // Set the item text

        if (highlightedItems.contains(items[position])) {
            rowView!!.setBackgroundColor(Color.LTGRAY)
        } else {
            rowView!!.setBackgroundColor(Color.TRANSPARENT)
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
                    selectedIngredients[items[position]] = 0
                    numbers[position] = 0
                    rowView!!.setBackgroundColor(Color.TRANSPARENT)
                    highlightedItems.remove(items[position])
                } else {
                    selectedIngredients[items[position]] = s.toString().toInt()
                    numbers[position] = s.toString().toInt()
                    rowView!!.setBackgroundColor(Color.LTGRAY)
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
}
