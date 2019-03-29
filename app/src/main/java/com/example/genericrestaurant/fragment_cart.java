package com.example.genericrestaurant;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class fragment_cart extends Fragment implements CartAdapter.OnItemClickListener, DialogClickListener {

    TextView total;
    Button order_place;
    Button cart_clear;
    ArrayList<OrderCard> menu;
    OrderCard item1;
    RecyclerView recyclerView;
    CartAdapter cartAdapter;
    private CartAdapter.OnItemClickListener listener;
    int amount_total;
    private NumberPicker.OnValueChangeListener valueChangeListener;
    int oldval, newval;
    public static final int REQ_CODE = 1;
    DatabaseHelper databaseHelper;
    Cursor cursor;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //menulist = findViewById(R.id.menu_list);
        //menulist.setClickable(true);
        menu = new ArrayList<>();
        item1 = new OrderCard("Chicken Burger", "100", "Non-Veg.", 1);
        //menu.add(item1);
        item1 = new OrderCard("Veg Burger", "80", "Veg.", 0, "2");
        //menu.add(item1);

        databaseHelper = new DatabaseHelper(getActivity());


        cursor = databaseHelper.fetchCartItems(databaseHelper.getWritableDatabase());
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "Cart Db is empty", Toast.LENGTH_SHORT).show();
        } else
            cursor.moveToFirst();

        OrderCard orderCard;
        while (!cursor.isAfterLast()) {
            int food_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.FOOD_ID));
            int quantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUANTITY));

            Cursor temp = databaseHelper.fetchMenuItem(databaseHelper.getReadableDatabase(), food_id);
            if (temp.getCount() == 0) {
                Toast.makeText(getActivity(), "Failed to map Cart Item", Toast.LENGTH_SHORT).show();
            } else
                temp.moveToFirst();

            String foodname = temp.getString(temp.getColumnIndex(DatabaseHelper.FOOD_NAME));
            String foodcost = temp.getString(temp.getColumnIndex(DatabaseHelper.FOOD_COST));
            String foodtype = temp.getString(temp.getColumnIndex(DatabaseHelper.FOOD_TYPE));
            int img_type = temp.getInt(temp.getColumnIndex(DatabaseHelper.FOOD_IMG));

            orderCard = new OrderCard(foodname, foodcost, foodtype, img_type, String.valueOf(quantity));

            menu.add(orderCard);

            cursor.moveToNext();
        }

        /*cart_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseHelper.emptyCart(databaseHelper.getWritableDatabase());

            }
        });*/

        return inflater.inflate(R.layout.activity_cart, null);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        total = view.findViewById(R.id.amount_cart);
        order_place = view.findViewById(R.id.button_place_order);
        cart_clear = view.findViewById(R.id.clear_cart);

        recyclerView = view.findViewById(R.id.recycler_cart);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        listener = this;
        //valueChangeListener=this;
        cartAdapter = new CartAdapter(getContext(), menu, listener);
        recyclerView.setAdapter(cartAdapter); // set the Adapter to RecyclerView
        set_total();

        cart_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu.isEmpty()) {
                    Toast.makeText(getActivity(), "Cart already Empty", Toast.LENGTH_SHORT).show();
                } else
                    cart_empty();
            }
        });

        order_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu.isEmpty())
                    Toast.makeText(getActivity(), "Add atleast 1 item to place order", Toast.LENGTH_SHORT).show();
                else
                    place_order();

            }
        });

    }


    @Override
    public void showNumberPicker(View v, int id, int qty) {
        DialogBox newFragment = new DialogBox();
        newFragment.set(id, qty, this.menu);
        newFragment.setTargetFragment(this, REQ_CODE);
        newFragment.show(getFragmentManager(), "time picker");
        menu = newFragment.get_menu();
        cartAdapter = new CartAdapter(getContext(), menu, listener);
        recyclerView.setAdapter(cartAdapter);
    }

    public void set_total() {
        amount_total = 0;
        for (int i = 0; i < menu.size(); i++)
            amount_total = amount_total + (Integer.parseInt(menu.get(i).food_cost) * Integer.parseInt(menu.get(i).quantity));
        total.setText("Total : " + String.valueOf(amount_total));
    }

    public void doPositiveClick(int id, int val) {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
        Toast.makeText(getContext(), "ID : " + String.valueOf(id) + " Val : " + String.valueOf(val), Toast.LENGTH_SHORT).show();
        menu.get(id).quantity = String.valueOf(val);


        Cursor cursor_temp = databaseHelper.fetchCartItems(databaseHelper.getReadableDatabase());
        cursor_temp.moveToFirst();
        int count = id;
        while (count > 0) {
            cursor_temp.moveToNext();
            count--;
        }
        int food_id = cursor_temp.getInt(cursor_temp.getColumnIndex(DatabaseHelper.FOOD_ID));
        ;

        databaseHelper.updateCartItem(databaseHelper.getWritableDatabase(), food_id, val);

        cartAdapter = new CartAdapter(getContext(), menu, listener);
        recyclerView.setAdapter(cartAdapter);
        set_total();
    }


    public void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");
    }

    public void removeItemFromCart(int id) {

        Cursor cursor_temp = databaseHelper.fetchCartItems(databaseHelper.getReadableDatabase());
        cursor_temp.moveToFirst();
        int count = id;
        int food_id = 0;
        while (count >= 0) {
            food_id = cursor_temp.getInt(cursor_temp.getColumnIndex(DatabaseHelper.FOOD_ID));
            cursor_temp.moveToNext();
            count--;
        }

        databaseHelper.removeCartItem(databaseHelper.getWritableDatabase(), food_id);
        menu.remove(id);
        cartAdapter = new CartAdapter(getContext(), menu, listener);
        recyclerView.setAdapter(cartAdapter);
        set_total();

    }

    public void cart_empty() {

        databaseHelper.emptyCart(databaseHelper.getWritableDatabase());
        menu.clear();
        cartAdapter = new CartAdapter(getContext(), menu, listener);
        recyclerView.setAdapter(cartAdapter);
        cart_clear.setVisibility(View.GONE);
        total.setText("Cart is empty, add items from menu to place order.");

        Toast.makeText(getActivity(), "Cart is Empty", Toast.LENGTH_SHORT).show();
    }

    private void place_order() {
        Intent intent = new Intent(getContext(), Place_Order.class);

        Bundle bundle = new Bundle();
        bundle.putInt("total", amount_total);
        bundle.putSerializable("items", menu);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
        cart_empty();
    }


}