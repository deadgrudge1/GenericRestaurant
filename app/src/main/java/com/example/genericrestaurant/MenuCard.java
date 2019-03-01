package com.example.genericrestaurant;

import android.graphics.drawable.Icon;

public class MenuCard
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
}
