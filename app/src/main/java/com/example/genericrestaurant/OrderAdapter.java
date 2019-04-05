package com.example.genericrestaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter<OrderCard> {

    ArrayList<OrderCard> order_list;
    Context mcontext;

    private static class ViewHolder {
        TextView txtName;
        TextView txtQty;
        TextView txtPrice;
        TextView txtTotal;
        ImageView info;
    }

    public OrderAdapter(ArrayList<OrderCard> data, Context context) {
        super(context, R.layout.order_layout, data);
        this.order_list = data;
        this.mcontext=context;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final OrderCard orderCard = getItem(position);
        ViewHolder viewHolder;
        final View result;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.order_layout, parent, false);

        if(convertView == null){
            viewHolder = new ViewHolder();
            viewHolder.txtName = convertView.findViewById(R.id.food_name_order);
            viewHolder.txtQty = convertView.findViewById(R.id.food_quantity_order);
            viewHolder.txtPrice = convertView.findViewById(R.id.food_cost_order1);
            viewHolder.txtTotal = convertView.findViewById(R.id.food_total_order1);
            viewHolder.info = convertView.findViewById(R.id.image_item_order);

            result=convertView;

            convertView.setTag(viewHolder);

        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.txtName.setText(orderCard.getFoodName());
        viewHolder.txtQty.setText(orderCard.getQuantity());
        viewHolder.txtPrice.setText(orderCard.getFoodCost());
        if(orderCard.img_type==0) {
            viewHolder.info.setImageResource(R.drawable.vegetarian_food_symbol);
        }
        else if(orderCard.img_type==1) {
            viewHolder.info.setImageResource(R.drawable.non_vegetarian_food_symbol);
        }
        int price1 = 0,qty1 = 0;
        price1 = Integer.parseInt(orderCard.getFoodCost());
        qty1 = Integer.parseInt(orderCard.getQuantity());
        int total = price1*qty1;
        viewHolder.txtTotal.setText(total);



        return convertView;

    }

}
