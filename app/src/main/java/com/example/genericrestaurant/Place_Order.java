package com.example.genericrestaurant;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Place_Order extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {

    ArrayList<OrderCard> order;
    int total;
    TextView total_text;
    //TextView order_text;
    OrderCard orderCard;
    ImageView paytm;
    TextView cashcard;
    Button back;
    Spinner spin;
    ArrayAdapter aa;
    String[] payment_options = {"Cash", "Card","UPI", "Paytm"};
    ListView orderList;
    OrderAdapter orderAdapter;
    DatabaseHelper databaseHelper;
    Cursor cursor;
    int table_id=0;
    TextView table_id_text;
    int order_id;
    TextView order_id_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place__order);



        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        aa = new ArrayAdapter(this,R.layout.layout_spinner_item,payment_options);
        aa.setDropDownViewResource(R.layout.layout_spinner_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        order = (ArrayList<OrderCard>) bundle.getSerializable("items");
        total = bundle.getInt("total");
        table_id = bundle.getInt("table_id");
        order_id =bundle.getInt("order_id");


        total_text = findViewById(R.id.place_order_total);
        //order_text = findViewById((R.id.order_items_text));
        back = findViewById(R.id.button_place_order_back);
        table_id_text = findViewById(R.id.table_id);
        order_id_text = findViewById(R.id.order_id);

        ArrayList<OrderCard> order1 = new ArrayList<>();

        databaseHelper = new DatabaseHelper(this);


        cursor = databaseHelper.fetchCartItems(databaseHelper.getWritableDatabase());
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Cart Db is empty", Toast.LENGTH_SHORT).show();
        } else
            cursor.moveToFirst();

        OrderCard orderCard;
        while (!cursor.isAfterLast()) {
            int food_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.FOOD_ID));
            int quantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUANTITY));

            Cursor temp = databaseHelper.fetchMenuItem(databaseHelper.getReadableDatabase(), food_id);
            if (temp.getCount() == 0) {
                Toast.makeText(this, "Failed to map Cart Item", Toast.LENGTH_SHORT).show();
            } else
                temp.moveToFirst();

            String foodname = temp.getString(temp.getColumnIndex(DatabaseHelper.FOOD_NAME));
            String foodcost = temp.getString(temp.getColumnIndex(DatabaseHelper.FOOD_COST));
            String foodtype = temp.getString(temp.getColumnIndex(DatabaseHelper.FOOD_TYPE));
            int img_type = temp.getInt(temp.getColumnIndex(DatabaseHelper.FOOD_IMG));

            orderCard = new OrderCard(foodname, foodcost, foodtype, img_type, String.valueOf(quantity));

            order1.add(orderCard);

            cursor.moveToNext();
        }


        //OrderCard orderCard1 = new OrderCard("Test","50","Veg",1,"2");

        orderList = findViewById(R.id.list_order);
        orderAdapter = new OrderAdapter(order1,this);
        orderList.setAdapter(orderAdapter);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //order_text.setText(null);
        total_text.setText("Total : Rs. " + String.valueOf(total));
        table_id_text.setText("Table ID : " + String.valueOf(table_id));
        order_id_text.setText("Order ID : " + String.valueOf(order_id));
        paytm = findViewById(R.id.qrcode);
        cashcard = findViewById(R.id.cashcard);
        cashcard.setVisibility(View.GONE);
        paytm.setVisibility(View.GONE);


        for(int i=0; i<order.size(); i++)
        {
            orderCard = order.get(i);
            int item_total = Integer.parseInt(orderCard.food_cost) * Integer.parseInt(orderCard.quantity);
            //order_text.append(orderCard.food_name + " x " + orderCard.quantity + " : Rs. " + item_total + "\n");
        }

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(i) {
            case 3:
                paytm.setImageResource(R.drawable.paytmqr);
                paytm.setVisibility(View.VISIBLE);
                cashcard.setVisibility(View.GONE);

            break;
            case 0:
                cashcard.setText("Please pay cash at the counter");
            paytm.setVisibility(View.GONE);
            cashcard.setVisibility(View.VISIBLE);

            break;

            case 2:
                paytm.setImageResource(R.drawable.bhimqr);
                paytm.setVisibility(View.VISIBLE);

                cashcard.setVisibility(View.GONE);
                break;
            case 1:
                paytm.setVisibility(View.GONE);
                cashcard.setText("Redirecting you to Visa\n payment gateway...");
                cashcard.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
