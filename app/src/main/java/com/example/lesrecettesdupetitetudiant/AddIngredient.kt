package com.example.lesrecettesdupetitetudiant

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.lesrecettesdupetitetudiant.databinding.ActivityAddIngredientBinding
import com.example.lesrecettesdupetitetudiant.databinding.ActivityAddIngredientsToFridgeBinding

class AddIngredient : AppCompatActivity() {
    private lateinit var binding: ActivityAddIngredientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ingredientName = intent.getStringExtra("ingredient_name")

        binding = ActivityAddIngredientBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val ingredientNameEditText = findViewById<EditText>(R.id.ingredientName)
        if (ingredientName != null) {
            if(ingredientName.isNotEmpty())
            {
                ingredientNameEditText.setText(ingredientName)
            }
        }

        val createIngredientButton = findViewById<Button>(R.id.createIngredient)
        createIngredientButton.setOnClickListener {
            val name = ingredientNameEditText.text.toString()
            if (name.isNotBlank()) {
                val db = MaBDHelper(this)
                db.addIngredient(name)
                val returnIntent = Intent()
                returnIntent.putExtra("name_ingredient", name)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }
    }
}

