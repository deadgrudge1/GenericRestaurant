package com.example.genericrestaurant;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CartAdapter extends RecyclerView.Adapter implements NumberPicker.OnValueChangeListener{
    private ArrayList<OrderCard> menuCardsdata;
    Context context;
    OnItemClickListener listener;
    NumberPicker.OnValueChangeListener changeListener;

    public interface OnItemClickListener {
        void showNumberPicker(View v, int id, int qty);
    }


    public CartAdapter(Context context, ArrayList<OrderCard> menuCardsdata, OnItemClickListener listener) {
        this.context = context;
        this.menuCardsdata=menuCardsdata;
        this.listener=listener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }




    public void onBindViewHolder(final RecyclerView.ViewHolder holder_view, final int position) {
        // set the data in items
        MyViewHolder holder=new MyViewHolder(holder_view.itemView);
        final OrderCard menuCard = menuCardsdata.get(position);
        holder.foodName.setText(menuCard.getFoodName());
        holder.foodType.setText(menuCard.food_type);
        holder.foodCost.setText("Rs. "+menuCard.food_cost);
        holder.quantity.setText("Qty : "+menuCard.quantity);

        holder.foodName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Name Selected", Toast.LENGTH_SHORT).show();
            }
        });

        holder.foodType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "FoodType Selected", Toast.LENGTH_SHORT).show();
            }
        });

        holder.foodCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Cost Selected", Toast.LENGTH_SHORT).show();
            }
        });

        holder.quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Quantity Selected", Toast.LENGTH_SHORT).show();
                listener.showNumberPicker(v,position,Integer.parseInt(menuCard.quantity));
                //show_dialog();
            }
        });


        // implement setOnClickListener event on item view.
        holder_view.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Toast.makeText(context, menuCard.food_name, Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return menuCardsdata.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView foodName;
        TextView foodType;
        TextView foodCost;
        TextView quantity;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            foodName = (TextView) itemView.findViewById(R.id.foodname);
            foodType = (TextView) itemView.findViewById(R.id.foodtype);
            foodCost = (TextView) itemView.findViewById(R.id.foodcost);
            quantity = (TextView) itemView.findViewById(R.id.edit_quantity);
        }
    }


    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        Toast.makeText(context,
                "selected number " + numberPicker.getValue(), Toast.LENGTH_SHORT).show();
    }


}