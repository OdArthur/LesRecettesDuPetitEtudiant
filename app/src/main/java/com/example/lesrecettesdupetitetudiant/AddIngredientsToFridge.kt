package com.example.lesrecettesdupetitetudiant

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lesrecettesdupetitetudiant.databinding.ActivityAddIngredientsToFridgeBinding

class AddIngredientsToFridge : AppCompatActivity() {
    private lateinit var binding: ActivityAddIngredientsToFridgeBinding
    private lateinit var st: String
    private val itemBackgroundStates = mutableSetOf<String>()
    private var selectedIndexInOriginalList = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddIngredientsToFridgeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        st = ""

        val db = MaBDHelper(this)

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val listView = findViewById<ListView>(R.id.listIngredients)

        val selectedIngredients = HashMap<String, Int>()

        db.searchAndDisplayIngredients(listView, "", selectedIngredients, itemBackgroundStates)

        listView.setOnItemClickListener { parent, _, position, _ ->
            val selectedIngredient = parent.getItemAtPosition(position) as String
            val currentCount = selectedIngredients.getOrDefault(selectedIngredient, 0)
            selectedIngredients[selectedIngredient] = currentCount + 1
            if(!itemBackgroundStates.contains(selectedIngredient))
            {
                itemBackgroundStates.add(selectedIngredient)
            }
            // Store the index of the selected item in the original list
            selectedIndexInOriginalList = position
            db.searchAndDisplayIngredients(listView, st, selectedIngredients, itemBackgroundStates)
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                db.searchAndDisplayIngredients(listView, s.toString(), selectedIngredients, itemBackgroundStates)
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
                    db.searchAndDisplayIngredients(listView, name, selectedIngredients, itemBackgroundStates)
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