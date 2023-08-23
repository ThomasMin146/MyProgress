package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    boolean isWorking, isRunning;
    long workingTime, restingTime, elapsedTime;
    SharedPreferences sharedPreferences;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_workout_page);

        dbHelper = new DataBaseHelper(this);
        stopwatch = new Stopwatch();
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Read data from SharedPreferences
        isWorking = sharedPreferences.getBoolean("isWorking", true);
        isRunning = sharedPreferences.getBoolean("isRunning", false);
        elapsedTime = sharedPreferences.getLong("elapsed_time", 0L);

        int rvPosition = getIntent().getIntExtra("Position", 0);

        workingTime = dbHelper.getWorkingTimeForId(dbHelper.getLastWorkoutId());
        restingTime = dbHelper.getRestingTimeForId(dbHelper.getLastWorkoutId());

        exerciseDetails = dbHelper.getExerciseDetailsByWorkoutId(dbHelper.getLastWorkoutId());

        rootView = findViewById(R.id.rootView);
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

        LinearLayoutManager layoutManager = (LinearLayoutManager) exerciseRecyclerView.getLayoutManager();

        layoutManager.scrollToPositionWithOffset(rvPosition, 0);

        stopwatch.setElapsedTime(elapsedTime);

        long elapsedMillis = stopwatch.getElapsedTime();
        String formattedTime = formatTime(elapsedMillis);
        minTime.setText(formattedTime);

        if(isWorking) {
            rootView.setBackgroundResource(R.drawable.gradient_background_red);
        }
        else {
            rootView.setBackgroundResource(R.drawable.gradient_background_green);
        }

        if(isRunning){
            stopwatch.resume();
            stopButton.setVisibility(View.GONE);
            resumeButton.setVisibility(View.GONE);
            setButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
        }

        if(!isRunning && elapsedTime == 0L)  {
            stopButton.setVisibility(View.GONE);
            resumeButton.setVisibility(View.GONE);
            setButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
        }

        if(!isRunning && elapsedTime != 0L){
            stopButton.setVisibility(View.VISIBLE);
            resumeButton.setVisibility(View.VISIBLE);
            setButton.setVisibility(View.GONE);
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.GONE);
        }

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
            Intent intent3 = new Intent(StartWorkoutPage.this, HomePage.class);
            startActivity(intent3);
        });

        playButton.setOnClickListener(v -> {
            stopwatch.start();
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
            minTime.setText(R.string.start_timer);
            stopButton.setVisibility(View.GONE);
            resumeButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
        });

        setButton.setOnClickListener(v -> {
            Date currentDate = new Date();
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String formattedDate2 = dateFormat2.format(currentDate);

            if(isWorking){
                rootView.setBackgroundResource(R.drawable.gradient_background_green);
                workingTime = dbHelper.getWorkingTimeForId(dbHelper.getLastWorkoutId()) + stopwatch.getElapsedTime();
                isWorking = false;

            } else {
                rootView.setBackgroundResource(R.drawable.gradient_background_red);
                restingTime = dbHelper.getRestingTimeForId(dbHelper.getLastWorkoutId()) + stopwatch.getElapsedTime();
                isWorking = true;
            }

            dbHelper.updateWorkout(dbHelper.getLastWorkoutId(), workoutName.getText().toString(),
                                  formattedDate2, workingTime, restingTime);

            stopwatch.reset();
            stopwatch.start();
        });

        addButton.setOnClickListener(v -> {
            Intent intent2 = new Intent(StartWorkoutPage.this, AddExercise.class);
            intent2.putExtra("Position", exerciseDetails.size());
            startActivity(intent2);
        });

        saveWorkout.setOnClickListener(v -> {
            stopwatch.reset();

            Date currentDate = new Date();
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String formattedDate2 = dateFormat2.format(currentDate);

            if(isWorking){
                workingTime = workingTime + stopwatch.getElapsedTime();
            } else {
                restingTime = restingTime + stopwatch.getElapsedTime();
            }

            if(workoutName.getText().toString().trim().isEmpty()){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Workout couldn't be saved");
                builder.setMessage("Please provide a name for your workout before saving.");
                builder.setNeutralButton("Ok", (null));
                builder.show();

            } else{
                dbHelper.updateWorkout(dbHelper.getLastWorkoutId(), workoutName.getText().toString(), formattedDate2, workingTime, restingTime);
                dbHelper.updateExerciseDetailsWorkoutID(dbHelper.getLastWorkoutId());
                Intent intent1 = new Intent(StartWorkoutPage.this, HomePage.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLastWorkoutSaved", true);
                editor.apply();
                startActivity(intent1);
            }

            isWorking = true;
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

        intent.putExtra("Name", dbHelper.getExerciseName(exerciseDetails.get(position).getExerciseId()));
        intent.putExtra("ID", String.valueOf(exerciseDetails.get(position).getId()));
        intent.putExtra("Reps", exerciseDetails.get(position).getReps());
        intent.putExtra("Sets", exerciseDetails.get(position).getSets());
        intent.putExtra("Weight", exerciseDetails.get(position).getWeight());
        intent.putExtra("Position", position);

        startActivity(intent);

    }

    @Override
    public void onAddItemClick(int position) {

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        // Write data to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("elapsed_time", stopwatch.getElapsedTime()); // Example elapsed time value
        editor.putBoolean("isRunning", stopwatch.isRunning()); // Example isRunning value
        editor.putBoolean("isWorking", isWorking); // Example isWorking value
        editor.apply(); // Commit changes

    }

}