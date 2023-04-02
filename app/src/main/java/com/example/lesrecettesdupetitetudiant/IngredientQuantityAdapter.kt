package com.example.lesrecettesdupetitetudiant

import android.content.Context
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

        IngredientQuantity.setOnFocusChangeListener{
            _, hasFocus->
            if(!hasFocus)
            {
                val positionInListView = IngredientQuantity.tag as Int
                if(IngredientQuantity.text.toString() == "")
                {
                    EditableQuantity[positionInListView] = IngredientQuantityData(items[positionInListView], -1)
                }
                else
                {
                    EditableQuantity[positionInListView] = IngredientQuantityData(items[positionInListView], IngredientQuantity.text.toString().toInt())
                }
            }
        }

        return convertView
    }
}
