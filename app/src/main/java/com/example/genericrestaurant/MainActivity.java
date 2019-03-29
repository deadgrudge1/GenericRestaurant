package com.example.genericrestaurant;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

//import ai.api.AIConfiguration;
//import ai.api.AIListener;
//import ai.api.model.AIError;
//import ai.api.model.AIResponse;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        fragment_speak.OnFragmentInteractionListener {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    ArrayList<MenuCard> order = new ArrayList<>();
    Fragment fragment = null;
    boolean doubleBackToExitPressedOnce = false;
    public MainActivity getMainActivityInstance(){return this;}
    MainActivity mainActivity = getMainActivityInstance();
    Fragment fragment_menu = new fragment_menu();
    Fragment fragment_cart = new fragment_cart();
    Fragment fragment_speak = new fragment_speak();
    fragment_menu fragmentMenu = new fragment_menu();
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    SwipeAdapter swipeAdapter;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(swipeAdapter);
        navigation.setOnNavigationItemSelectedListener(this);
        order.add(new MenuCard("Veg Burger","Rs. 70","Veg",0));
        //fragment_mic = new fragment_speak();
        loadFragment(fragment_menu,"Menu");







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
                    //fragment = new fragment_menu();
                    loadFragment(fragment_menu, "Menu");
                break;

            case R.id.navigation_Microphone:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order_speak", order);
                    fragment = new fragment_speak();
                    fragment.setArguments(bundle);
                    loadFragment(fragment_speak, "Speak");
            break;

            case R.id.navigation_dashboard:
                fragment = new fragment_cart();
                loadFragment(fragment_cart, "Cart");
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

    @Override
    protected void onPause() {
        super.onPause();
    }

   /* @Override
    public void onResult(AIResponse result) {

    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }*/
}
