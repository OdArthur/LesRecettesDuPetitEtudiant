package com.example.lesrecettesdupetitetudiant

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.TextView

class IngredientQuantityAdapter(
    private val context: Context,
    private val items: ArrayList<String>
) : BaseAdapter() {

    private lateinit var EditableQuantity:MutableList<IngredientQuantityData>
    private lateinit var IngredientNameText:TextView
    private lateinit var IngredientQuantity:EditText

    fun GetQuant():MutableList<IngredientQuantityData>{
        return EditableQuantity
    }

    override fun getCount(): Int {
        return items.size
    }
    override fun getItem(position: Int): Any {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_ingredient_quantity, parent, false)
            EditableQuantity = mutableListOf()
            do {
                EditableQuantity.add(IngredientQuantityData("0",0))
            } while (EditableQuantity.size  != items.size)
        }

        IngredientNameText = convertView!!.findViewById(R.id.TXTQuantityIngredientName)
        IngredientQuantity = convertView!!.findViewById(R.id.editTextNumber)

        IngredientQuantity.tag = position

        IngredientNameText.text = items[position] // Set the item text

        IngredientQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString() == "")
                {
                    EditableQuantity[position] = IngredientQuantityData(items[position], -1)
                }
                else
                {
                    EditableQuantity[position] = IngredientQuantityData(items[position], s.toString().toInt())
                }
            }
        })

        return convertView
    }
}
