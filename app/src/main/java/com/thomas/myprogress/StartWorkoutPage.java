package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thomas.myprogress.adapters.ExerciseRVAdapter;
import com.thomas.myprogress.adapters.WorkoutRVAdapter;
import com.thomas.myprogress.models.ExerciseDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StartWorkoutPage extends AppCompatActivity implements RVInterface{
    DataBaseHelper dbHelper;
    Stopwatch stopwatch;
    TextView minTime;
    Button playButton, resumeButton, setButton, pauseButton, stopButton, addButton, saveWorkout;
    ArrayList<ExerciseDetails> exerciseDetails;
    RecyclerView exerciseRecyclerView;
    WorkoutRVAdapter exerciseAdapter;
    EditText workoutName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_workout_page);

        dbHelper = new DataBaseHelper(this);
        stopwatch = new Stopwatch();

        exerciseDetails = dbHelper.getAllExerciseDetails();
        //exerciseDetails = new ArrayList<>();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);

        exerciseRecyclerView = findViewById(R.id.exerciseRecyclerView);
        addButton = findViewById(R.id.addExercise);
        saveWorkout = findViewById(R.id.saveWorkout);
        minTime = findViewById(R.id.minTime);
        workoutName = findViewById(R.id.workoutName);


        playButton = findViewById(R.id.playButton);
        resumeButton = findViewById(R.id.resumeButton);
        stopButton = findViewById(R.id.stopButton);
        pauseButton = findViewById(R.id.pauseButton);
        setButton = findViewById(R.id.setButton);


        exerciseAdapter = new WorkoutRVAdapter(this, exerciseDetails, this);
        exerciseRecyclerView.setAdapter(exerciseAdapter);
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        workoutName.setText("Unnamed");


        long workoutId = dbHelper.addWorkout(workoutName.getText().toString(), formattedDate, 0);

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
            minTime.setText("00 : 00 . 00");
            stopButton.setVisibility(View.GONE);
            resumeButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
        });

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartWorkoutPage.this, AddExercise.class);
            intent.putExtra("workoutId", workoutId);
            startActivity(intent);
        });

        saveWorkout.setOnClickListener(v -> {
            Date currentDate = new Date();
            String formattedDate2 = dateFormat.format(currentDate);
            dbHelper.updateWorkout(workoutId, workoutName.getText().toString(), formattedDate2, 0);

            Intent intent = new Intent(StartWorkoutPage.this, HomePage.class);
            startActivity(intent);

        });

    }

    private String formatTime(long elapsedMillis) {
        int hours = (int) (elapsedMillis / (1000 * 60 * 60));
        int minutes = (int) (elapsedMillis % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) (elapsedMillis % (1000 * 60)) / 1000;
        int milliseconds = (int) (elapsedMillis % 1000);

        return String.format(Locale.getDefault(), "%02d : %02d . %02d", minutes, seconds, milliseconds/10);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(StartWorkoutPage.this, ChosenExercise2.class);
        intent.putExtra("Name", dbHelper.getExerciseName(exerciseDetails.get(position).getExerciseId()));
        intent.putExtra("ID", String.valueOf(exerciseDetails.get(position).getId()));
        intent.putExtra("Reps", exerciseDetails.get(position).getReps());
        intent.putExtra("Weight", exerciseDetails.get(position).getWeight());
        intent.putExtra("position", position);

        startActivity(intent);

    }

}