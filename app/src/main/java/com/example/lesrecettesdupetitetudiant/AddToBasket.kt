package com.example.lesrecettesdupetitetudiant

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lesrecettesdupetitetudiant.databinding.ActivityAddToBasketBinding

class AddToBasket : AppCompatActivity() {
    private lateinit var binding: ActivityAddToBasketBinding
    private lateinit var st: String
    private val itemBackgroundStates = mutableSetOf<String>()
    private val db = MaBDHelper(this)
    private lateinit var listView: ListView
    private val selectedIngredients = HashMap<String, Int>()

    private val onItemClick: (selectedIngredient: String) -> Unit = { selectedIngredient ->
        val currentCount = selectedIngredients.getOrDefault(selectedIngredient, 0)
        selectedIngredients[selectedIngredient] = currentCount + 1
        if (!itemBackgroundStates.contains(selectedIngredient)) {
            itemBackgroundStates.add(selectedIngredient)
        }
        callSearchAndDisplayIngredientsDbFunction()
    }

    private val callSearchAndDisplayIngredientsDbFunction: () -> Unit = {
        db.searchAndDisplayIngredients(
            listView,
            st,
            selectedIngredients,
            itemBackgroundStates,
            onItemClick,
            showDeleteConfirmationDialog
        )
    }

    private val showDeleteConfirmationDialog: (ingredientName: String) -> Unit = { ingredientName ->
        val builder = AlertDialog.Builder(this)
        var message = "Voulez-vous vraiment supprimer cet ingrédient ?"
        if(db.isIngredientUsedInRecipe(ingredientName))
        {
            message+= "\nCette ingrédient est utiliser dans l'une de vos recettes."
            Log.d("TAG", "Ingrédient déjà utliser dans une recette")
        }
        if(db.isIngredientInCart(ingredientName))
        {
            message+= "\nCette ingrédient est utiliser dans votre panier, il sera supprimer de votre panier."
            Log.d("TAG", "Ingrédient déjà utliser dans le panier")
        }

        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Supprimer") { _, _ ->
                if(db.isIngredientUsedInRecipe(ingredientName)) db.deleteRecipesWithIngredient(ingredientName)
                if(db.isIngredientInCart(ingredientName)) db.deleteIngredientFromCart(ingredientName)
                db.deleteIngredient(ingredientName)
                callSearchAndDisplayIngredientsDbFunction()
            }
            .setNegativeButton("Annuler") { dialog, _ -> dialog.dismiss() }
        val alert = builder.create()
        alert.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddToBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        st = ""
        listView = binding.listIngredients
        val searchEditText = binding.searchEditText

        callSearchAndDisplayIngredientsDbFunction()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                st = s.toString()
                callSearchAndDisplayIngredientsDbFunction()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })


        val addBtn = binding.addBTN
        addBtn.setOnClickListener {
            db.addIngredientsToBasket(selectedIngredients)
            finish()
        }

        val addIngredientActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val name = data?.getStringExtra("name_ingredient")
                    if (name != null) {
                        searchEditText.setText(name)
                    }
                }
            }

        val createButton = binding.createIngredients
        createButton.setOnClickListener {
            val intent = Intent(this, AddIngredient::class.java)
            val ingredientName = searchEditText.text.toString()
            intent.putExtra("ingredient_name", ingredientName)
            addIngredientActivityResult.launch(intent)
        }
    }

}