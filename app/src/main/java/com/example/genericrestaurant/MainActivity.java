package com.example.genericrestaurant;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

//import ai.api.AIConfiguration;
//import ai.api.AIListener;
//import ai.api.model.AIError;
//import ai.api.model.AIResponse;

public class MainActivity extends AppCompatActivity implements
        fragment_speak.OnFragmentInteractionListener {

    BottomNavigationView navigation;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    ArrayList<MenuCard> order = new ArrayList<>();
    Fragment fragment = null;
    boolean doubleBackToExitPressedOnce = false;
    Fragment fragment_menu = new fragment_menu();
    Fragment fragment_cart = new fragment_cart();
    Fragment fragment_speak = new fragment_speak();
    fragment_menu fragmentMenu = new fragment_menu();
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager(),3);
    private SpeechRecognizerManager mSpeechRecognizerManager;
    private static MainActivity instance;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mSpeechRecognizerManager=new SpeechRecognizerManager(this);
        navigation =  findViewById(R.id.navigation);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(swipeAdapter);
        viewPager.setVisibility(View.GONE);
        order.add(new MenuCard("Veg Burger","Rs. 70","Veg",0));
        //fragment_mic = new fragment_speak();
        loadFragment(fragment_menu,"Menu");
        instance = this;


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                switch (menuItem.getItemId())
                {
                    case R.id.navigation_home:
                        //fragment = new fragment_menu();
                        loadFragment(fragment_menu, "Menu");
                        return true;

                    case R.id.navigation_Microphone:
                        //Bundle bundle = new Bundle();
                        //bundle.putSerializable("order_speak", order);
                        //fragment = new fragment_speak();
                        //fragment.setArguments(bundle);
                        loadFragment(fragment_speak, "Speak");
                        return true;

                    case R.id.navigation_dashboard:
                        fragment = new fragment_cart();
                        loadFragment(fragment_cart, "Cart");
                        return true;
                }

                return false;
            }
        });
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    public static MainActivity getInstance() {
        return instance;
    }

}
