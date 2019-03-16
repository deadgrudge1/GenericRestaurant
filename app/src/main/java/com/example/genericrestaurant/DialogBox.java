package com.example.genericrestaurant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.ArrayList;

public class DialogBox extends DialogFragment {
    private NumberPicker.OnValueChangeListener valueChangeListener;
    private int qty;
    private int id;
    private ArrayList<OrderCard> menu;
    private DialogClickListener dialogClickListener;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        dialogClickListener=(DialogClickListener)getTargetFragment();
        final NumberPicker numberPicker = new NumberPicker(getActivity());

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(60);
        numberPicker.setValue(qty);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Value");
        builder.setMessage("Choose a number :");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //valueChangeListener.onValueChange(numberPicker,
                   //     numberPicker.getValue(), numberPicker.getValue());
                Toast.makeText(getContext(),String.valueOf(numberPicker.getValue()),Toast.LENGTH_SHORT).show();
                //((MainActivity)getActivity()).doPositiveClick(id,numberPicker.getValue());
                qty=numberPicker.getValue();
                dialogClickListener.doPositiveClick(id,qty);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //valueChangeListener.onValueChange(numberPicker,
                      //  numberPicker.getValue(), numberPicker.getValue());
                dismiss();
            }
        });

        builder.setView(numberPicker);
        return builder.create();
    }

    public NumberPicker.OnValueChangeListener getValueChangeListener() {
        return valueChangeListener;
    }

    public void set(int id, int qty, ArrayList<OrderCard> menu)
    {
        this.id=id;
        this.qty=qty;
        this.menu=menu;
    }

    public ArrayList<OrderCard> get_menu()
    {
        return menu;
    }

    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }

}
