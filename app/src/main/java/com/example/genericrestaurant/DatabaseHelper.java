package com.example.genericrestaurant;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    // Table Name
    public static final String TABLE_MENU = "menu";

    // Table columns
    public static final String FOOD_ID   = "f_id";
    public static final String FOOD_NAME = "Food_Name";
    public static final String FOOD_TYPE = "Food_type";
    public static final String FOOD_COST = "Food_cost";


    // Database Information
    static final String DB_NAME = "restaurant.db";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_MENU + "(" + FOOD_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FOOD_NAME + " TEXT NOT NULL, " + FOOD_TYPE + " TEXT NOT NULL, " + FOOD_COST + " INTEGER NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        onCreate(db);
    }


}
