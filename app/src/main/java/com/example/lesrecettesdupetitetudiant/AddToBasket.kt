package com.example.lesrecettesdupetitetudiant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.lesrecettesdupetitetudiant.databinding.ActivityAddToBasketBinding

class AddToBasket : AppCompatActivity(){
    private lateinit var binding: ActivityAddToBasketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddToBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var db: MaBDHelper = MaBDHelper(this)
        binding.IngredientDropDown.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, db.GetIngredient())

            binding.BTNAdd.setOnClickListener{
                view ->
            db.addToBasket(
                "IngredientName",
                "IngredientUnit",
                binding.BasketQuantity.text.toString().trim().toInt(),
            )
            /*Snackbar.make(view, binding.BasketTitle.text.toString().trim(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()*/
        }
    }
}