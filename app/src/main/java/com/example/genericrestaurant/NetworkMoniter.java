package com.example.genericrestaurant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkMoniter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
       /* if(checkNetworkConnection(context))
        {
             DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase database = dbHelper.getWritableDatabase();

/*            Cursor cursor;
            cursor = dbHelper.readFromLocalDatabase(database);

            while(cursor.moveToNext())
            {
                //int sync_status = cursor.getColumnIndex(DBManager.SYNC_STATUS);

            }
        }*/

    }


    /*public boolean checkNetworkConnection(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo();
        return (networkInfo!= null && networkInfo.isConnected());
    }*/
}
