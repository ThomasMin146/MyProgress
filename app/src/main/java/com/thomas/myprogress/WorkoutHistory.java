package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.thomas.myprogress.adapters.WorkoutHistoryRVAdapter;
import com.thomas.myprogress.adapters.WorkoutRVAdapter;
import com.thomas.myprogress.models.ExerciseDetails;
import com.thomas.myprogress.models.Workout;

import java.util.ArrayList;

public class WorkoutHistory extends AppCompatActivity implements RVInterface{
    ArrayList<Workout> workouts;
    RecyclerView workoutRecyclerView;
    WorkoutHistoryRVAdapter workoutAdapter;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_history);

        dbHelper = new DataBaseHelper(this);

        workouts = dbHelper.getAllWorkouts();


        workoutRecyclerView = findViewById(R.id.workoutRecyclerView);

        workoutAdapter = new WorkoutHistoryRVAdapter(this, workouts, this);
        workoutRecyclerView.setAdapter(workoutAdapter);
        workoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(WorkoutHistory.this, ChosenWorkout.class);
        intent.putExtra("name", workouts.get(position).getName());
        intent.putExtra("id", workouts.get(position).getId());

        startActivity(intent);
    }
}