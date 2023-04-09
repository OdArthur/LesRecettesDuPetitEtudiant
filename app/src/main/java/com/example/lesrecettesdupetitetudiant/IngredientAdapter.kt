package com.example.lesrecettesdupetitetudiant

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView

class IngredientAdapter(
    context: Context,
    private val items: ArrayList<String>,
    private val numbers: ArrayList<Int>,
    private val highlightedItems: MutableSet<String>,
    private val selectedIngredients: HashMap<String, Int>,
    private val onItemClick: (selectedIngredient: String) -> Unit
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

        return rowView
    }
}
