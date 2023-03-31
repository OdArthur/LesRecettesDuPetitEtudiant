package com.example.lesrecettesdupetitetudiant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
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
            finish()
        }
        else
        {
            val cursor = db.GetRecipe(RecipeID)
            cursor.moveToFirst()
            binding.RecipeTitle.text = cursor.getString(cursor.getColumnIndexOrThrow("title_recette"))
            binding.RecipeDescription.text = cursor.getString(cursor.getColumnIndexOrThrow("desc_recette"))
            binding.RecipeDescription.movementMethod = ScrollingMovementMethod()

            binding.favRecette.isChecked = toBoolean(cursor.getInt(cursor.getColumnIndexOrThrow("fav_recette")))

            binding.favRecette.setOnCheckedChangeListener { _, isChecked ->
                db.FavRecipe(RecipeID, isChecked)
            }

            binding.editRecipe.setOnClickListener{
                view->
                intent = Intent(this, EditRecipe::class.java)
                intent.putExtra("ID", RecipeID)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        var RecipeID = intent.getIntExtra("ID",-1)

        val cursor = db.GetRecipe(RecipeID)
        cursor.moveToFirst()
        binding.RecipeTitle.text = cursor.getString(cursor.getColumnIndexOrThrow("title_recette"))
        binding.RecipeDescription.text = cursor.getString(cursor.getColumnIndexOrThrow("desc_recette"))
        }
    }

    fun toBoolean(number:Int):Boolean
    {
        return number == 1
    }