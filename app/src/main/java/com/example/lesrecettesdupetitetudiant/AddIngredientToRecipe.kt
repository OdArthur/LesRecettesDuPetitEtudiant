package com.example.lesrecettesdupetitetudiant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.lesrecettesdupetitetudiant.databinding.ActivityAddIngredientToRecipeBinding

class AddIngredientToRecipe : AppCompatActivity() {

    private lateinit var binding: ActivityAddIngredientToRecipeBinding
    private lateinit var db:MaBDHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredient_to_recipe)

        val RecipeTitle = intent.getStringExtra("RecipeTitle")!!
        val RecipeDescription = intent.getStringExtra("RecipeDescription")!!

        binding = ActivityAddIngredientToRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MaBDHelper(this)

        binding.ListIngredientsRecipe.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, db.GetIngredient())

        binding.BTNValidate.setOnClickListener{
            view->
            db.addRecipe(RecipeTitle, RecipeDescription)
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Return", "Recipe")
            startActivity(intent)
        }
    }
}