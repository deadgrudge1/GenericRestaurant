package com.example.genericrestaurant;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Place_Order extends AppCompatActivity {

    ArrayList<OrderCard> order;
    int total;
    TextView total_text;
    TextView order_text;
    OrderCard orderCard;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place__order);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        for(int i=0; i<order.size(); i++)
        {
            orderCard = order.get(i);
            int item_total = Integer.parseInt(orderCard.food_cost) * Integer.parseInt(orderCard.quantity);
            order_text.append(orderCard.food_name + " x " + orderCard.quantity + " : Rs. " + item_total + "\n");
        }

    }
}
