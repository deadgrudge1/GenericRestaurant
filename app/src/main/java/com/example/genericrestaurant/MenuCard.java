package com.example.genericrestaurant;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.view.Menu;


@SuppressLint("ValidFragment")
public class MenuCard extends Fragment
{
    public int id;
    public String food_name, food_cost;
    public String food_type;
    public int img_type;


    public MenuCard(String food_name, String food_cost, String food_type, int img_type) {
        this.food_name = food_name;
        this.food_cost = food_cost;
        this.food_type = food_type;
        this.img_type=img_type;
    }

    public MenuCard(int id,String food_name, String food_cost, String food_type, int img_type) {
        this.id=id;
        this.food_name = food_name;
        this.food_cost = food_cost;
        this.food_type = food_type;
        this.img_type=img_type;
    }

    public int getID() { return  id; }

    public String getFoodName() {
        return food_name;
    }

    public String getFoodCost() {
        return food_cost;
    }

    public String getFoodType() {
        return food_type;
    }

    public int getImgType() {return img_type;}

}
