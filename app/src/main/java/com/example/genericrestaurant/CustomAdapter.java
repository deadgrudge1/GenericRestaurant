package com.example.genericrestaurant;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<MenuCard>{

    private ArrayList<MenuCard> menuCardsdata;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView foodName;
        TextView foodType;
        TextView foodCost;

    }

    public CustomAdapter(ArrayList<MenuCard> data, Context context) {
        super(context, R.layout.food_item, data);
        this.menuCardsdata = data;
        this.mContext=context;

    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MenuCard menuCard = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.food_item, parent, false);
            viewHolder.foodName = (TextView) convertView.findViewById(R.id.foodname);
            viewHolder.foodType = (TextView) convertView.findViewById(R.id.foodtype);
            viewHolder.foodCost = (TextView) convertView.findViewById(R.id.foodcost);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        viewHolder.foodName.setText(menuCard.getFoodName());
        viewHolder.foodType.setText(menuCard.getFoodType());
        viewHolder.foodCost.setText(menuCard.getFoodCost());

        // Return the completed view to render on screen
        return convertView;
    }
}

