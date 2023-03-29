package com.example.lesrecettesdupetitetudiant


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
            var db:MaBDHelper = MaBDHelper(this)
            db.addRecipe(
                binding.RecipeTitle.text.toString().trim(),
                binding.RecipeDescription.text.toString().trim(),
            )
        }
    }
}