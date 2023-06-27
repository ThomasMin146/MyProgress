package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.thomas.myprogress.dbhelper.DataBaseHelper;

import java.util.ArrayList;

public class CreateExercise extends AppCompatActivity {
    EditText nameOfNewExercise, bodypartOfNewExercise, difficultyOfNewExercise;
    Button createExerciseButton;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_exercise);

        dbHelper = new DataBaseHelper(this);

        nameOfNewExercise = findViewById(R.id.nameOfNewExercise);
        bodypartOfNewExercise = findViewById(R.id.bodyPartOfNewExercise);
        difficultyOfNewExercise = findViewById(R.id.difficultyOfNewExercise);

        createExerciseButton = findViewById(R.id.createExerciseButton);

        createExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.createExercise("Exercise", nameOfNewExercise.getText().toString(),
                        difficultyOfNewExercise.getText().toString(), bodypartOfNewExercise.getText().toString());

                Intent intent = new Intent(CreateExercise.this, AddExercise.class);
                startActivity(intent);
            }
        });

    }
}