package com.example.lesrecettesdupetitetudiant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.example.lesrecettesdupetitetudiant.databinding.ActivityAddIngredientToRecipeBinding
import com.google.android.material.snackbar.Snackbar

class EditRequiredIngredient : AppCompatActivity() {

    private lateinit var binding: ActivityAddIngredientToRecipeBinding
    private lateinit var db:MaBDHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredient_to_recipe)

        val RecipeTitle = intent.getStringExtra("RecipeTitle")!!
        val RecipeDescription = intent.getStringExtra("RecipeDescription")!!

        var RecipeID = intent.getIntExtra("ID", -1)

        binding = ActivityAddIngredientToRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MaBDHelper(this)

        binding.ListIngredientsRecipe.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, db.GetIngredient())

        var selectedIngredients = arrayOf<String>()

        binding.ListIngredientsRecipe.setOnItemClickListener { parent, view, position, _ ->
            if(selectedIngredients.indexOf(parent.getItemAtPosition(position) as String) != -1 )
            {
                val tempMutable = selectedIngredients.toMutableList()
                tempMutable.removeAt(selectedIngredients.indexOf(parent.getItemAtPosition(position) as String))
                selectedIngredients = tempMutable.toTypedArray()
            }
            else
            {
                selectedIngredients = selectedIngredients.plus(parent.getItemAtPosition(position) as String)
            }

        }

        binding.BTNValidate.setOnClickListener{
            view->
            intent = Intent(this, EditRequiredIngredientQuantity::class.java)

            intent.putExtra("ID", RecipeID)
            intent.putExtra("Ingredients", selectedIngredients)
            startActivity(intent)
        }
    }
}