package com.example.genericrestaurant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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
    public ListView menulist;
    ArrayList<MenuCard> menu = new ArrayList<>();
    CustomAdapter foodAdapter;
    MenuCard item1;
    FloatingActionButton add_item_button;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //foodAdapter = new CustomAdapter(menu,getContext());
        item1 = new MenuCard("Chicken Burger", "Rs. 100", "Non-Veg.");
        menu.add(item1);

        return inflater.inflate(R.layout.fragment_menu, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        add_item_button = view.findViewById(R.id.add_item);
        menulist = view.findViewById(R.id.menu_list);
        loadMenu();
        menulist.setAdapter(foodAdapter);

    }

    private void loadMenu() {

        String path;

        path = "http://192.168.0.107/restaurant/fetch_menu.php";
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        StringRequest stringRequest = new StringRequest(Request.Method.POST, path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject menu_json = array.getJSONObject(i);

                                //adding the product to product list
                                menu.add(new MenuCard(
                                        menu_json.getString("Name_Menu"),
                                        String.valueOf("Rs. "+menu_json.getInt("Cost")),
                                        String.valueOf(menu_json.getInt("Type_Menu"))
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            foodAdapter = new CustomAdapter(menu,getContext());
                            menulist.setAdapter(foodAdapter);
                            //recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                                Toast.makeText(getContext(),"Error : "+e,Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Failed to Fetch Menu",Toast.LENGTH_SHORT).show();
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }



}
