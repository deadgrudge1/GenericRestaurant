package com.example.genericrestaurant;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

public class MySingleton {

    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mContext;

    public MySingleton(Context context) {
        requestQueue = getRequestQueue();
        mContext = context;
    }

    private RequestQueue getRequestQueue() {

        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context)
    {

        if(mInstance == null)
        {
            mInstance = new MySingleton(context);
        }

        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> request)
    {



        getRequestQueue().add(request);
    }

}
