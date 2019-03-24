package com.example.genericrestaurant;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Place_Order extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {

    ArrayList<OrderCard> order;
    int total;
    TextView total_text;
    TextView order_text;
    OrderCard orderCard;
    ImageView paytm;
    TextView cashcard;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place__order);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        String[] payment_options = {"Cash", "Card","UPI", "Paytm"};

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,payment_options);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        order = (ArrayList<OrderCard>) bundle.getSerializable("items");
        total = bundle.getInt("total");


        total_text = (TextView)findViewById(R.id.place_order_total);
        order_text = (TextView)findViewById((R.id.order_items_text));
        back = (Button)findViewById(R.id.button_place_order_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        order_text.setText(null);
        total_text.setText("Total : Rs. " + String.valueOf(total));
        paytm = findViewById(R.id.qrcode);
        cashcard = findViewById(R.id.cashcard);
        cashcard.setVisibility(View.GONE);
        paytm.setVisibility(View.GONE);


        for(int i=0; i<order.size(); i++)
        {
            orderCard = order.get(i);
            int item_total = Integer.parseInt(orderCard.food_cost) * Integer.parseInt(orderCard.quantity);
            order_text.append(orderCard.food_name + " x " + orderCard.quantity + " : Rs. " + item_total + "\n");
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
                cashcard.setText("Pleas pay cash at the counter");
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
