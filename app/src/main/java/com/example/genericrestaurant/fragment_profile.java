package com.example.genericrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class fragment_profile extends Fragment {
    Button button;
    Button sign_out_button;
    TextView user_id_text;
    EditText name_text;
    EditText phone_text;
    EditText age_text;
    int user_id;
    String name;
    String phone;
    int age;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,null);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button = view.findViewById(R.id.button);
        sign_out_button = view.findViewById(R.id.button_sign_out);
        user_id_text = view.findViewById(R.id.user_id_text_profile);
        name_text = view.findViewById(R.id.name_edittext);
        phone_text = view.findViewById(R.id.phone_edittext);
        age_text = view.findViewById(R.id.age_edittext);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().id_user = 0;
                MainActivity.getInstance().name = null;
                MainActivity.getInstance().phone_no = null;
                MainActivity.getInstance().age = 0;

                user_id_text.setText("User ID : ");
                name_text.setText("");
                phone_text.setText("");
                age_text.setText("");
            }
        });

        user_id = MainActivity.getInstance().id_user;
        user_id_text.setText("User ID : " + user_id);

        name = MainActivity.getInstance().name;
        name_text.setText(name);

        phone = MainActivity.getInstance().phone_no;
        phone_text.setText(phone);

        age = MainActivity.getInstance().age;
        if(age>0)
            age_text.setText(String.valueOf(age) + " yrs.");

    }

    @Override
    public void onResume() {
        super.onResume();
        user_id = MainActivity.getInstance().id_user;
        user_id_text.setText("User ID : " + user_id);

        name = MainActivity.getInstance().name;
        name_text.setText(name);

        phone = MainActivity.getInstance().phone_no;
        phone_text.setText(phone);

        age = MainActivity.getInstance().age;
        if(age>0)
            age_text.setText(String.valueOf(age) + " yrs.");
    }
}
