package com.example.lesrecettesdupetitetudiant

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import com.example.lesrecettesdupetitetudiant.databinding.ActivityEditRecipeBinding
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class EditRecipe : AppCompatActivity() {

    private lateinit var binding: ActivityEditRecipeBinding
    private lateinit var db:MaBDHelper

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
            binding.recipeLink.setText(cursor.getString(cursor.getColumnIndexOrThrow("link_recette")))
        }

        binding.BTNEdit.setOnClickListener{
            view->
            if(binding.recipeLink.text.toString() == "" || isURL(binding.recipeLink.text.toString()))
            {
                db.EditRecipe(binding.recipeTitle.text.toString(), binding.recipeDescription.text.toString(), RecipeID, binding.recipeLink.text.toString())
                finish()
            }
            else
            {
                binding.recipeLinkLayout.isErrorEnabled = true
                binding.recipeLinkLayout.error = "Mauvais lien"
            }
        }

        binding.recipeLink.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(binding.recipeLinkLayout.isErrorEnabled)
                {
                    binding.recipeLinkLayout.isErrorEnabled = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }
        })

        binding.BTNDelete.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            var message = "Etes-vous certain de vouloir supprimer la recette?"

            builder.setMessage(message)

            builder.setPositiveButton("Supprimer") { _, _ ->
                db.deleteRecipe(RecipeID)
                //TODO go to main menu
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("Return", "Recipe")
                startActivity(intent)
            }
            builder.setNegativeButton("Annuler", null)

// Créer et afficher la boîte de dialogue
            val dialog = builder.create()
            dialog.show()
        }
    }

    fun isURL(url: String): Boolean {
        val p: Pattern = Patterns.WEB_URL
        val m: Matcher = p.matcher(url.lowercase(Locale.getDefault()))
        return m.matches()
    }
}