package com.example.lesrecettesdupetitetudiant

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.lesrecettesdupetitetudiant.databinding.ActivityShowRecipeBinding
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class ShowRecipe : AppCompatActivity() {

    private lateinit var binding: ActivityShowRecipeBinding
    private lateinit var db: MaBDHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_recipe)

        var RecipeID = intent.getIntExtra("ID", -1)

        binding = ActivityShowRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MaBDHelper(binding.root.context)
        if (RecipeID == -1) {
            finish()
        } else {
            val cursor = db.GetRecipe(RecipeID)
            cursor.moveToFirst()
            binding.RecipeTitle.text =
                cursor.getString(cursor.getColumnIndexOrThrow("title_recette"))
            binding.RecipeDescription.text =
                cursor.getString(cursor.getColumnIndexOrThrow("desc_recette"))
            binding.RecipeDescription.movementMethod = ScrollingMovementMethod()

            binding.favRecette.isChecked =
                toBoolean(cursor.getInt(cursor.getColumnIndexOrThrow("fav_recette")))

            binding.favRecette.setOnCheckedChangeListener { _, isChecked ->
                db.FavRecipe(RecipeID, isChecked)
            }

            binding.editRecipe.setOnClickListener { view ->
                intent = Intent(this, EditRecipe::class.java)
                intent.putExtra("ID", RecipeID)
                startActivity(intent)
            }

            binding.editRecipeIngredient.setOnClickListener { view ->
                intent = Intent(this, ShowRequiredIgredient::class.java)
                intent.putExtra("ID", RecipeID)
                startActivity(intent)
            }

            binding.BTNRecipeLink.setOnClickListener { view ->
                if (cursor.getString(cursor.getColumnIndexOrThrow("link_recette")) == "")
                {
                    Toast.makeText(this, "Aucun lien n'a été renseigné.", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    if(ConnexionInternet.isConnectedInternet(this))
                    {
                        Toast.makeText(this, "Le téléphone est bien connecté à internet", Toast.LENGTH_SHORT).show()
                        var url = cursor.getString(cursor.getColumnIndexOrThrow("link_recette"))
                        if(!url.startsWith("http://") && !url.startsWith("https://"))
                        {
                            url = "http://" + url
                        }
                        val browserIntent : Intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(browserIntent)
                    }
                    else
                    {
                        Toast.makeText(this, "Le téléphone n'est pas connecté à internet", Toast.LENGTH_LONG).show()
                    }


                }
            }

            binding.BTNComplete.setOnClickListener { view ->
                val builder = AlertDialog.Builder(this)
                var message = "Utiliser vos ingrédients enlèvera la quantité requise pour la recette du frigidaire"

                builder.setMessage(message)

                builder.setPositiveButton("Confirmer") { _, _ ->
                    db.completeRecipe(RecipeID)
                    Toast.makeText(view.context, "Les ingrédients ont été utiliser avec succés !", Toast.LENGTH_LONG).show()
                }
                builder.setNegativeButton("Annuler", null)

// Créer et afficher la boîte de dialogue
                val dialog = builder.create()
                dialog.show()
            }

        }
    }

    override fun onResume() {
        super.onResume()

        var RecipeID = intent.getIntExtra("ID", -1)

        val cursor = db.GetRecipe(RecipeID)
        cursor.moveToFirst()
        binding.RecipeTitle.text = cursor.getString(cursor.getColumnIndexOrThrow("title_recette"))
        binding.RecipeDescription.text =
            cursor.getString(cursor.getColumnIndexOrThrow("desc_recette"))
    }

    private fun toBoolean(number: Int): Boolean {
        return number == 1
    }
}