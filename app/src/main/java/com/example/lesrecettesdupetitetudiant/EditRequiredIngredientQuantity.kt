package com.example.lesrecettesdupetitetudiant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.example.lesrecettesdupetitetudiant.databinding.ActivityListRecipeIngredientQuantityBinding

class EditRequiredIngredientQuantity : AppCompatActivity() {
    private lateinit var db:MaBDHelper
    private lateinit var binding: ActivityListRecipeIngredientQuantityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_recipe_ingredient_quantity)

        val Ingredients = intent.getStringArrayExtra("Ingredients")
        var RecipeID = intent.getIntExtra("ID", -1)

        binding = ActivityListRecipeIngredientQuantityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MaBDHelper(binding.root.context)

        var adapter:IngredientQuantityAdapter

        if(Ingredients != null)
        {

            if (Ingredients.size == 0)
            {
                val tempList:ArrayList<String> = arrayListOf("ERROR")
                adapter = IngredientQuantityAdapter(this, tempList)
            }
            else
            {
                val ListIngredients = ArrayList<String>(Ingredients.size)
                Ingredients.toCollection(ListIngredients)
                adapter = IngredientQuantityAdapter(this, ListIngredients)
            }
        }
        else
        {
            val tempList:ArrayList<String> = arrayListOf("ERROR")
            adapter = IngredientQuantityAdapter(this, tempList)
        }


        binding.ListIngredientQuantity.adapter = adapter

        binding.BTNAddRecipe.setOnClickListener{
            view->
            val IngredientQuant = adapter.GetQuant()
            db.RemoveIngredientForRecipe(RecipeID)
            db.AddIngredientForRecipe(RecipeID,IngredientQuant)
            intent = Intent(this, ShowRecipe::class.java)
            intent.putExtra("ID", RecipeID)
            startActivity(intent)
        }

    }
}