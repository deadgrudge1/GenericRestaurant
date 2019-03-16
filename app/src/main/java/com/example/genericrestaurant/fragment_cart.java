package com.example.genericrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class fragment_cart extends Fragment implements CartAdapter.OnItemClickListener,DialogClickListener{

    TextView total;
    Button order_place;
    ArrayList<OrderCard> menu;
    OrderCard item1;
    RecyclerView recyclerView;
    CartAdapter cartAdapter;
    private CartAdapter.OnItemClickListener listener;
    int amount_total;
    private NumberPicker.OnValueChangeListener valueChangeListener;
    int oldval,newval;
    public static final int REQ_CODE=1;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        //menulist = findViewById(R.id.menu_list);
        //menulist.setClickable(true);
        menu = new ArrayList<>();
        item1 = new OrderCard("Chicken Burger", "100", "Non-Veg.",1);
        menu.add(item1);
        item1 = new OrderCard("Veg Burger", "80", "Veg.",0,"2");
        menu.add(item1);
        return inflater.inflate(R.layout.activity_cart, null);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        total=(TextView)view.findViewById(R.id.amount_cart);
        order_place=(Button)view.findViewById(R.id.button_place_order);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_cart);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        listener=this;
        //valueChangeListener=this;
        cartAdapter = new CartAdapter(getContext(),menu,listener);
        recyclerView.setAdapter(cartAdapter); // set the Adapter to RecyclerView
        set_total();
    }

    @Override
    public void showNumberPicker(View v, int id, int qty) {
        DialogBox newFragment = new DialogBox();
        newFragment.set(id,qty,this.menu);
        newFragment.setTargetFragment(this,REQ_CODE);
        newFragment.show(getFragmentManager(), "time picker");
        menu=newFragment.get_menu();
        cartAdapter = new CartAdapter(getContext(),menu,listener);
        recyclerView.setAdapter(cartAdapter);
    }

    public void set_total()
    {
        amount_total=0;
        for(int i=0;i<menu.size();i++)
            amount_total=amount_total+(Integer.parseInt(menu.get(i).food_cost)*Integer.parseInt(menu.get(i).quantity));
        total.setText("Total : "+String.valueOf(amount_total));
    }

    public void doPositiveClick(int id,int val) {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
        Toast.makeText(getContext(),"ID : "+String.valueOf(id)+" Val : "+String.valueOf(val),Toast.LENGTH_SHORT).show();
        menu.get(id).quantity=String.valueOf(val);

        cartAdapter = new CartAdapter(getContext(),menu,listener);
        recyclerView.setAdapter(cartAdapter);
        set_total();
    }


    public void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");
    }





}
