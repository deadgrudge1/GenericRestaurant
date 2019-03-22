package com.example.genericrestaurant;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;

import java.io.Serializable;


@SuppressLint("ValidFragment")
public class OrderCard extends Fragment implements Serializable
{
    public String food_name, food_cost;
    public String food_type, quantity;
    public int img_type;

    public OrderCard(String food_name, String food_cost, String food_type, int img_type, String quantity) {
        this.food_name = food_name;
        this.food_cost = food_cost;
        this.food_type = food_type;
        this.img_type=img_type;
        this.quantity = quantity;
    }

    public OrderCard(String food_name, String food_cost, String food_type, int img_type) {
        this.food_name = food_name;
        this.food_cost = food_cost;
        this.food_type = food_type;
        this.img_type = img_type;
        quantity="1";
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

    public int getImgType(){return img_type;}

    public String getQuantity() {
        return quantity;
    }

}