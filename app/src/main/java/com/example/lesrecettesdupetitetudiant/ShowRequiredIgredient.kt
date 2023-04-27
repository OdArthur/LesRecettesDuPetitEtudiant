package com.example.lesrecettesdupetitetudiant

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

            binding.BTNAddToBasket.setOnClickListener{
                view ->
                val builder = AlertDialog.Builder(this)
                var message = "Etes-vous certain de vouloir ajouter les ingrédients requis pour la recette au panier ?"

                builder.setMessage(message)

                builder.setPositiveButton("Ajouter") { _, _ ->
                    db.AddIngredientFromRecipe(RecipeID)
                    Toast.makeText(this, "Les éléments ont bien été ajouté", Toast.LENGTH_SHORT).show()
                }
                builder.setNegativeButton("Annuler", null)

// Créer et afficher la boîte de dialogue
                val dialog = builder.create()
                dialog.show()
            }
        }

    }
}