package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.thomas.myprogress.adapters.ExerciseDetailsHistoryRVAdapter;
import com.thomas.myprogress.adapters.WorkoutHistoryRVAdapter;
import com.thomas.myprogress.adapters.WorkoutRVAdapter;
import com.thomas.myprogress.models.ExerciseDetails;

import java.util.ArrayList;

public class ChosenWorkout extends AppCompatActivity implements RVInterface{

    TextView workoutName;
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
        dbHelper = new DataBaseHelper(this);

        workoutName.setText(getIntent().getStringExtra("name"));

        int workoutid = getIntent().getIntExtra("id", -1);
        exerciseDetails = dbHelper.getExerciseDetailsByWorkoutId(workoutid);

        //exerciseDetails = new ArrayList<>();
        //exerciseDetails.add(new ExerciseDetails(300,300,300,"1", "5", "10"));



        exerciseAdapter = new ExerciseDetailsHistoryRVAdapter(this, exerciseDetails, this);
        exerciseRecyclerView.setAdapter(exerciseAdapter);
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onAddItemClick(int position) {

    }
}