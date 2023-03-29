package com.example.lesrecettesdupetitetudiant

import android.app.Activity
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
import com.example.lesrecettesdupetitetudiant.databinding.ActivityAddIngredientsToFridgeBinding

class AddIngredientsToFridge : AppCompatActivity() {
    private lateinit var binding: ActivityAddIngredientsToFridgeBinding
    private lateinit var st: String
    companion object {
        const val REQUEST_CODE_ADD_INGREDIENT = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddIngredientsToFridgeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        st = ""

        var db:MaBDHelper = MaBDHelper(this)

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val listView = findViewById<ListView>(R.id.listIngredients)

        val selectedIngredients = HashMap<String, Int>()

        db.searchAndDisplay(listView, "", selectedIngredients)

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedIngredient = parent.getItemAtPosition(position) as String
            val currentCount = selectedIngredients.getOrDefault(selectedIngredient, 0)
            selectedIngredients[selectedIngredient] = currentCount + 1
            //db.searchAndDisplay(listView, st, selectedIngredients)
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                db.searchAndDisplay(listView, s.toString(), selectedIngredients)
                st = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })


        val addBtn = findViewById<Button>(R.id.AddBTN)
        addBtn.setOnClickListener {
            db.addIngredientsToFridge(selectedIngredients)
            finish()
        }

        val addIngredientActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val name = data?.getStringExtra("name_ingredient")
                if (name != null) {
                    val db = MaBDHelper(applicationContext)
                    val listView = findViewById<ListView>(R.id.listIngredients)
                    db.searchAndDisplay(listView, name, selectedIngredients)
                }
            }
        }

        val createButton = findViewById<Button>(R.id.createIngredients)
        createButton.setOnClickListener {
            val intent = Intent(this, AddIngredient::class.java)
            val ingredientName = searchEditText.text.toString()
            intent.putExtra("ingredient_name", ingredientName)
            addIngredientActivityResult.launch(intent)
        }
    }
}