package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.thomas.myprogress.dbhelper.DataBaseHelper;

import java.util.ArrayList;

public class StartWorkoutPage extends AppCompatActivity {
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_workout_page);
        ArrayList<String> list = new ArrayList<>();
        list.add("asd");
        list.add("dsa");
        dbHelper = new DataBaseHelper(this);

        Spinner exerciseSpinner = findViewById(R.id.exerciseSpinner);
        ArrayList<String> henlo = dbHelper.allExercisesNames();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, henlo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(adapter);

    }
}