package com.example.lesrecettesdupetitetudiant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.lesrecettesdupetitetudiant.databinding.ActivityEditRecipeBinding

    private lateinit var binding: ActivityEditRecipeBinding
    private lateinit var db:MaBDHelper

class EditRecipe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)

        binding = ActivityEditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var RecipeID = intent.getIntExtra("ID", -1)

        db = MaBDHelper(binding.root.context)

        if(RecipeID == -1)
        {
            finish()
        }
        else
        {
            db.GetRecipe(RecipeID)
            val cursor = db.GetRecipe(RecipeID)
            cursor.moveToFirst()
            binding.recipeTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow("title_recette")))
            binding.recipeDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow("desc_recette")))
        }

        binding.BTNEdit.setOnClickListener{
            view->
            db.EditRecipe(binding.recipeTitle.text.toString(), binding.recipeDescription.text.toString(), RecipeID)
            finish()
        }
    }
}