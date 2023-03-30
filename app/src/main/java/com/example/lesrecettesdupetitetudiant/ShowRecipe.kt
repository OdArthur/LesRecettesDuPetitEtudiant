package com.example.lesrecettesdupetitetudiant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.example.lesrecettesdupetitetudiant.databinding.ActivityShowRecipeBinding


class ShowRecipe : AppCompatActivity() {

    private lateinit var binding: ActivityShowRecipeBinding
    private lateinit var db:MaBDHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_recipe)

        var RecipeID = intent.getIntExtra("ID",-1)

        binding = ActivityShowRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MaBDHelper(binding.root.context)
        if(RecipeID == -1)
        {
            //TODO Go back to previous window
        }
        else
        {
            val cursor = db.GetRecipe(RecipeID)
            cursor.moveToFirst()
            binding.RecipeTitle.text = cursor.getString(cursor.getColumnIndexOrThrow("title_recette"))
            binding.RecipeDescription.text = cursor.getString(cursor.getColumnIndexOrThrow("desc_recette"))
            binding.RecipeDescription.movementMethod = ScrollingMovementMethod()
        }
    }
}