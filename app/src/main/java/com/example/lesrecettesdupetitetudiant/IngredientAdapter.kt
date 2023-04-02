package com.example.lesrecettesdupetitetudiant

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class IngredientAdapter(
    context: Context,
    private val items: ArrayList<String>,
    private val numbers: ArrayList<Int>,
    private val highlightedItems: MutableSet<String>
) : ArrayAdapter<String>(context, R.layout.list_item_ingredient, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = convertView ?: inflater.inflate(R.layout.list_item_ingredient, parent, false)

        val numberTextView = rowView.findViewById<TextView>(R.id.TXTQuantityIngredientName)
        val itemTextView = rowView.findViewById<TextView>(R.id.itemClickCountTextView)

        val number = if (numbers.size > position) numbers[position] else 0
        numberTextView.text = number.toString() // Set the number
        itemTextView.text = items[position] // Set the item text

        if (highlightedItems.contains(items[position])) {
            rowView.setBackgroundColor(Color.LTGRAY)
        } else {
            rowView.setBackgroundColor(Color.TRANSPARENT)
        }

        return rowView
    }
}
