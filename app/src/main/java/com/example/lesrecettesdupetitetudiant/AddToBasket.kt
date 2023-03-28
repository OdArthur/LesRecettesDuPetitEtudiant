package com.example.lesrecettesdupetitetudiant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.lesrecettesdupetitetudiant.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.example.lesrecettesdupetitetudiant.databinding.ActivityAddToBasketBinding

class AddToBasket : AppCompatActivity(){
    private lateinit var binding: ActivityAddToBasketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddToBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BTNAdd.setOnClickListener{
                view ->
            var db: MaBDHelper = MaBDHelper(this)
            db.addToBasket(
                binding.BasketIngredient.text.toString().trim(),
                binding.BasketUnit.text.toString().trim(),
                binding.BasketQuantity.text.toString().trim().toInt(),
            )
            /*Snackbar.make(view, binding.BasketTitle.text.toString().trim(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()*/
        }
    }
}