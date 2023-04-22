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
import kotlinx.coroutines.delay
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
        private const val LINK_RECETTE = "link_recette"

        private const val TBL_PANIER = "tbl_panier"
        private const val INGREDIENT_ID_PANIER = "ingredient_id_panier"
        private const val QUANT_PANIER = "quant_panier"
        private const val ID_TABLE_PANIER = "id_table_panier"

        private const val TBL_INGREDIENT = "tbl_ingredient"
        private const val NAME_INGREDIENT= "name_ingredient"
        private const val UNIT_ID_INGREDIENT="unit_id_ingredient"
        private const val ID_TABLE_INGREDIENT="id_table_ingredient"
        private const val FAV_INGREDIENT = "fav_ingredient"

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
                + UNIT_ID_INGREDIENT + " INTEGER, " + FAV_INGREDIENT + " INTEGER NOT NULL DEFAULT 0, FOREIGN KEY (" + UNIT_ID_INGREDIENT + ") REFERENCES " + TBL_UNIT + " (" + ID_TABLE_UNIT + ") );")
        db.execSQL(createTblIngredient)


        val createTblRecette=("CREATE TABLE " + TBL_RECETTE + " (" + ID_TABLE_RECETTE + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE_RECETTE + " VARCHAR(130), "
                + DESC_RECETTE + " TEXT, " + FAV_RECETTE + " INTEGER NOT NULL DEFAULT 0, " + LINK_RECETTE + " VARCHAR(255));")
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

    fun addRecipe(title:String, Description:String, ListIngredient:MutableList<IngredientQuantityData>, link:String)
    {
        val db:SQLiteDatabase = this.writableDatabase
        val cv:ContentValues = ContentValues()

        cv.put(TITLE_RECETTE, title)
        cv.put(DESC_RECETTE, Description)
        cv.put(LINK_RECETTE, link)
        val result = db.insert(TBL_RECETTE, null, cv)
        if (result.toInt() == -1)
        {
            Toast.makeText(context, "adding a recipe have failed", Toast.LENGTH_SHORT).show()
        }
        else
        {
            val cursor = db.rawQuery("SELECT $ID_TABLE_RECETTE FROM $TBL_RECETTE ORDER BY $ID_TABLE_RECETTE DESC LIMIT 1", null)
            cursor.moveToFirst()
            AddIngredientForRecipe(cursor.getInt(cursor.getColumnIndexOrThrow(ID_TABLE_RECETTE)), ListIngredient)
            //Toast.makeText(context, "adding a recipe have succeded", Toast.LENGTH_SHORT).show()
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

    fun EditRecipe(RecipeTitle:String, RecipeDescription:String, RecipeID:Int, RecipeLink: String)
    {
        val db = this.writableDatabase

        var cv:ContentValues = ContentValues()
        cv.put(TITLE_RECETTE, RecipeTitle)
        cv.put(DESC_RECETTE, RecipeDescription)
        cv.put(LINK_RECETTE, RecipeLink)

        db.update(TBL_RECETTE, cv, "$ID_TABLE_RECETTE = $RecipeID", null)
    }

    fun FavRecipe(RecipeID: Int, Fav:Boolean)
    {
        val db = this.writableDatabase
        var cv:ContentValues = ContentValues()
        cv.put(FAV_RECETTE, Fav)

        db.update(TBL_RECETTE, cv, "$ID_TABLE_RECETTE = $RecipeID",null)
    }

    fun searchAndDisplayRecipe(listView: ListView, searchQuery: String?, IsFav:Boolean, isPossible:Boolean) {
        val db = this.readableDatabase
        val listItems = ArrayList<String>()

        var cursorFinal:Cursor
        if(IsFav)
        {
            cursorFinal = db.rawQuery("SELECT * FROM $TBL_RECETTE WHERE $TITLE_RECETTE LIKE '%$searchQuery%' AND $FAV_RECETTE = 1", null)
        }
        else
        {
            cursorFinal = db.rawQuery("SELECT * FROM $TBL_RECETTE WHERE $TITLE_RECETTE LIKE '%$searchQuery%'", null)
        }

        var cursor:Cursor
        if(isPossible)
        {
            Log.d("RECIPE DEBUG ", "IT IS POSSIBLE")
            cursor = db.rawQuery("SELECT $RECETTE_ID_INGREDIENT_REQUIS FROM $TBL_INGREDIENT_REQUIS JOIN $TBL_INGREDIENT ON $TBL_INGREDIENT_REQUIS.$INGREDIENT_ID_INGREDIENT_REQUIS = $TBL_INGREDIENT.$ID_TABLE_INGREDIENT JOIN $TBL_FRIGIDAIRE ON $TBL_INGREDIENT.$ID_TABLE_INGREDIENT = $TBL_FRIGIDAIRE.$INGREDIENT_ID_FRIGIDAIRE WHERE $TBL_INGREDIENT_REQUIS.$QUANT_INGREDIENT_REQUIS > $TBL_FRIGIDAIRE.$QUANT_FRIGIDAIRE", null)
        }
        else
        {
            cursor = db.rawQuery("", null)
        }


        if (cursorFinal?.moveToFirst() == true) {
            do {
                if(isPossible)
                {
                    if (cursor.moveToFirst())
                    {
                        Log.d("RECIPE DEBUG : ", "MOVE TO FIRST")
                        var RecipePossible = true
                        do {
                            if(cursor.getInt(cursor.getColumnIndexOrThrow(RECETTE_ID_INGREDIENT_REQUIS)) == cursorFinal.getInt(cursorFinal.getColumnIndexOrThrow(ID_TABLE_RECETTE)))
                            {
                                RecipePossible=false
                                Log.d("RECIPE DEBUG ", "RECIPE IS NOT POSSIBLE" + cursorFinal.getString(cursorFinal.getColumnIndexOrThrow(TITLE_RECETTE)))
                            }
                        }while(RecipePossible && cursor.moveToNext())
                        if (RecipePossible)
                        {
                            val nameRecipe = cursorFinal.getString(cursorFinal.getColumnIndexOrThrow(TITLE_RECETTE))
                            listItems.add(nameRecipe)
                        }
                    }
                    else
                    {
                        val nameRecipe = cursorFinal.getString(cursorFinal.getColumnIndexOrThrow(TITLE_RECETTE))
                        listItems.add(nameRecipe)
                    }
                }
                else
                {
                    val nameRecipe = cursorFinal.getString(cursorFinal.getColumnIndexOrThrow(TITLE_RECETTE))
                    listItems.add(nameRecipe)
                }
            } while (cursorFinal.moveToNext())
        } else {
            listItems.add("Aucun résultat trouvé.")
        }

        val adapter = ArrayAdapter(this.context, R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter
        cursorFinal.close()
    }

    fun deleteRecipe(RecipeID: Int)
    {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TBL_RECETTE WHERE $ID_TABLE_RECETTE = $RecipeID")
        RemoveIngredientForRecipe(RecipeID)
    }

    fun addIngredientsToBasket(ingredients: HashMap<String, Int>) {
        val db = writableDatabase
        for ((ingredient, quantity) in ingredients) {
            val id = getIngredientIdByName(ingredient)
            if (id != -1) {
                val currentQuantity = getIngredientQuantityFromFridge(id)
                if (currentQuantity != -1) {
                    val newQuantity = currentQuantity + quantity
                    val values = ContentValues().apply {
                        put(INGREDIENT_ID_PANIER, id)
                        put(QUANT_PANIER, newQuantity)
                    }
                    db.update(TBL_PANIER, values, "$INGREDIENT_ID_PANIER=?", arrayOf(id.toString()))
                } else {
                    val values = ContentValues().apply {
                        put(INGREDIENT_ID_PANIER, id)
                        put(QUANT_PANIER, quantity)
                    }
                    db.insert(TBL_PANIER, null, values)
                }
            } else {
                Log.e("TAG", "Ingredient $ingredient not found")
            }
        }
    }

    fun removeIngredientsFromBasket(ingredients: HashMap<String, Int>)
    {
        val db = this.writableDatabase
        for((ingredientName, quantity) in ingredients) {
            val ingredientId = getIngredientIdByName(ingredientName)
            // Retrieve the current quantity of the ingredient in the fridge
            val selectQuery =
                "SELECT $QUANT_PANIER FROM $TBL_PANIER WHERE $INGREDIENT_ID_PANIER = $ingredientId"
            val cursor = db.rawQuery(selectQuery, null)
            var currentQuantity = 0
            if (cursor.moveToFirst()) {
                currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(QUANT_PANIER))
            }
            cursor.close()

            // Calculate the new quantity of the ingredient in the fridge
            val newQuantity = currentQuantity - quantity

            // Update the quantity of the ingredient in the fridge
            if (newQuantity > 0) {
                val updateQuery =
                    "UPDATE $TBL_PANIER SET $QUANT_PANIER = $newQuantity WHERE $INGREDIENT_ID_PANIER = $ingredientId"
                db.execSQL(updateQuery)
            } else {
                val deleteQuery =
                    "DELETE FROM $TBL_PANIER WHERE $INGREDIENT_ID_PANIER = $ingredientId"
                db.execSQL(deleteQuery)
            }
        }
    }

    fun displayFridge(listView: ListView) {
        val db = this.readableDatabase
        val query = "SELECT TBL_INGREDIENT.NAME_INGREDIENT, TBL_FRIGIDAIRE.QUANT_FRIGIDAIRE, TBL_INGREDIENT.FAV_INGREDIENT " +
                "FROM TBL_FRIGIDAIRE " +
                "INNER JOIN TBL_INGREDIENT " +
                "ON TBL_FRIGIDAIRE.INGREDIENT_ID_FRIGIDAIRE = TBL_INGREDIENT.ID_TABLE_INGREDIENT"


        val cursor = db.rawQuery(query, null)
        val listItems = ArrayList<String>()
        val isFavs = ArrayList<Boolean>()
        val quantIngredients = ArrayList<Int>()
        var listIsEmpty = false

        if (cursor?.moveToFirst() == true) {
            do {
                val ingredientName = cursor.getString(cursor.getColumnIndexOrThrow(NAME_INGREDIENT))
                val quantite = cursor.getDouble(cursor.getColumnIndexOrThrow(QUANT_FRIGIDAIRE)).toInt()
                val isFav = cursor.getInt(cursor.getColumnIndexOrThrow(FAV_INGREDIENT))
                if(quantite != 0)
                {
                    listItems.add(ingredientName)
                    quantIngredients.add(quantite)
                    if(isFav == 1)
                        isFavs.add(true)
                    else
                        isFavs.add(false)
                }
            } while (cursor.moveToNext())
        } else
        {
            listIsEmpty = true
            listItems.add("Votre réfrigérateur est vide")
        }

        val adapter = if (listIsEmpty)
            NoResultsAdapter(this.context, listItems)
        else FridgeAdapter(this.context, listItems, quantIngredients, isFavs)

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


    fun searchAndDisplayIngredients(listView: ListView, searchQuery: String?, ingredientClickCounts: HashMap<String, Int>, itemBackgroundStates: MutableSet<String>, onItemClick: (selectedIngredient: String) -> Unit, showDeleteConfirmationDialog: (ingredientName: String) -> Unit) {
        val db = this.readableDatabase
        val listItems = ArrayList<String>()
        val clickCounts = ArrayList<Int>()
        val isFavs = ArrayList<Boolean>()
        var listIsEmpty = false
        val scrollPosition = listView?.firstVisiblePosition ?: 0
        val view = listView?.getChildAt(0)
        val scrollTop = view?.top ?: 0

        val cursor = db.rawQuery("SELECT * FROM $TBL_INGREDIENT WHERE name_ingredient LIKE '%$searchQuery%'", null)

        if (cursor?.moveToFirst() == true) {
            do {
                val nameIngredient = cursor.getString(cursor.getColumnIndexOrThrow(NAME_INGREDIENT))
                listItems.add(nameIngredient)
                val clickCount = ingredientClickCounts?.get(nameIngredient) ?: 0 // Récupérez la valeur correspondante de la HashMap, ou 0 si elle n'existe pas
                clickCounts.add(clickCount) // Ajoutez la valeur correspondante à la nouvelle liste
                val isFav = cursor.getInt(cursor.getColumnIndexOrThrow(FAV_INGREDIENT))
                if(isFav == 1)
                    isFavs.add(true)
                else
                    isFavs.add(false)
            } while (cursor.moveToNext())
        } else {
            listIsEmpty = true
            listItems.add("Aucun résultat trouvé.")
        }


        val adapter = if (listIsEmpty)
            NoResultsAdapter(this.context, listItems)
        else IngredientAdapter(this.context, listItems, clickCounts, itemBackgroundStates, ingredientClickCounts, isFavs, onItemClick, showDeleteConfirmationDialog)
        listView.adapter = adapter

        listView?.post {
            listView?.setSelectionFromTop(scrollPosition, scrollTop)
        }

        cursor.close()
    }

    fun addIngredient(name: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME_INGREDIENT, name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()) else it.toString() })
        val result = db.insert(TBL_INGREDIENT, null, contentValues)
        if(result.toInt() != -1) {
            val cursor = db.rawQuery("SELECT $ID_TABLE_INGREDIENT FROM $TBL_INGREDIENT ORDER BY $ID_TABLE_INGREDIENT DESC LIMIT 1", null)
            if(cursor.moveToFirst())
            {
                initializeIngredientInFridge(cursor.getInt(cursor.getColumnIndexOrThrow(ID_TABLE_INGREDIENT)))
            }
        }
    }

    fun initializeIngredientInFridge(ingredientID: Int)
    {
        val db = writableDatabase
        val cv:ContentValues = ContentValues().apply {
            put(INGREDIENT_ID_FRIGIDAIRE, ingredientID)
            put(QUANT_FRIGIDAIRE, 0)
        }
        db.insert(TBL_FRIGIDAIRE, null, cv)
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

    fun isIngredientUsedInRecipe(IngredientName: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TBL_INGREDIENT_REQUIS " +
                "WHERE $INGREDIENT_ID_INGREDIENT_REQUIS = " +
                "(SELECT $ID_TABLE_INGREDIENT FROM $TBL_INGREDIENT " +
                "WHERE $NAME_INGREDIENT = '$IngredientName')"
        Log.d("TAG", "query: $query")
        val cursor = db.rawQuery(query, null)

        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }

    fun isIngredientInCart(ingredientName: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TBL_PANIER " +
                "WHERE $INGREDIENT_ID_PANIER = " +
                "(SELECT $ID_TABLE_INGREDIENT FROM $TBL_INGREDIENT " +
                "WHERE $NAME_INGREDIENT = '$ingredientName')"
        Log.d("TAG", "query: $query")
        val cursor = db.rawQuery(query, null)

        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }
    fun deleteIngredientFromCart(name: String){
        val db = this.writableDatabase
        val ingredientId = getIngredientIdByName(name)
        db.delete(TBL_PANIER, "$INGREDIENT_ID_PANIER = ?", arrayOf(ingredientId.toString()))
    }

    fun deleteRecipesWithIngredient(ingredientName: String) {
        val db = this.writableDatabase
        var ingredientId = getIngredientIdByName(ingredientName)

        Log.d("TAG", "Ingredient id to delete : " + ingredientId.toString() )
        db.delete(TBL_INGREDIENT_REQUIS, "$INGREDIENT_ID_INGREDIENT_REQUIS = ?", arrayOf(ingredientId.toString()))
    }

    fun deleteIngredient(name: String){
        val db = this.writableDatabase
        val ingredientId = getIngredientIdByName(name)
        db.delete(TBL_INGREDIENT, "$NAME_INGREDIENT=?", arrayOf(name))
        db.delete(TBL_FRIGIDAIRE, "$INGREDIENT_ID_FRIGIDAIRE=?", arrayOf(ingredientId.toString()))
    }

    fun addFavIngredient(ingredientName: String) {
        val ingredientId = getIngredientIdByName(ingredientName)
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(FAV_INGREDIENT, 1)
        db.update(TBL_INGREDIENT, values, "$ID_TABLE_INGREDIENT = ?", arrayOf(ingredientId.toString()))
        db.close()
    }

    fun removeFavIngredient(ingredientName: String) {
        val ingredientId = getIngredientIdByName(ingredientName)
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(FAV_INGREDIENT, 0)
        db.update(TBL_INGREDIENT, values, "$ID_TABLE_INGREDIENT = ?", arrayOf(ingredientId.toString()))
        db.close()
    }

    fun removeIngredientsFromFridge(ingredients: HashMap<String, Int>)
    {
        val db = this.writableDatabase
        for((ingredientName, quantity) in ingredients) {
            val ingredientId = getIngredientIdByName(ingredientName)
            // Retrieve the current quantity of the ingredient in the fridge
            val selectQuery =
                "SELECT $QUANT_FRIGIDAIRE FROM $TBL_FRIGIDAIRE WHERE $INGREDIENT_ID_FRIGIDAIRE = $ingredientId"
            val cursor = db.rawQuery(selectQuery, null)
            var currentQuantity = 0
            if (cursor.moveToFirst()) {
                currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(QUANT_FRIGIDAIRE))
            }
            cursor.close()

            // Calculate the new quantity of the ingredient in the fridge
            val newQuantity = currentQuantity - quantity

            // Update the quantity of the ingredient in the fridge
            if (newQuantity > 0) {
                val updateQuery =
                    "UPDATE $TBL_FRIGIDAIRE SET $QUANT_FRIGIDAIRE = $newQuantity WHERE $INGREDIENT_ID_FRIGIDAIRE = $ingredientId"
                db.execSQL(updateQuery)
            } else {
                val deleteQuery =
                    "DELETE FROM $TBL_FRIGIDAIRE WHERE $INGREDIENT_ID_FRIGIDAIRE = $ingredientId"
                db.execSQL(deleteQuery)
            }
        }
    }

    fun AddIngredientForRecipe(RecipeID:Int, Ingredients:MutableList<IngredientQuantityData>)
    {
        val db = this.writableDatabase
        for((ingredient, quantity) in Ingredients )
        {
            if(ingredient != "0")
            {
                val icv:ContentValues = ContentValues()
                icv.put(RECETTE_ID_INGREDIENT_REQUIS, RecipeID)
                icv.put(INGREDIENT_ID_INGREDIENT_REQUIS, getIngredientIdByName(ingredient))
                icv.put(QUANT_INGREDIENT_REQUIS, quantity)

                db.insert(TBL_INGREDIENT_REQUIS, null, icv)
            }
        }
    }

    fun DisplayRequiredIngredientForRecipe(RecipeID:Int, listView: ListView)
    {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TBL_INGREDIENT_REQUIS WHERE $RECETTE_ID_INGREDIENT_REQUIS = $RecipeID", null)

        val listItems = ArrayList<String>()
        if(cursor.moveToFirst())
        {
            do
            {
                val Icursor = db.rawQuery("SELECT $NAME_INGREDIENT FROM $TBL_INGREDIENT WHERE $ID_TABLE_INGREDIENT = ${cursor.getString(cursor.getColumnIndexOrThrow(INGREDIENT_ID_INGREDIENT_REQUIS))}", null)
                if (Icursor.moveToFirst())
                {
                    listItems.add(Icursor.getString(Icursor.getColumnIndexOrThrow(NAME_INGREDIENT)) + " - " + cursor.getInt(cursor.getColumnIndexOrThrow(QUANT_INGREDIENT_REQUIS)).toString())
                }
            }while(cursor.moveToNext())
        }
        else
        {
            listItems.add("Aucun ingrédient requis pour cette recette.")
        }

        val adapter = ArrayAdapter(this.context, R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter
    }

    fun GetRequiredIngredientForRecipe(RecipeID: Int):ArrayList<String>
    {
        val db = this.readableDatabase
        val listItems = ArrayList<String>()

        val cursor = db.rawQuery("SELECT * FROM $TBL_INGREDIENT_REQUIS WHERE $RECETTE_ID_INGREDIENT_REQUIS = $RecipeID", null)

        if (cursor.moveToFirst())
        {
            do {
                val Icursor = db.rawQuery("SELECT $NAME_INGREDIENT FROM $TBL_INGREDIENT WHERE $ID_TABLE_INGREDIENT = ${cursor.getString(cursor.getColumnIndexOrThrow(INGREDIENT_ID_INGREDIENT_REQUIS))}", null)
                if (Icursor.moveToFirst())
                {
                    do {
                        listItems.add(Icursor.getString(Icursor.getColumnIndexOrThrow(NAME_INGREDIENT)))
                    }while (Icursor.moveToNext())
                }
            }while (cursor.moveToNext())
        }
        return listItems
    }

    fun RemoveIngredientForRecipe(RecipeID:Int)
    {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TBL_INGREDIENT_REQUIS WHERE $RECETTE_ID_INGREDIENT_REQUIS = $RecipeID")
    }

    fun completeRecipe(recipeID: Int) {
        val db = this.writableDatabase
        var Rcursor:Cursor

        Rcursor = db.rawQuery("SELECT $INGREDIENT_ID_INGREDIENT_REQUIS, $QUANT_INGREDIENT_REQUIS FROM $TBL_INGREDIENT_REQUIS WHERE $RECETTE_ID_INGREDIENT_REQUIS = $recipeID", null)
        if (Rcursor.moveToFirst())
        {
            do {
                val cursor = db.rawQuery("SELECT * FROM $TBL_FRIGIDAIRE WHERE $INGREDIENT_ID_FRIGIDAIRE = ${Rcursor.getString(Rcursor.getColumnIndexOrThrow(INGREDIENT_ID_INGREDIENT_REQUIS))} ", null)
                cursor.moveToFirst()
                var newQuant = cursor.getInt(cursor.getColumnIndexOrThrow(QUANT_FRIGIDAIRE)) - Rcursor.getInt(Rcursor.getColumnIndexOrThrow(QUANT_INGREDIENT_REQUIS))
                if (newQuant<0)
                {
                    newQuant = 0
                }
                val cv = ContentValues()
                cv.put(QUANT_FRIGIDAIRE, newQuant)
                db.update(TBL_FRIGIDAIRE, cv, "$ID_TABLE_FRIGIDAIRE = ${cursor.getInt(cursor.getColumnIndexOrThrow(ID_TABLE_FRIGIDAIRE))}", null)
            }while (Rcursor.moveToNext())
        }
    }
}
