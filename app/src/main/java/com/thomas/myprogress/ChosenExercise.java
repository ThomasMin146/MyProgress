package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChosenExercise extends AppCompatActivity {
    private Button addSetButton;
    RecyclerView rvLayout;
    ArrayList<ExerciseModel> exerciseModels;
    private int setCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosen_exercise);

        String name = getIntent().getStringExtra("Name");
        TextView textView = findViewById(R.id.exerciseName);

        rvLayout = findViewById(R.id.rvLayout);
        setCounter = 1;

        addSetButton = findViewById(R.id.addSetButton);

        exerciseModels = new ArrayList<>();



        textView.setText(name);
        exerciseModels.add(new ExerciseModel(1));

        CustomAdapter adapter = new CustomAdapter(this, exerciseModels);
        rvLayout.setAdapter(adapter);
        rvLayout.setLayoutManager(new LinearLayoutManager(this));


        addSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCounter++;

                exerciseModels.add(new ExerciseModel(setCounter));
                adapter.notifyItemInserted(exerciseModels.size()-1);

            }
        });
    }

}