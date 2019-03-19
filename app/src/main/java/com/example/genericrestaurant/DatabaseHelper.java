package com.example.genericrestaurant;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Table Name
    public static final String TABLE_MENU = "menu";
    public static final String TABLE_CART = "cart";

    // Table columns
    public static final String FOOD_ID = "ID_Menu";
    public static final String FOOD_NAME = "Name_Menu";
    public static final String FOOD_TYPE = "Type_Menu";
    public static final String FOOD_IMG = "Image_Menu";
    public static final String FOOD_COST = "Cost";

    public static final String QUANTITY = "Item_Qty";
    public static final String POSITION = "Position";


    public DatabaseHelper getDatabaseHelperContext(){
        return this;
    }


    // Database Information
    static final String DB_NAME = "restaurant.db";


    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE_MENU = "create table " + TABLE_MENU + "(" + FOOD_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FOOD_NAME + " TEXT NOT NULL, " + FOOD_TYPE + " TEXT NOT NULL, " + FOOD_IMG + " INTEGER, " + FOOD_COST + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_CART = "create table " + TABLE_CART + "("
            /* + POSITION + " INTEGER PRIMARY KEY, " */
            + FOOD_ID + " INTEGER REFERENCES " + TABLE_MENU + "(" + FOOD_ID + "), " + QUANTITY + " INTEGER);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_MENU);
        MenuCard menuCard;
        //menuCard= new MenuCard("Veg Burger database", "100", "Veg.", 0);
        //insertMenuItem(db, menuCard);
        Log.d("insert-item", "Inserted item succesfully 1");
        //menuCard = new MenuCard("Chicken Burger database", "100", "Non Veg.", 1);
        //insertMenuItem(db, menuCard);
        menuCard = new MenuCard("Veg Burger", "70", "Veg.",0);
        insertMenuItem(db,menuCard);
        menuCard = new MenuCard("Chicken Burger", "80", "Non-Veg.",1);
        insertMenuItem(db,menuCard);
        menuCard = new MenuCard("Fries", "50", "Veg.",0);
        insertMenuItem(db,menuCard);
        menuCard = new MenuCard("Coke", "40", "Veg.",0);
        insertMenuItem(db,menuCard);
        Log.d("insert-item", "Inserted item succesfully");

        db.execSQL(CREATE_TABLE_CART);
        //insertCartItem(db, 2, 2);
        //insertCartItem(db, 4, 3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        onCreate(db);
    }

    public boolean insertMenuItem(SQLiteDatabase db,MenuCard menuCard) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.FOOD_NAME, menuCard.food_name);
        contentValue.put(DatabaseHelper.FOOD_TYPE, menuCard.food_type);
        contentValue.put(DatabaseHelper.FOOD_COST, menuCard.food_cost);
        contentValue.put(DatabaseHelper.FOOD_IMG, menuCard.img_type);
        long result = db.insert(DatabaseHelper.TABLE_MENU, null, contentValue);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor fetchMenuItems(SQLiteDatabase db) {
        String[] columns = new String[]{DatabaseHelper.FOOD_ID, DatabaseHelper.FOOD_NAME, DatabaseHelper.FOOD_TYPE, DatabaseHelper.FOOD_COST, DatabaseHelper.FOOD_IMG};
        Cursor cursor = db.query(DatabaseHelper.TABLE_MENU, columns, null, null, null, null, null);

        return cursor;
    }

    public int updateMenuItems(SQLiteDatabase db, long _id, String name, String desc, int cost) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FOOD_NAME, name);
        contentValues.put(DatabaseHelper.FOOD_TYPE, desc);
        contentValues.put(DatabaseHelper.FOOD_COST, cost);
        int i = db.update(DatabaseHelper.TABLE_MENU, contentValues, DatabaseHelper.FOOD_ID + " = " + _id, null);
        return i;
    }

    public boolean insertCartItem(SQLiteDatabase db, /*int position,*/ int Food_ID, int quantity)
    {
        ContentValues contentValues = new ContentValues();
        //contentValues.put(DatabaseHelper.POSITION, position);
        contentValues.put(DatabaseHelper.FOOD_ID, Food_ID);
        contentValues.put(DatabaseHelper.QUANTITY, quantity);
        long result = db.insert(DatabaseHelper.TABLE_CART, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor fetchCartItems(SQLiteDatabase db)
    {
        String[] columns = new String[]
                {
                  DatabaseHelper.FOOD_ID,
                  DatabaseHelper.QUANTITY
                };
        Cursor cursor = db.query(DatabaseHelper.TABLE_CART, columns, null, null, null,null,null);

        return cursor;
    }

    public boolean removeCartItem(SQLiteDatabase db, int position)
    {

            //db.rawQuery("DELETE FROM " + TABLE_CART + " WHERE " + POSITION + " = " + position, null);
            db.delete(DatabaseHelper.TABLE_CART, DatabaseHelper.FOOD_ID + "=" + position,null);

            return true;
    }

    public Cursor fetchMenuItem(SQLiteDatabase db, int Food_ID)
    {
        String[] columns = new String[]
                {DatabaseHelper.FOOD_ID,
                        DatabaseHelper.FOOD_NAME,
                        DatabaseHelper.FOOD_TYPE,
                        DatabaseHelper.FOOD_COST,
                        DatabaseHelper.FOOD_IMG};
        return db.rawQuery("SELECT * FROM " + TABLE_MENU + " WHERE " + FOOD_ID + " = " + Food_ID,null);
    }

    public boolean arrangeCart(SQLiteDatabase db)
    {
        int count=0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.POSITION,count);
        db.update(DatabaseHelper.TABLE_CART,contentValues,count + " = RANK = " + count+1,null);
        return true;
    }

    public void delete(SQLiteDatabase db, String id) {
        db.delete(DatabaseHelper.TABLE_MENU, DatabaseHelper.FOOD_ID + "=" + id, null);

    }


}



