package com.example.lesrecettesdupetitetudiant.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.lesrecettesdupetitetudiant.MaBDHelper
import com.example.lesrecettesdupetitetudiant.R
import com.example.lesrecettesdupetitetudiant.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listFridgeIngredients = view.findViewById<ListView>(R.id.listFridgeIngredients)
        var db:MaBDHelper = MaBDHelper(requireContext())

        db.displayFridge(listFridgeIngredients)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}