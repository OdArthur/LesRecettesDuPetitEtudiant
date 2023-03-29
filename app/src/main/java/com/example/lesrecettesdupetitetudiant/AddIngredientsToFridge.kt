package com.example.lesrecettesdupetitetudiant

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.size
import com.example.lesrecettesdupetitetudiant.databinding.ActivityAddIngredientsToFridgeBinding

class AddIngredientsToFridge : AppCompatActivity() {
    private lateinit var binding: ActivityAddIngredientsToFridgeBinding
    private lateinit var st: String
    private val itemBackgroundStates = mutableSetOf<String>()
    private var listViewLayoutObserver: ViewTreeObserver.OnGlobalLayoutListener? = null // Ajout de la référence à l'observateur
    private var selectedIndexInOriginalList = -1

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
            view.setBackgroundColor(Color.LTGRAY)
            if(!itemBackgroundStates.contains(selectedIngredient))
            {
                itemBackgroundStates.add(selectedIngredient)
            }

            // Store the index of the selected item in the original list
            selectedIndexInOriginalList = position
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                db.searchAndDisplay(listView, s.toString(), selectedIngredients)
                st = s.toString()
                listViewLayoutObserver?.let { observer ->
                    listView.viewTreeObserver.removeOnGlobalLayoutListener(observer) // Suppression de l'observateur existant
                }
                listViewLayoutObserver = ViewTreeObserver.OnGlobalLayoutListener {
                    for (i in 0 until listView.childCount) {
                        val item = listView.getChildAt(i)
                        val itemName = (item as TextView).text.toString()
                        if (itemBackgroundStates.contains(itemName)) {
                            Log.d("TAG", "Set background to grey for : $itemName")
                            item.setBackgroundColor(Color.LTGRAY)
                        }
                    }
                }
                listView.viewTreeObserver.addOnGlobalLayoutListener(listViewLayoutObserver) // Ajout de l'observateur mis à jour
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