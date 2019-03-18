package com.example.genericrestaurant;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class fragment_speak extends Fragment {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    public ListView order_list;
    ArrayList<MenuCard> order = new ArrayList<>();
    DatabaseHelper databaseHelper;
    CustomAdapter orderAdapter ;
    MenuCard order1;
    TextView total_amount;
    Cursor cursor;
    FloatingActionButton mic_float_button;
    ArrayList<MenuCard> mArrayList = new ArrayList<>();


    public void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(),
                    getString(R.string.speech_not_supported),Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //order1 = new MenuCard("Chicken Burger", "Rs. 100", "Non-Veg.");
        //order.add(order1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        databaseHelper = new DatabaseHelper(getActivity());

        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        cursor = databaseHelper.fetchMenuItems(databaseHelper.getWritableDatabase());
        if(cursor.getCount() == 0)
        {
            Toast.makeText(getActivity(), "Database is empty", Toast.LENGTH_SHORT).show();
        }
        else
            cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String foodname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FOOD_NAME));
            String foodcost = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FOOD_COST));
            String foodtype = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FOOD_TYPE));
            MenuCard menuCard = new MenuCard(foodname, foodcost, foodtype, 0);
            mArrayList.add(menuCard); //add the item
            cursor.moveToNext();
        }


       return inflater.inflate(R.layout.fragment_mic,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mic_float_button = view.findViewById(R.id.mic_float_button);
        mic_float_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });
        orderAdapter = new CustomAdapter(mArrayList,getContext());
        order_list = view.findViewById(R.id.orderlist);
        order_list.setAdapter(orderAdapter);



    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String string=result.get(0);
                    //order.add(new MenuCard(string,"Rs. 50","Veg",0));
                    orderAdapter=new CustomAdapter(order,getContext());
                    order_list.setAdapter(orderAdapter);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order_speak_fragment",order);

                }
                break;
            }

        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(ArrayList<MenuCard> order);
    }

}


