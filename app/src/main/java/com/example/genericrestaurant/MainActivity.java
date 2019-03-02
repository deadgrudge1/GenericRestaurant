package com.example.genericrestaurant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView mTextMessage;
    public ListView menulist;
    ArrayList<MenuCard> menu = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);


        loadFragment(new fragment_menu());

        MenuCard item1 = new MenuCard("Chicken Burger","Rs. 100", "Non-Veg.");
        menu.add(0,item1);

    }


    private boolean loadFragment(Fragment fragment)
    {

        if(fragment != null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
            .commit();
            return true;
        }
        else
            return false;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;
        switch (menuItem.getItemId())
        {
            case R.id.navigation_home:
                fragment = new fragment_menu();
                menulist = findViewById(R.id.menu_list);


                CustomAdapter foodAdapter = new CustomAdapter(menu,this);

                menulist.setAdapter(foodAdapter);
                break;

        }

        return false;
    }
}
