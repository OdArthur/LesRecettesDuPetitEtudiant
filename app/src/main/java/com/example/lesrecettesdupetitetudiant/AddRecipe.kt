package com.example.lesrecettesdupetitetudiant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.lesrecettesdupetitetudiant.databinding.ActivityAddRecipeBinding
import com.example.lesrecettesdupetitetudiant.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

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
            /*Snackbar.make(view, binding.RecipeTitle.text.toString().trim(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()*/
        }
    }
}