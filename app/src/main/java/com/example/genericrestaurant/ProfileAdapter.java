package com.example.genericrestaurant;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileAdapter extends RecyclerView.Adapter{

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // infalte the item Layout
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        ProfileAdapter.MyViewHolder vh = new ProfileAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        // set the data in items
        ProfileAdapter.MyViewHolder holder=new ProfileAdapter.MyViewHolder(viewHolder.itemView);

        holder.Name.setText("Amit");
        holder.Contact.setText("8208582466");
        holder.Address.setText("Baner, Pune");
        holder.Age.setText("22");
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //LINK FROM HERE - EDIT TEXT

        // init the item view's
        EditText Name;
        EditText Contact;
        EditText Address;
        EditText Age;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            //Name = (EditText) itemView.findViewById(R.id.foodname);
            //Contact = (EditText) itemView.findViewById(R.id.foodtype);
            //Address = (EditText) itemView.findViewById(R.id.foodcost);
            //Age = (EditText) itemView.findViewById(R.id.edit_quantity);
        }
    }
}
