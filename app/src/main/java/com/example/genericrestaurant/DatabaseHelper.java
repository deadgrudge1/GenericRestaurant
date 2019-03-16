package com.example.genericrestaurant;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{
    // Table Name
    public static final String TABLE_MENU = "menu";

    // Table columns
    public static final String FOOD_ID   = "ID_Menu";
    public static final String FOOD_NAME = "Name_Menu";
    public static final String FOOD_TYPE = "Type_Menu";
    public static final String FOOD_IMG = "Image_Menu";
    public static final String FOOD_COST = "Cost";


    // Database Information
    static final String DB_NAME = "restaurant.db";
    DBManager dbManager;

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_MENU + "(" + FOOD_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FOOD_NAME + " TEXT NOT NULL, " + FOOD_TYPE + " TEXT NOT NULL, " + FOOD_IMG + " INTEGER, " + FOOD_COST + " INTEGER NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);
        MenuCard menuCard = new MenuCard("Veg Burger database", "100", "Veg.",0);
        dbManager.insertMenuItem(menuCard);
         menuCard = new MenuCard("Chicken Burger database", "100", "Non Veg.",1);
        dbManager.insertMenuItem(menuCard);
        Log.d("insert-item","Inserted item succesfully");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        onCreate(db);
    }


}
