package com.thomas.myprogress;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    public CustomSpinnerAdapter(@NonNull Context context, int resource, String[] items) {
        super(context, resource, items);
    }


    // Other necessary constructors

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30); // Adjust the text size as needed
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30); // Adjust the text size as needed
        return view;
    }
}

