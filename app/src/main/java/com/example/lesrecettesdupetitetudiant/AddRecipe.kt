package com.example.lesrecettesdupetitetudiant


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lesrecettesdupetitetudiant.databinding.ActivityAddRecipeBinding

class AddRecipe : AppCompatActivity() {

    private lateinit var binding: ActivityAddRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BTNAdd.setOnClickListener{
            view ->
            intent = Intent(this, AddIngredientToRecipe::class.java)
            intent.putExtra("RecipeTitle", binding.RecipeTitle.text.toString().trim())
            intent.putExtra("RecipeDescription", binding.RecipeDescription.text.toString().trim())
            startActivity(intent)



        }
    }
}