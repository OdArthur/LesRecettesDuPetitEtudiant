package com.example.lesrecettesdupetitetudiant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class NoResultsAdapter(context: Context, listItems: List<String>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val items = listItems

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: inflater.inflate(R.layout.list_item_no_results, parent, false)
        view.findViewById<TextView>(R.id.no_results_text_view).text = items[position]
        return view
    }

}