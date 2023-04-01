package com.example.lesrecettesdupetitetudiant

import android.R
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class MaBDHelper(MyContext: Context) : SQLiteOpenHelper(MyContext, NOM_BD, null, VERSION_BD) {
    private val context = MyContext

    companion object {
        private const val NOM_BD = "DB_LRDPE.db"
        private const val VERSION_BD = 1

        private const val TBL_FRIGIDAIRE = "tbl_frigidaire"
        private const val INGREDIENT_ID_FRIGIDAIRE = "ingredient_id_frigidaire"
        private const val QUANT_FRIGIDAIRE = "quant_frigidaire"
        private const val ID_TABLE_FRIGIDAIRE = "id_table_frigidaire"

        private const val TBL_RECETTE = "tbl_recette"
        private const val ID_TABLE_RECETTE = "id_table_recette"
        private const val TITLE_RECETTE = "title_recette"
        private const val DESC_RECETTE = "desc_recette"
        private const val FAV_RECETTE = "fav_recette"

        private const val TBL_PANIER = "tbl_panier"
        private const val INGREDIENT_ID_PANIER = "ingredient_id_panier"
        private const val QUANT_PANIER = "quant_panier"
        private const val ID_TABLE_PANIER = "id_table_panier"

        private const val TBL_INGREDIENT = "tbl_ingredient"
        private const val NAME_INGREDIENT= "name_ingredient"
        private const val UNIT_ID_INGREDIENT="unit_id_ingredient"
        private const val ID_TABLE_INGREDIENT="id_table_ingredient"

        private const val TBL_UNIT = "tbl_unit"
        private const val ID_TABLE_UNIT = "id_table_unit"
        private const val NAME_UNIT = "name_unit"

        private const val TBL_INGREDIENT_REQUIS = "tbl_ingredient_requis"
        private const val ID_TABLE_INGREDIENT_REQUIS = "id_table_ingredient_requis"
        private const val RECETTE_ID_INGREDIENT_REQUIS = "recette_id_ingredient_requis"
        private const val INGREDIENT_ID_INGREDIENT_REQUIS = "ingredient_id_ingredient_requis"
        private const val QUANT_INGREDIENT_REQUIS = "quant_ingredient_requis"


    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTblUnit=("CREATE TABLE " + TBL_UNIT + " (" + ID_TABLE_UNIT + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_UNIT + " VARCHAR(50) UNIQUE "
                +");")
        db.execSQL(createTblUnit)


        val createTblIngredient = ("CREATE TABLE " + TBL_INGREDIENT + " (" + ID_TABLE_INGREDIENT + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_INGREDIENT + " VARCHAR(50) UNIQUE, "
                + UNIT_ID_INGREDIENT + " INTEGER, FOREIGN KEY (" + UNIT_ID_INGREDIENT + ") REFERENCES " + TBL_UNIT + " (" + ID_TABLE_UNIT + ") );" )
        db.execSQL(createTblIngredient)


        val createTblRecette=("CREATE TABLE " + TBL_RECETTE + " (" + ID_TABLE_RECETTE + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE_RECETTE + " VARCHAR(130), "
                + DESC_RECETTE + " TEXT, " + FAV_RECETTE + " INTEGER NOT NULL DEFAULT 0);")
        db.execSQL(createTblRecette)


        val createTblFrigidaire = ("CREATE TABLE " + TBL_FRIGIDAIRE + " (" + ID_TABLE_FRIGIDAIRE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QUANT_FRIGIDAIRE + " INTEGER, " + INGREDIENT_ID_FRIGIDAIRE + " INTEGER, FOREIGN KEY (" + INGREDIENT_ID_FRIGIDAIRE + ") REFERENCES " + TBL_INGREDIENT + " (" + ID_TABLE_INGREDIENT + ") );")
        db.execSQL(createTblFrigidaire)


        val createTblPanier = ("CREATE TABLE " + TBL_PANIER + " (" + ID_TABLE_PANIER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QUANT_PANIER + " INTEGER, " + INGREDIENT_ID_PANIER + " INTEGER, FOREIGN KEY (" + INGREDIENT_ID_PANIER + ") REFERENCES " + TBL_INGREDIENT + " (" + ID_TABLE_INGREDIENT + ") );")
        db.execSQL(createTblPanier)


        val createTblIngredientrequis = ("CREATE TABLE " + TBL_INGREDIENT_REQUIS + " (" + ID_TABLE_INGREDIENT_REQUIS + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RECETTE_ID_INGREDIENT_REQUIS + " INTEGER, " + INGREDIENT_ID_INGREDIENT_REQUIS + " INTEGER, " + QUANT_INGREDIENT_REQUIS + " INTEGER, FOREIGN KEY (" + INGREDIENT_ID_INGREDIENT_REQUIS + ") REFERENCES " + TBL_INGREDIENT + " (" + ID_TABLE_INGREDIENT + "), FOREIGN KEY (" + RECETTE_ID_INGREDIENT_REQUIS + ") REFERENCES " + TBL_RECETTE + " (" + ID_TABLE_RECETTE + ") );")
        db.execSQL(createTblIngredientrequis)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS MaTable")
        onCreate(db)
    }

    fun addRecipe(title:String, Description:String)
    {
        val db:SQLiteDatabase = this.writableDatabase
        val cv:ContentValues = ContentValues()

        cv.put(TITLE_RECETTE, title)
        cv.put(DESC_RECETTE, Description)
        val result = db.insert(TBL_RECETTE, null, cv)
        if (result.toInt() == -1)
        {
            Toast.makeText(context, "adding a recipe have failed", Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(context, "adding a recipe have succeded", Toast.LENGTH_SHORT).show()
        }
    }

    fun displayRecipe(listView: ListView):ArrayList<Int> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TBL_RECETTE", null)
        val listItems = ArrayList<String>()
        val listItemsID = ArrayList<Int>()

        if (cursor.count == 0) {
            listItems.add("Vous n'avez aucune recette ç_ç")
        } else {
            if (cursor.moveToFirst()) {
                do {
                    val RecipeTitle = cursor.getString(cursor.getColumnIndexOrThrow(TITLE_RECETTE))
                    val RecipeID = cursor.getInt(cursor.getColumnIndexOrThrow(ID_TABLE_RECETTE))
                    listItems.add(RecipeTitle)
                    listItemsID.add(RecipeID)
                } while (cursor.moveToNext())
            }
        }

        val adapter = ArrayAdapter(this.context, R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter
        return listItemsID
    }

    fun GetRecipe(ID:Int):Cursor
    {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TBL_RECETTE WHERE $ID_TABLE_RECETTE = $ID", null)

    }

    fun EditRecipe(RecipeTitle:String, RecipeDescription:String, RecipeID:Int)
    {
        val db = this.writableDatabase

        var cv:ContentValues = ContentValues()
        cv.put(TITLE_RECETTE, RecipeTitle)
        cv.put(DESC_RECETTE, RecipeDescription)

        db.update(TBL_RECETTE, cv, "$ID_TABLE_RECETTE = $RecipeID", null)
    }

    fun FavRecipe(RecipeID: Int, Fav:Boolean)
    {
        val db = this.writableDatabase
        var cv:ContentValues = ContentValues()
        cv.put(FAV_RECETTE, Fav)

        db.update(TBL_RECETTE, cv, "$ID_TABLE_RECETTE = $RecipeID",null)
    }

    fun searchAndDisplayRecipe(listView: ListView, searchQuery: String?, IsFav:Boolean) {
        val db = this.readableDatabase
        val listItems = ArrayList<String>()

        var cursor:Cursor
        if(IsFav)
        {
            cursor = db.rawQuery("SELECT * FROM $TBL_RECETTE WHERE $TITLE_RECETTE LIKE '%$searchQuery%' AND $FAV_RECETTE = 1", null)
        }
        else
        {
            cursor = db.rawQuery("SELECT * FROM $TBL_RECETTE WHERE $TITLE_RECETTE LIKE '%$searchQuery%'", null)
        }


        if (cursor?.moveToFirst() == true) {
            do {
                val nameRecipe = cursor.getString(cursor.getColumnIndexOrThrow(TITLE_RECETTE))
                listItems.add(nameRecipe)
            } while (cursor.moveToNext())
        } else {
            listItems.add("Aucun résultat trouvé.")
        }

        val adapter = ArrayAdapter(this.context, R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter
        cursor.close()
    }

    fun deleteRecipe(RecipeID: Int)
    {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TBL_RECETTE WHERE $ID_TABLE_RECETTE = $RecipeID")
        Toast.makeText(context, "Deleted Recipe",Toast.LENGTH_SHORT).show()
    }

    fun addToBasket(name:String, unit:String, quantity:Int)
    {
        val db:SQLiteDatabase = this.writableDatabase
        val cv:ContentValues = ContentValues()

        //cv.put(INGREDIENT_ID_PANIER, unit)
        cv.put(QUANT_PANIER, quantity)

        val result = db.insert(TBL_PANIER, null, cv)
        if (result.toInt() == -1)
        {
            Toast.makeText(context, "adding to basket have failed", Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(context, "adding to basket have succeded", Toast.LENGTH_SHORT).show()
        }
    }

    fun displayFridge(listView: ListView) {
        Log.d("TAG" , "display fridge function called")
        val db = this.readableDatabase
        val query = "SELECT TBL_INGREDIENT.NAME_INGREDIENT, TBL_FRIGIDAIRE.QUANT_FRIGIDAIRE " +
                "FROM TBL_FRIGIDAIRE " +
                "INNER JOIN TBL_INGREDIENT " +
                "ON TBL_FRIGIDAIRE.INGREDIENT_ID_FRIGIDAIRE = TBL_INGREDIENT.ID_TABLE_INGREDIENT"

        val cursor = db.rawQuery(query, null)
        val listItems = ArrayList<String>()

        if (cursor.count == 0) {
            listItems.add("Le frigidaire est vide")
        } else {
            if (cursor.moveToFirst()) {
                do {
                    val ingredientName = cursor.getString(cursor.getColumnIndexOrThrow(NAME_INGREDIENT))
                    val quantite = cursor.getDouble(cursor.getColumnIndexOrThrow(QUANT_FRIGIDAIRE)).toInt()
                    listItems.add(" quantité : $quantite - Ingredient : $ingredientName")
                } while (cursor.moveToNext())
            }
        }

        val adapter = ArrayAdapter(this.context, R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter

        cursor.close()
    }

    fun GetIngredient() : ArrayList<String> {
        val db = this.readableDatabase
        val listItems = ArrayList<String>()

        val cursor = db.rawQuery("SELECT * FROM $TBL_INGREDIENT", null)

        if (cursor?.moveToFirst() == true) {
            do {
                val nameIngredient = cursor.getString(cursor.getColumnIndexOrThrow(NAME_INGREDIENT))
                listItems.add(nameIngredient)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listItems
    }


    fun searchAndDisplayIngredients(listView: ListView, searchQuery: String?, ingredientClickCounts: HashMap<String, Int>?, itemBackgroundStates: MutableSet<String>) {
        val db = this.readableDatabase
        val listItems = ArrayList<String>()
        val clickCounts = ArrayList<Int>()

        val cursor = db.rawQuery("SELECT * FROM $TBL_INGREDIENT WHERE name_ingredient LIKE '%$searchQuery%'", null)

        if (cursor?.moveToFirst() == true) {
            do {
                val nameIngredient = cursor.getString(cursor.getColumnIndexOrThrow(NAME_INGREDIENT))
                listItems.add(nameIngredient)
                val clickCount = ingredientClickCounts?.get(nameIngredient) ?: 0 // Récupérez la valeur correspondante de la HashMap, ou 0 si elle n'existe pas
                clickCounts.add(clickCount) // Ajoutez la valeur correspondante à la nouvelle liste
            } while (cursor.moveToNext())
        } else {
            listItems.add("Aucun résultat trouvé.")
        }

        val adapter = IngredientAdapter(this.context, listItems, clickCounts, itemBackgroundStates)
        listView.adapter = adapter
        cursor.close()
    }

    fun addIngredient(name: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME_INGREDIENT, name)
        db.insert(TBL_INGREDIENT, null, contentValues)
    }
    fun addIngredientsToFridge(ingredients: HashMap<String, Int>) {
        val db = writableDatabase
        for ((ingredient, quantity) in ingredients) {
            val id = getIngredientIdByName(ingredient)
            if (id != -1) {
                val currentQuantity = getIngredientQuantityFromFridge(id)
                if (currentQuantity != -1) {
                    val newQuantity = currentQuantity + quantity
                    val values = ContentValues().apply {
                        put(INGREDIENT_ID_FRIGIDAIRE, id)
                        put(QUANT_FRIGIDAIRE, newQuantity)
                    }
                    db.update(TBL_FRIGIDAIRE, values, "$INGREDIENT_ID_FRIGIDAIRE=?", arrayOf(id.toString()))
                } else {
                    val values = ContentValues().apply {
                        put(INGREDIENT_ID_FRIGIDAIRE, id)
                        put(QUANT_FRIGIDAIRE, quantity)
                    }
                    db.insert(TBL_FRIGIDAIRE, null, values)
                }
            } else {
                Log.e("TAG", "Ingredient $ingredient not found")
            }
        }
    }

    private fun getIngredientQuantityFromFridge(ingredientId: Int): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $QUANT_FRIGIDAIRE FROM $TBL_FRIGIDAIRE WHERE $INGREDIENT_ID_FRIGIDAIRE = $ingredientId", null)
        var quantity = -1
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndexOrThrow(QUANT_FRIGIDAIRE))
        }
        cursor.close()
        return quantity
    }

    private fun getIngredientIdByName(name: String): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $ID_TABLE_INGREDIENT FROM $TBL_INGREDIENT WHERE $NAME_INGREDIENT = ?", arrayOf(name))
        val result = if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndexOrThrow(ID_TABLE_INGREDIENT))
        } else {
            -1
        }
        cursor.close()
        return result
    }

}
