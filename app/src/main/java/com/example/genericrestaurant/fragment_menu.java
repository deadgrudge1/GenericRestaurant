package com.example.genericrestaurant;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
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
    public ListView menuCardListView;
    ArrayList<MenuCard> menuCardArrayList = new ArrayList<>();
    CustomAdapter foodAdapter;
    MenuCard item1;
    FloatingActionButton add_item_button;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //foodAdapter = new CustomAdapter(menu,getContext());


        return inflater.inflate(R.layout.fragment_menu, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        add_item_button = view.findViewById(R.id.add_item);
        menuCardListView = view.findViewById(R.id.menu_list);
        try {
            loadMenu();
        }catch (Exception e){

        }

       // try {

        //}catch (Exception e){Toast.makeText(getContext(),"Failed to Fetch Menu : loadmenu",Toast.LENGTH_SHORT).show();}

    }

    private void loadMenu()

    {

        String path;

        path = "http://192.168.0.108/restaurant/fetch_menu.php";
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
                                menuCardArrayList = new ArrayList<>();
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject menu_json = array.getJSONObject(i);
                                    //adding the product to product list
                                    menuCardArrayList.add(new MenuCard(
                                            menu_json.getString("Name_Menu"),
                                            String.valueOf("Rs. " + menu_json.getInt("Cost")),
                                            menu_json.getString("Type_Menu"),
                                            menu_json.getInt("Image_Menu")
                                    ));
                                }

                                //creating adapter object and setting it to recyclerview
                                foodAdapter = new CustomAdapter(menuCardArrayList, getContext());
                                menuCardListView.setAdapter(foodAdapter);
                                //recyclerView.setAdapter(adapter);
                            } catch (JSONException e) {
                                Toast.makeText(getContext(), "Error : " + e, Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            item1 = new MenuCard("Veg Burger", "Rs. 70", "Veg.",0);
                            menuCardArrayList.add(item1);
                            item1 = new MenuCard("Chicken Burger", "Rs. 80", "Non-Veg.",1);
                            menuCardArrayList.add(item1);
                            item1 = new MenuCard("Fries", "Rs. 50", "Veg.",0);
                            menuCardArrayList.add(item1);
                            item1 = new MenuCard("Coke", "Rs. 40", "Veg.",0);
                            menuCardArrayList.add(item1);
                            if (getActivity()!=null){
                                Toast.makeText(getContext(), "Failed to Fetch Menu\nLoading Default", Toast.LENGTH_SHORT).show();
                                foodAdapter=new CustomAdapter(menuCardArrayList,getActivity());
                                menuCardListView.setAdapter(foodAdapter);
                            }

                        }
                    });

            //try {
                //adding our stringrequest to queue
                Volley.newRequestQueue(getContext()).add(stringRequest);
            //}catch (Exception e) {
              //      Toast.makeText(getContext(), "Failed to Fetch Menu : Volley", Toast.LENGTH_SHORT).show();
                //}

    }




}
