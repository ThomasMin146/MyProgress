package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.thomas.myprogress.adapters.WorkoutHistoryRVAdapter;
import com.thomas.myprogress.models.Workout;

import java.util.ArrayList;

public class WorkoutHistory extends AppCompatActivity implements RVInterface{
    ArrayList<Workout> workouts;
    RecyclerView workoutRecyclerView;
    WorkoutHistoryRVAdapter workoutAdapter;
    DataBaseHelper dbHelper;
    TextView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_history);

        dbHelper = new DataBaseHelper(this);

        workouts = dbHelper.getAllWorkouts();

        workoutRecyclerView = findViewById(R.id.workoutRecyclerView);
        backBtn = findViewById(R.id.backButton);

        workoutAdapter = new WorkoutHistoryRVAdapter(this, workouts, this);
        workoutRecyclerView.setAdapter(workoutAdapter);
        workoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WorkoutHistory.this, HomePage.class);
            startActivity(intent);
        });

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(WorkoutHistory.this, ChosenWorkout.class);
        intent.putExtra("name", workouts.get(position).getName());
        intent.putExtra("id", workouts.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onAddItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position, boolean isChecked) {

    }
}