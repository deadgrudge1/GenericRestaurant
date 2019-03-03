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


        foodAdapter = new CustomAdapter(menu,getContext());
        item1 = new MenuCard("Chicken Burger", "Rs. 100", "Non-Veg.");
        menu.add(item1);
        return inflater.inflate(R.layout.fragment_menu, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        add_item_button = view.findViewById(R.id.add_item);
        menulist = view.findViewById(R.id.menu_list);
        menulist.setAdapter(foodAdapter);

    }



}
