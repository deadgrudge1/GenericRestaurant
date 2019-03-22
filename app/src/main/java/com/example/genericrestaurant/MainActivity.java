package com.example.genericrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,fragment_speak.OnFragmentInteractionListener {

    private TextView mTextMessage;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    ArrayList<MenuCard> order = new ArrayList<>();
    //private Fragment fragment_mic;
    Fragment fragment = null;
    boolean doubleBackToExitPressedOnce = false;

    public MainActivity getMainActivityInstance(){return this;}
    MainActivity mainActivity = getMainActivityInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        order.add(new MenuCard("Veg Burger","Rs. 70","Veg",0));
        //fragment_mic = new fragment_speak();



        loadFragment(new fragment_menu(),"Menu");
    }

    private boolean loadFragment(Fragment fragment, String fragment_tag)
    {

        if(fragment != null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment,fragment_tag)
            .commit();
            return true;
        }
        else
            return false;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        switch (menuItem.getItemId())
        {
            case R.id.navigation_home:
                    fragment = new fragment_menu();
                    loadFragment(fragment, "Home");


                break;

            case R.id.navigation_Microphone:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order_speak", order);
                    fragment = new fragment_speak();
                    fragment.setArguments(bundle);
                    loadFragment(fragment, "Speak");
            break;

            case R.id.navigation_dashboard:
                fragment = new fragment_cart();
                loadFragment(fragment, "Cart");
                break;
        }

        return false;
    }

    @Override
    public void onFragmentInteraction(ArrayList<MenuCard> order) {
        this.order=order;
        Toast.makeText(getApplicationContext(),"Got it",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
