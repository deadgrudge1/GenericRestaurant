package com.example.genericrestaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

class DBManager
{
    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {

        dbHelper.close();

    }

    public void  insertMenuItem(MenuCard menuCard) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.FOOD_NAME, menuCard.food_name);
        contentValue.put(DatabaseHelper.FOOD_TYPE, menuCard.food_type);
        contentValue.put(DatabaseHelper.FOOD_COST, menuCard.food_cost);
        database.insert(DatabaseHelper.TABLE_MENU, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper.FOOD_ID, DatabaseHelper.FOOD_NAME, DatabaseHelper.FOOD_TYPE, DatabaseHelper.FOOD_COST };
        Cursor cursor = database.query(DatabaseHelper.TABLE_MENU, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name, String desc,int cost) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FOOD_NAME, name);
        contentValues.put(DatabaseHelper.FOOD_TYPE, desc);
        contentValues.put(DatabaseHelper.FOOD_COST,cost);
        int i = database.update(DatabaseHelper.TABLE_MENU, contentValues, DatabaseHelper.FOOD_ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_MENU, DatabaseHelper.FOOD_ID + "=" + _id, null);
    }


}
