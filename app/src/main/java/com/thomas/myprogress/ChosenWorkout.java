package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.thomas.myprogress.adapters.ExerciseDetailsHistoryRVAdapter;
import com.thomas.myprogress.adapters.WorkoutHistoryRVAdapter;
import com.thomas.myprogress.adapters.WorkoutRVAdapter;
import com.thomas.myprogress.models.ExerciseDetails;

import java.util.ArrayList;

public class ChosenWorkout extends AppCompatActivity {
    TextView workoutName, backBtn;
    ArrayList<ExerciseDetails> exerciseDetails;
    DataBaseHelper dbHelper;
    RecyclerView exerciseRecyclerView;
    ExerciseDetailsHistoryRVAdapter exerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosen_workout);

        workoutName = findViewById(R.id.workoutName);
        exerciseRecyclerView = findViewById(R.id.workoutRecyclerView);
        backBtn = findViewById(R.id.backButton);

        dbHelper = new DataBaseHelper(this);

        int workoutid = getIntent().getIntExtra("id", -1);
        String workoutNameString = getIntent().getStringExtra("name");
        workoutName.setText(workoutNameString);
        exerciseDetails = dbHelper.getExerciseDetailsByWorkoutId(workoutid);

        exerciseAdapter = new ExerciseDetailsHistoryRVAdapter(this, exerciseDetails);
        exerciseRecyclerView.setAdapter(exerciseAdapter);
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ChosenWorkout.this, WorkoutHistory.class);
            startActivity(intent);
        });
    }

}