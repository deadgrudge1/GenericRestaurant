package com.example.genericrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView mTextMessage;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    //private Fragment fragment_mic;

    public MainActivity getMainActivityInstance(){return this;}
    MainActivity mainActivity = getMainActivityInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
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

        Fragment fragment = null;
        switch (menuItem.getItemId())
        {
            case R.id.navigation_home:
                fragment = new fragment_menu();
                loadFragment(fragment,"Home");
                break;

            case R.id.navigation_Microphone:
                    fragment = new fragment_speak();
                    loadFragment(fragment, "Speak");
            break;
        }

        return false;
    }


}
