package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.thomas.myprogress.dbhelper.DataBaseHelper;

import java.util.ArrayList;

public class CreateExercise extends AppCompatActivity {
    EditText nameOfNewExercise;
    Button createExerciseButton;

    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_exercise);


        nameOfNewExercise = findViewById(R.id.nameOfNewExercise);
        createExerciseButton = findViewById(R.id.createExerciseButton);

        createExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.newExercise("Exercise", "Hello");
                Intent intent = new Intent(CreateExercise.this, AddExercise.class);
                startActivity(intent);
            }
        });

    }
}