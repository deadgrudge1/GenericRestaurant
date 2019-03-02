package com.example.genericrestaurant;

import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MenuCard extends Fragment
{
    public String food_name, food_cost;
    public String food_type;



    public MenuCard(String food_name, String food_cost, String food_type) {
        this.food_name = food_name;
        this.food_cost = food_cost;
        this.food_type = food_type;
    }

    public String getFoodName() {
        return food_name;
    }

    public String getFoodCost() {
        return food_cost;
    }

    public String getFoodType() {
        return food_type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, null);
    }
}
