package com.thomas.myprogress;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.thomas.myprogress.dbhelper.DataBaseHelper;

public class UpdateExercise extends AppCompatActivity {
    TextView exercise;
    EditText nameOfNewExercise;
    Button createExerciseButton, updateExerciseButton;
    Spinner bodypart, difficulty;
    DataBaseHelper dbHelper;
    String selectedBodypartOption;
    String selectedDifficultyOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_exercise);

        dbHelper = new DataBaseHelper(this);

        exercise = findViewById(R.id.exercise);
        nameOfNewExercise = findViewById(R.id.nameOfNewExercise);
        createExerciseButton = findViewById(R.id.createExerciseButton);
        updateExerciseButton = findViewById(R.id.updateExerciseButton);
        bodypart = findViewById(R.id.bodyPartOfNewExercise);
        difficulty = findViewById(R.id.difficultyOfNewExercise);

        createExerciseButton.setVisibility(View.GONE);
        updateExerciseButton.setVisibility(View.VISIBLE);

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

        exercise.setText(getIntent().getStringExtra("ExerciseName"));
        nameOfNewExercise.setText(getIntent().getStringExtra("ExerciseName"));

        ArrayAdapter<String> bodypartAA = (ArrayAdapter<String>) bodypart.getAdapter();
        if (bodypartAA != null) {
            int position = bodypartAA.getPosition(getIntent().getStringExtra("Bodypart"));
            if (position != Spinner.INVALID_POSITION) {
                bodypart.setSelection(position);
            }
        }

        ArrayAdapter<String> difficultyAA = (ArrayAdapter<String>) difficulty.getAdapter();
        if (difficultyAA != null) {
            int position = difficultyAA.getPosition(getIntent().getStringExtra("Difficulty"));
            if (position != Spinner.INVALID_POSITION) {
                difficulty.setSelection(position);
            }
        }


        updateExerciseButton.setOnClickListener(v -> {
            //dbHelper.createExercise(nameOfNewExercise.getText().toString(), selectedDifficultyOption, selectedBodypartOption);

            Intent intent = new Intent(UpdateExercise.this, AddExercise.class);
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
