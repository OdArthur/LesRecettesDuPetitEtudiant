package com.example.lesrecettesdupetitetudiant.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lesrecettesdupetitetudiant.MaBDHelper
import com.example.lesrecettesdupetitetudiant.ShowRecipe
import com.example.lesrecettesdupetitetudiant.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    private lateinit var db:MaBDHelper

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        db = MaBDHelper(binding.root.context)

        /*val textView: TextView = binding.textSlideshow
        slideshowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/

        val ItemsID = db.displayRecipe(binding.ListRecipe)

        binding.ListRecipe.setOnItemClickListener { parent, view, position, id ->
            val selectedRecipe = ItemsID.get(id.toInt())
            val intent = Intent(binding.root.context, ShowRecipe::class.java)
            intent.putExtra("ID", selectedRecipe)
            startActivity(intent)
        }

        binding.switchFav.setOnCheckedChangeListener { _, isChecked ->
            db.searchAndDisplayRecipe(binding.ListRecipe, binding.searchRecip.text.toString(), isChecked, binding.switchPossible.isChecked)
        }

        binding.switchPossible.setOnCheckedChangeListener { _, isChecked ->
            db.searchAndDisplayRecipe(binding.ListRecipe, binding.searchRecip.text.toString(), binding.switchFav.isChecked, isChecked)
        }

        binding.searchRecip.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                db.searchAndDisplayRecipe(binding.ListRecipe, s.toString(), binding.switchFav.isChecked, binding.switchPossible.isChecked)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })

        return root
    }

    override fun onResume() {
        super.onResume()

        val ItemsID = db.displayRecipe(binding.ListRecipe)

        binding.ListRecipe.setOnItemClickListener { parent, view, position, id ->
            val selectedRecipe = ItemsID.get(id.toInt())
            val intent = Intent(binding.root.context, ShowRecipe::class.java)
            intent.putExtra("ID", selectedRecipe)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}