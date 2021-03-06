package com.example.genericrestaurant;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class fragment_menu extends Fragment
{
    public ListView menuCardListView;
    ArrayList<MenuCard> menuCardArrayList = new ArrayList<>();
    CustomAdapter foodAdapter;
    ProgressBar progressBar;
    Cursor cursor;
    DatabaseHelper databaseHelper;
    StringRequest stringRequest;
    SwipeRefreshLayout pullToRefresh;
    int status = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //foodAdapter = new CustomAdapter(menu,getContext());
        String string;
        if(savedInstanceState!=null)
        {
            string = savedInstanceState.getString("test");
            Toast.makeText(getContext(), "Yo this works", Toast.LENGTH_SHORT).show();
        }


        return inflater.inflate(R.layout.fragment_menu, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        menuCardListView = view.findViewById(R.id.menu_list);
        progressBar=view.findViewById(R.id.loadingPanel);
        pullToRefresh = view.findViewById(R.id.pullDownToRefresh);


        if(savedInstanceState!=null)
            status = savedInstanceState.getInt("status");
        try {
            if(status == 0)
            loadMenu();

            else if(status == 1)
            {
                databaseHelper = new DatabaseHelper(getActivity());
                menuCardArrayList = new ArrayList<MenuCard>();

                cursor = databaseHelper.fetchMenuItems(databaseHelper.getWritableDatabase());
                if(cursor.getCount() == 0)
                {
                    Toast.makeText(getActivity(), "Database is empty", Toast.LENGTH_SHORT).show();
                }
                else
                    cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String foodname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FOOD_NAME));
                    String foodcost = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FOOD_COST));
                    String foodtype = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FOOD_TYPE));
                    int img_type = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.FOOD_IMG));
                    MenuCard menuCard = new MenuCard(foodname, foodcost, foodtype, img_type);
                    menuCardArrayList.add(menuCard); //add the item
                    cursor.moveToNext();
                }

                foodAdapter = new CustomAdapter(menuCardArrayList, getContext());
                menuCardListView.setAdapter(foodAdapter);
                progressBar.setVisibility(View.GONE);


            }
        }catch (Exception e){

        }


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(),"Wait for Refresh", Toast.LENGTH_SHORT).show();
                loadMenu();
                foodAdapter.notifyDataSetChanged();
                pullToRefresh.setRefreshing(false);
            }
        });



       // try {

        //}catch (Exception e){Toast.makeText(getContext(),"Failed to Fetch Menu : loadmenu",Toast.LENGTH_SHORT).show();}

    }

    public void loadMenu()

    {

        String path;

        //path = "https://generic-restaurant.000webhostapp.com/fetch_menu.php";
        path = getResources().getString(R.string.server_path)+"fetch_menu.php";

        //path = "192.168.43.230/restaurant/fetch_menu.php";
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
            stringRequest = new StringRequest(Request.Method.POST, path,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //converting the string to json array object
                                JSONArray array = new JSONArray(response);
                                menuCardArrayList = new ArrayList<>();
                                databaseHelper = new DatabaseHelper(getContext());
                                databaseHelper.getWritableDatabase().delete(DatabaseHelper.TABLE_MENU,null,null);
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject menu_json = array.getJSONObject(i);
                                    //adding the product to product list
                                    menuCardArrayList.add(new MenuCard(
                                            menu_json.getInt("ID_Menu"),
                                            menu_json.getString("Name_Menu"),
                                            String.valueOf(menu_json.getInt("Cost")),
                                            menu_json.getString("Type_Menu"),
                                            menu_json.getInt("Image_Menu")
                                    ));

                                    databaseHelper.insertMenuItem(databaseHelper.getWritableDatabase(),menuCardArrayList.get(i));
                                }

                                //creating adapter object and setting it to recyclerview
                                foodAdapter = new CustomAdapter(menuCardArrayList, getContext());
                                menuCardListView.setAdapter(foodAdapter);
                                progressBar.setVisibility(View.GONE);
                                status = 1;
                                Toast.makeText(getActivity(), "Data fetched from server", Toast.LENGTH_SHORT).show();
                                //recyclerView.setAdapter(adapter);
                            } catch (JSONException e) {
                                Toast.makeText(getContext(), "Error : " + e, Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            databaseHelper = new DatabaseHelper(getActivity());
                            menuCardArrayList = new ArrayList<MenuCard>();
                            cursor = databaseHelper.fetchMenuItems(databaseHelper.getWritableDatabase());
                            if(cursor.getCount() == 0)
                            {
                                Toast.makeText(getActivity(), "Database is empty", Toast.LENGTH_SHORT).show();
                            }
                            else
                                cursor.moveToFirst();
                            while (!cursor.isAfterLast()) {
                                String foodname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FOOD_NAME));
                                String foodcost = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FOOD_COST));
                                String foodtype = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FOOD_TYPE));
                                int img_type = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.FOOD_IMG));
                                MenuCard menuCard = new MenuCard(foodname, foodcost, foodtype, img_type);
                                menuCardArrayList.add(menuCard); //add the item
                                cursor.moveToNext();
                            }


                            if (getActivity()!=null){
                                Toast.makeText(getContext(), "Failed to Fetch Menu\nLoading Default", Toast.LENGTH_SHORT).show();
                                foodAdapter=new CustomAdapter(menuCardArrayList,getActivity());
                                menuCardListView.setAdapter(foodAdapter);
                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    });

            //try {
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //adding our stringrequest to queue
                Volley.newRequestQueue(getContext()).add(stringRequest);
            //}catch (Exception e) {
              //      Toast.makeText(getContext(), "Failed to Fetch Menu : Volley", Toast.LENGTH_SHORT).show();
                //}

    }

    @Override
    public void onPause() {
        super.onPause();
        if(stringRequest!=null)
            stringRequest.cancel();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putString("test","test");
        outState.putInt("status",status);
        super.onSaveInstanceState(outState);
    }
}