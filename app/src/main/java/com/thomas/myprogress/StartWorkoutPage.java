package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thomas.myprogress.adapters.WorkoutRVAdapter;
import com.thomas.myprogress.models.ExerciseDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StartWorkoutPage extends AppCompatActivity implements RVInterface{
    DataBaseHelper dbHelper;
    Stopwatch stopwatch;
    TextView minTime, backButton;
    Button playButton, resumeButton, setButton, pauseButton, stopButton, addButton, saveWorkout;
    ArrayList<ExerciseDetails> exerciseDetails;
    RecyclerView exerciseRecyclerView;
    WorkoutRVAdapter exerciseAdapter;
    EditText workoutName;
    boolean isWorking;
    long workingTime, restingTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_workout_page);

        dbHelper = new DataBaseHelper(this);
        stopwatch = new Stopwatch();

        isWorking = false;
        workingTime = 0;
        restingTime = 0;

        exerciseDetails = dbHelper.getExerciseDetailsByWorkoutId(dbHelper.getLastWorkoutId());

        exerciseRecyclerView = findViewById(R.id.exerciseRecyclerView);
        addButton = findViewById(R.id.addExercise);
        saveWorkout = findViewById(R.id.saveWorkout);
        minTime = findViewById(R.id.minTime);
        workoutName = findViewById(R.id.workoutName);
        backButton = findViewById(R.id.backButton);

        workoutName.setText(dbHelper.getWorkoutName(dbHelper.getLastWorkoutId()));

        playButton = findViewById(R.id.playButton);
        resumeButton = findViewById(R.id.resumeButton);
        stopButton = findViewById(R.id.stopButton);
        pauseButton = findViewById(R.id.pauseButton);
        setButton = findViewById(R.id.setButton);

        exerciseAdapter = new WorkoutRVAdapter(this, exerciseDetails, this);
        exerciseRecyclerView.setAdapter(exerciseAdapter);
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (stopwatch.isRunning()) {
                    long elapsedMillis = stopwatch.getElapsedTime();
                    String formattedTime = formatTime(elapsedMillis);
                    minTime.setText(formattedTime);
                }
                handler.postDelayed(this, 10); // Update every 100 milliseconds
            }
        };
        handler.postDelayed(runnable, 10);

        workoutName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dbHelper.updateWorkoutName(dbHelper.getLastWorkoutId(), s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartWorkoutPage.this, HomePage.class);
            intent.putExtra("WorkoutName", workoutName.getText().toString());
            startActivity(intent);
        });

        playButton.setOnClickListener(v -> {
            stopwatch.start();
            isWorking = true;
            minTime.setBackgroundColor(Color.RED);

            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
            setButton.setVisibility(View.VISIBLE);
        });

        pauseButton.setOnClickListener(v -> {
            stopwatch.pause();
            setButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.VISIBLE);
            resumeButton.setVisibility(View.VISIBLE);

        });

        resumeButton.setOnClickListener(v -> {
            stopwatch.resume();
            stopButton.setVisibility(View.GONE);
            resumeButton.setVisibility(View.GONE);
            setButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.VISIBLE);
        });

        stopButton.setOnClickListener(v -> {
            stopwatch.reset();
            minTime.setText("00 : 00 . 00");
            stopButton.setVisibility(View.GONE);
            resumeButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
        });

        setButton.setOnClickListener(v -> {

            if(isWorking){
                minTime.setBackgroundColor(Color.GREEN);
                workingTime = workingTime + stopwatch.getElapsedTime();

                isWorking = false;
            } else {
                minTime.setBackgroundColor(Color.RED);
                restingTime = restingTime + stopwatch.getElapsedTime();

                isWorking = true;
            }

            stopwatch.reset();
            stopwatch.start();
        });

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartWorkoutPage.this, AddExercise.class);
            startActivity(intent);
        });

        saveWorkout.setOnClickListener(v -> {
            Date currentDate = new Date();
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate2 = dateFormat2.format(currentDate);

            if(isWorking){
                workingTime = workingTime + stopwatch.getElapsedTime();
            } else {
                restingTime = restingTime + stopwatch.getElapsedTime();
            }

            dbHelper.updateWorkout(dbHelper.getLastWorkoutId(), workoutName.getText().toString(), formattedDate2, workingTime, restingTime);

            dbHelper.updateExerciseDetailsWorkoutID(dbHelper.getLastWorkoutId());

            Intent intent = new Intent(StartWorkoutPage.this, HomePage.class);
            HomePage.isLastWorkoutSaved = true;
            startActivity(intent);

        });

    }

    private String formatTime(long elapsedMillis) {
        int minutes = (int) (elapsedMillis % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) (elapsedMillis % (1000 * 60)) / 1000;
        int milliseconds = (int) (elapsedMillis % 1000);

        return String.format(Locale.getDefault(), "%02d : %02d . %02d", minutes, seconds, milliseconds/10);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(StartWorkoutPage.this, ChosenExercise.class);

        intent.putExtra("WorkoutName", workoutName.getText().toString());

        intent.putExtra("Name", dbHelper.getExerciseName(exerciseDetails.get(position).getExerciseId()));
        intent.putExtra("ID", String.valueOf(exerciseDetails.get(position).getId()));
        intent.putExtra("Reps", exerciseDetails.get(position).getReps());
        intent.putExtra("Sets", exerciseDetails.get(position).getSets());
        intent.putExtra("Weight", exerciseDetails.get(position).getWeight());
        intent.putExtra("position", position);

        startActivity(intent);

    }

    @Override
    public void onAddItemClick(int position) {

    }


}