package com.thomas.myprogress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thomas.myprogress.dbhelper.DataBaseHelper;

import java.util.ArrayList;

public class StartWorkoutPage extends AppCompatActivity {
    DataBaseHelper dbHelper;
    Spinner exerciseSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_workout_page);

        dbHelper = new DataBaseHelper(this);

        exerciseSpinner = findViewById(R.id.exerciseSpinner);
        ArrayList<String> henlo = dbHelper.allExercisesNames();
        henlo.add(0,"Choose an exercise...");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, henlo) {
            @Override
            public boolean isEnabled(int position){
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,@NonNull ViewGroup parent) {
                // Get the item view
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    textView.setTextColor(Color.GRAY);
                }
                else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Spinner on item selected listener
        exerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {

                // Get the spinner selected item text
                String selectedItemText = (String) parent.getItemAtPosition(position);

                // If user change the default selection
                // First item is disable and
                // it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText(getApplicationContext(),"Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        exerciseSpinner.setAdapter(adapter);

    }
}