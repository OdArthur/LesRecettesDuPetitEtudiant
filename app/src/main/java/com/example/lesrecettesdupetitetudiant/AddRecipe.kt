package com.example.lesrecettesdupetitetudiant


import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.lesrecettesdupetitetudiant.databinding.ActivityAddRecipeBinding
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class AddRecipe : AppCompatActivity() {

    private lateinit var binding: ActivityAddRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recipeLink.addTextChangedListener (object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(binding.recipeLinkLayout.isErrorEnabled)
                {
                    binding.recipeLinkLayout.isErrorEnabled = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }
        })


        binding.BTNAdd.setOnClickListener{
            view ->
            if(binding.recipeLink.text.toString() == "" || isURL(binding.recipeLink.text.toString()))
            {
                intent = Intent(this, AddIngredientToRecipe::class.java)
                intent.putExtra("RecipeTitle", binding.RecipeTitle.text.toString().trim())
                intent.putExtra("RecipeDescription", binding.RecipeDescription.text.toString().trim())
                intent.putExtra("RecipeLink", binding.recipeLink.text.toString())
                startActivity(intent)
            }
            else
            {
                binding.recipeLinkLayout.isErrorEnabled = true
                binding.recipeLinkLayout.error = "Mauvais lien"
            }
        }
    }

    fun isURL(url: String): Boolean {
        val p: Pattern = Patterns.WEB_URL
        val m: Matcher = p.matcher(url.lowercase(Locale.getDefault()))
        return m.matches()
    }
}