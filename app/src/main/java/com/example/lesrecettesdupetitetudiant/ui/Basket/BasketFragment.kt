package com.example.lesrecettesdupetitetudiant.ui.Basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.lesrecettesdupetitetudiant.MaBDHelper
import com.example.lesrecettesdupetitetudiant.R
import com.example.lesrecettesdupetitetudiant.databinding.FragmentBasketBinding


class BasketFragment : Fragment() {

    private var _binding: FragmentBasketBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listBasketIngredients = view.findViewById<ListView>(R.id.listBasketIngredients)
        var db:MaBDHelper = MaBDHelper(requireContext())

        db.displayBasket(listBasketIngredients)

        binding.BTNBasketToFridge.setOnClickListener{ view ->
            db.AddBasketToFridge(listBasketIngredients)
        }

    }

    override fun onResume() {
        super.onResume()
        var db:MaBDHelper = MaBDHelper(requireContext())
        val listBasketIngredients = view?.findViewById<ListView>(R.id.listBasketIngredients)
        listBasketIngredients?.let { db.displayBasket(it) }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}