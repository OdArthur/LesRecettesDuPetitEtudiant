package com.example.lesrecettesdupetitetudiant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class FridgeAdapter(
    context: Context,
    private val items: ArrayList<String>,
    private val numbers: ArrayList<Int>,
    private val isFavs: ArrayList<Boolean>
) : ArrayAdapter<String>(context, R.layout.list_item_ingredient, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var rowView = convertView

        if(rowView == null)
        {
            rowView = inflater.inflate(R.layout.list_fridge_ingredient, parent, false)
        }
        val favoriteToggle = rowView!!.findViewById<ImageView>(R.id.fav_toggle)
        val quantityIngredient = rowView!!.findViewById<TextView>(R.id.quantIngredient)
        val itemTextView = rowView!!.findViewById<TextView>(R.id.itemName)
        val trashImage = rowView!!.findViewById<ImageView>(R.id.trashfridge)

        val number = if (numbers.size > position) numbers[position] else 0
        quantityIngredient.setText(number.toString()) // Set the number
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

        sortItemsAlphabeticallyWithFavoritesFirst(items, numbers, isFavs)

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


        return rowView
    }

    fun sortItemsAlphabeticallyWithFavoritesFirst(items: ArrayList<String>, numbers: ArrayList<Int>, isFavs: ArrayList<Boolean>) {
        // Créez une liste temporaire qui contiendra les éléments triés
        val sortedList = ArrayList<Pair<String, Pair<Int, Boolean>>>()
        for (i in items.indices) {
            val pair = Pair(items[i], Pair(numbers[i], isFavs[i]))
            sortedList.add(pair)
        }

        // Trier la liste temporaire en fonction du nom de l'élément (ordre alphabétique)
        sortedList.sortBy { it.first }

        // Créez une nouvelle liste en ajoutant les éléments triés par ordre alphabétique,
        // en mettant d'abord les favoris en premier
        val newList = ArrayList<String>()
        val newNumbers = ArrayList<Int>()
        val newIsFavs = ArrayList<Boolean>()
        for (pair in sortedList) {
            if (pair.second.second) {
                // Ajoutez l'élément favori en premier
                newList.add(0, pair.first)
                newNumbers.add(0, pair.second.first)
                newIsFavs.add(0, pair.second.second)
            } else {
                // Ajouter l'élément non favori à la fin
                newList.add(pair.first)
                newNumbers.add(pair.second.first)
                newIsFavs.add(pair.second.second)
            }
        }

        // Remplacez les anciennes listes par les nouvelles listes triées
        items.clear()
        numbers.clear()
        isFavs.clear()
        items.addAll(newList)
        numbers.addAll(newNumbers)
        isFavs.addAll(newIsFavs)
        notifyDataSetChanged()
    }
}