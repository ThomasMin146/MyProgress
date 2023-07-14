package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class ChosenExercise2 extends AppCompatActivity {
    Button saveSetButton;
    ArrayList<String> repsList;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosen_exercise2);

        TextView textView = findViewById(R.id.exerciseName);
        dbHelper = new DataBaseHelper(this);
        repsList = new ArrayList<>();
        saveSetButton = findViewById(R.id.saveSetButton);

        String name = getIntent().getStringExtra("Name");
        String detailsId = getIntent().getStringExtra("ID");
        String reps = getIntent().getStringExtra("Reps");
        String weight = getIntent().getStringExtra("Weight");

        String workoutName = getIntent().getStringExtra("WorkoutName");


        String[] repsArray = reps.split(", ");
        String[] weightArray = weight.split(", ");

        EditText[] repsEditTexts = new EditText[6];
        repsEditTexts[0] = findViewById(R.id.repsET1);
        repsEditTexts[1] = findViewById(R.id.repsET2);
        repsEditTexts[2] = findViewById(R.id.repsET3);
        repsEditTexts[3] = findViewById(R.id.repsET4);
        repsEditTexts[4] = findViewById(R.id.repsET5);
        repsEditTexts[5] = findViewById(R.id.repsET6);

        EditText[] weightEditTexts = new EditText[6];
        weightEditTexts[0] = findViewById(R.id.weightET1);
        weightEditTexts[1] = findViewById(R.id.weightET2);
        weightEditTexts[2] = findViewById(R.id.weightET3);
        weightEditTexts[3] = findViewById(R.id.weightET4);
        weightEditTexts[4] = findViewById(R.id.weightET5);
        weightEditTexts[5] = findViewById(R.id.weightET6);

        // logic for how many reps edit texts have value
        for (int i = 0; i < repsArray.length; i++) {
            repsEditTexts[i].setText(repsArray[i]);
        }
        for (int i = repsArray.length; i < repsEditTexts.length; i++) {
            repsEditTexts[i].setText("");
        }

        // logic for how many weight edit texts have value
        for (int i = 0; i < weightArray.length; i++) {
            weightEditTexts[i].setText(weightArray[i]);
        }
        for (int i = weightArray.length; i < weightEditTexts.length; i++) {
            weightEditTexts[i].setText("");
        }

        textView.setText(name);

        saveSetButton.setOnClickListener(v -> {

            StringBuilder repsStringBuilder = new StringBuilder();
            int count = 0;

            for (EditText repsEditText : repsEditTexts) {
                String text = repsEditText.getText().toString().trim();
                if (!text.isEmpty()) {
                    if (count > 0) {
                        repsStringBuilder.append(", ");
                    }
                    repsStringBuilder.append(text);
                    count++;
                }
            }
            String reps1 = repsStringBuilder.toString();


            StringBuilder weightStringBuilder = new StringBuilder();
            int count1 = 0;

            for (EditText weightEditText : weightEditTexts) {
                String text1 = weightEditText.getText().toString().trim();
                if (!text1.isEmpty()) {
                    if (count1 > 0) {
                        weightStringBuilder.append(", ");
                    }
                    weightStringBuilder.append(text1);
                    count1++;
                }
            }

            String weight1 = weightStringBuilder.toString();

            // logic for how many sets we have
            for(int i = 0; i < repsEditTexts.length; i++){
                if(!repsEditTexts[i].getText().toString().isEmpty()){
                    repsList.add(repsEditTexts[i].getText().toString());
                }
            }

            dbHelper.updateExerciseDetails(Integer.valueOf(detailsId), String.valueOf(repsList.size()), reps1, weight1);

            Intent intent = new Intent(v.getContext(), StartWorkoutPage.class);

            intent.putExtra("WorkoutName", workoutName);

            v.getContext().startActivity(intent);
        });
    }

}
