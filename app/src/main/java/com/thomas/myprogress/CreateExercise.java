package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.thomas.myprogress.adapters.CustomSpinnerAdapter;


public class CreateExercise extends AppCompatActivity {
    EditText nameOfNewExercise;
    Button createExerciseButton;
    Spinner bodypart, difficulty;
    DataBaseHelper dbHelper;
    String selectedBodypartOption;
    String selectedDifficultyOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_exercise);

        dbHelper = new DataBaseHelper(this);

        nameOfNewExercise = findViewById(R.id.nameOfNewExercise);
        bodypart = findViewById(R.id.bodyPartOfNewExercise);
        difficulty = findViewById(R.id.difficultyOfNewExercise);

        createExerciseButton = findViewById(R.id.createExerciseButton);

        String[] bodypartOptions = {"Select a bodypart", "LEGS", "ABS", "CORE", "TRICEPS", "BICEPS", "CHEST", "BACK", "SHOULDERS"};
        String[] difficultyOptions = {"Select a difficulty", "BEGINNER", "INTERMEDIATE", "ADVANCED"};

        CustomSpinnerAdapter bodypartAdapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, bodypartOptions);
        CustomSpinnerAdapter difficultyAdapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, difficultyOptions);

        bodypartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bodypart.setAdapter(bodypartAdapter);
        difficulty.setAdapter(difficultyAdapter);

        bodypart.setSelection(0, false);
        difficulty.setSelection(0, false);

        createExerciseButton.setOnClickListener(v -> {
            long exerciseId = dbHelper.addExercise(nameOfNewExercise.getText().toString(), selectedBodypartOption, selectedDifficultyOption);
            Intent intent = new Intent(CreateExercise.this, AddExercise.class);
            intent.putExtra("exerciseId", exerciseId);
            startActivity(intent);
        });

        bodypart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle the selected item
                if (position != 0) {
                    // Update the selectedOption variable with the selected string
                    selectedBodypartOption = bodypartOptions[position];
                } else {
                    // Handle the case when the initial selection is made
                    selectedBodypartOption = null;
                }
                // Do something with the selected option
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
                selectedBodypartOption = null;
            }
        });

        difficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle the selected item
                if (position != 0) {
                    // Update the selectedOption variable with the selected string
                    selectedDifficultyOption = difficultyOptions[position];
                } else {
                    // Handle the case when the initial selection is made
                    selectedDifficultyOption = null;
                }
                // Do something with the selected option
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
                selectedDifficultyOption = null;
            }
        });

    }
}