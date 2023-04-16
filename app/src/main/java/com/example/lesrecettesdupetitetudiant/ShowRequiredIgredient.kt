package com.example.lesrecettesdupetitetudiant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lesrecettesdupetitetudiant.databinding.ActivityShowRequiredIgredientBinding

class ShowRequiredIgredient : AppCompatActivity() {

    private lateinit var binding: ActivityShowRequiredIgredientBinding
    private lateinit var db: MaBDHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_required_igredient)

        var RecipeID = intent.getIntExtra("ID", -1)

        binding = ActivityShowRequiredIgredientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MaBDHelper(binding.root.context)
        if(-1 == RecipeID)
        {
            finish()
        }
        else
        {
            db.DisplayRequiredIngredientForRecipe(RecipeID, binding.ListRequiredIngredient)

            binding.BTNEdit.setOnClickListener{
                view ->
                val intent = Intent(binding.root.context, EditRequiredIngredient::class.java)
                intent.putExtra("ID", RecipeID)
                startActivity(intent)
            }
        }

    }
}