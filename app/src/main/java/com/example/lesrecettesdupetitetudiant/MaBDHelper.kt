package com.example.lesrecettesdupetitetudiant

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
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
        private const val FK_CONSTRAINT_INGREDIENT_ID_FRIGIDAIRE = "fk_contraint_ingredient_id_frigidaire"

        private const val TBL_RECETTE = "tbl_recette"
        private const val ID_TABLE_RECETTE = "id_table_recette"
        private const val TITLE_RECETTE = "title_recette"
        private const val DESC_RECETTE = "desc_recette"

        private const val TBL_PANIER = "tbl_panier"
        private const val INGREDIENT_ID_PANIER = "ingredient_id_panier"
        private const val QUANT_PANIER = "quant_panier"
        private const val ID_TABLE_PANIER = "id_table_panier"
        private const val FK_CONSTRAINT_INGREDIENT_ID_PANIER = "fk_constraint_ingredient_id_panier"

        private const val TBL_INGREDIENT = "tbl_ingredient"
        private const val NAME_INGREDIENT= "name_ingredient"
        private const val UNIT_ID_INGREDIENT="unit_id_ingredient"
        private const val ID_TABLE_INGREDIENT="id_table_ingredient"

        private const val TBL_UNIT = "tbl_unit"
        private const val ID_TABLE_UNIT = "id_table_unit"
        private const val NAME_UNIT = "name_unit"
        private const val FK_CONSTRAINT_INGREDIENT_UNIT_ID_INGREDIENT = "fk_constraint_ingredient_unit_id_ingredient"

        private const val TBL_INGREDIENT_REQUIS = "tbl_ingredient_requis"
        private const val ID_TABLE_INGREDIENT_REQUIS = "id_table_ingredient_requis"
        private const val RECETTE_ID_INGREDIENT_REQUIS = "recette_id_ingredient_requis"
        private const val INGREDIENT_ID_INGREDIENT_REQUIS = "ingredient_id_ingredient_requis"
        private const val QUANT_INGREDIENT_REQUIS = "quant_ingredient_requis"
        private const val FK_CONSTRAINT_RECETTE_ID_INGREDIENT_REQUIS = "fk_constraint_recette_id_ingredient_requis"
        private const val FK_CONSTRAINT_INGREDIENT_ID_INGREDIENT_REQUIS = "fk_constraint_ingredient_id_ingredient_requis"


    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTblUnit=("CREATE TABLE " + TBL_UNIT + " (" + ID_TABLE_UNIT + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_UNIT + " VARCHAR(50) UNIQUE "
                +");")
        db.execSQL(createTblUnit)


        val createTblIngredient = ("CREATE TABLE " + TBL_INGREDIENT + " (" + ID_TABLE_INGREDIENT + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_INGREDIENT + " VARCHAR(50) UNIQUE, "
                + UNIT_ID_INGREDIENT + " INTEGER, FOREIGN KEY (" + UNIT_ID_INGREDIENT + ") REFERENCES " + TBL_UNIT + " (" + ID_TABLE_UNIT + ") );" )
        db.execSQL(createTblIngredient)


        val createTblRecette=("CREATE TABLE " + TBL_RECETTE + " (" + ID_TABLE_RECETTE + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE_RECETTE + " VARCHAR(130), "
                + DESC_RECETTE + ");")
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
        if (result.equals(-1))
        {
            Toast.makeText(context, "adding a recipe have failed", Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(context, "adding a recipe have succeded", Toast.LENGTH_SHORT).show()
        }
    }

    fun addToBasket(name:String, unit:String, quantity:Int)
    {
        val db:SQLiteDatabase = this.writableDatabase
        val cv:ContentValues = ContentValues()

        cv.put(NAME_INGREDIENT, name)
        cv.put(QUANT_INGREDIENT_REQUIS, quantity)
        cv.put(UNIT_ID_INGREDIENT, unit)
        val result = db.insert(TBL_PANIER, null, cv)
        if (result.equals(-1))
        {
            Toast.makeText(context, "adding a recipe have failed", Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(context, "adding a recipe have succeded", Toast.LENGTH_SHORT).show()
        }
    }
}


