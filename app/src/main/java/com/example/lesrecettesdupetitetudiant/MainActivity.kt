package com.example.lesrecettesdupetitetudiant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.lesrecettesdupetitetudiant.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var  sqliteHelper: MaBDHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sqliteHelper = MaBDHelper(this)

        setSupportActionBar(binding.appBarMain.toolbar)

        val Return = intent.getStringExtra("Return")

        if(Return == "Recipe")
        {

            Snackbar.make(binding.navView, "Recettes", Snackbar.LENGTH_SHORT).setAction("Action", null).show()
        }

        binding.appBarMain.fab.setOnClickListener {
            view -> if(binding.appBarMain.toolbar.title.toString() == "Recettes")
            {
                /*Snackbar.make(view, "Ajouter une nouvelle recette", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()*/
                intent = Intent(this, AddRecipe::class.java)
                startActivity(intent)
            }
            else if(binding.appBarMain.toolbar.title.toString() == "Panier")
            {
                /*Snackbar.make(view, "Ajouter un ingrédient au panier", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()*/

                intent = Intent(this, AddToBasket::class.java)
                startActivity(intent)
            }
            else if( binding.appBarMain.toolbar.title.toString() == "Réfrigérateur" )
            {
                intent = Intent(this, AddIngredientsToFridge::class.java)
                startActivity(intent)
            }
            else
            {
                Log.d("LE DEBUG VRAIMENT UTILE : ", binding.appBarMain.toolbar.title.toString())
            }

        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}