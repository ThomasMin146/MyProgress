package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thomas.myprogress.dbhelper.DataBaseHelper;

import java.util.ArrayList;
import java.util.Locale;

public class StartWorkoutPage extends AppCompatActivity {
    DataBaseHelper dbHelper;
    Stopwatch stopwatch;
    TextView minTime;
    private Button playButton, resumeButton, setButton, pauseButton, stopButton;
    ArrayList<ItemExercise> exerciseItems;
    RecyclerView exerciseRecyclerView;
    ExerciseAdapter exerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_workout_page);

        dbHelper = new DataBaseHelper(this);
        stopwatch = new Stopwatch();
        exerciseItems = new ArrayList<>();

        exerciseRecyclerView = findViewById(R.id.exerciseRecyclerView);
        minTime = findViewById(R.id.minTime);

        playButton = findViewById(R.id.playButton);
        resumeButton = findViewById(R.id.resumeButton);
        stopButton = findViewById(R.id.stopButton);
        pauseButton = findViewById(R.id.pauseButton);
        setButton = findViewById(R.id.setButton);

        setUpExerciseItems();
        exerciseAdapter = new ExerciseAdapter(this, exerciseItems);
        exerciseAdapter.getItemCount();
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

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopwatch.start();
                playButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
                setButton.setVisibility(View.VISIBLE);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopwatch.pause();
                setButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
                resumeButton.setVisibility(View.VISIBLE);

            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopwatch.resume();
                stopButton.setVisibility(View.GONE);
                resumeButton.setVisibility(View.GONE);
                setButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.VISIBLE);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopwatch.reset();
                minTime.setText("00 : 00 . 00");
                stopButton.setVisibility(View.GONE);
                resumeButton.setVisibility(View.GONE);
                playButton.setVisibility(View.VISIBLE);
            }
        });

    }

    private String formatTime(long elapsedMillis) {
        int hours = (int) (elapsedMillis / (1000 * 60 * 60));
        int minutes = (int) (elapsedMillis % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) (elapsedMillis % (1000 * 60)) / 1000;
        int milliseconds = (int) (elapsedMillis % 1000);

        return String.format(Locale.getDefault(), "%02d : %02d . %02d", minutes, seconds, milliseconds/10);
    }

    private void setUpExerciseItems(){
        String[] exerciseNames = {"Pushups", "Pullups", "Dips", "Squats", "L-sit"};
        int[] exerciseReps = {15, 10, 10, 25, 20};
        ExerciseEnums.BodyPart[] bodyPart = {ExerciseEnums.BodyPart.CHEST, ExerciseEnums.BodyPart.BACK, ExerciseEnums.BodyPart.TRICEPS, ExerciseEnums.BodyPart.LEGS, ExerciseEnums.BodyPart.ABS};
        ExerciseEnums.ExerciseDifficulty[] exerciseDifficulty = {ExerciseEnums.ExerciseDifficulty.BEGINNER, ExerciseEnums.ExerciseDifficulty.BEGINNER, ExerciseEnums.ExerciseDifficulty.BEGINNER, ExerciseEnums.ExerciseDifficulty.BEGINNER, ExerciseEnums.ExerciseDifficulty.INTERMEDIATE};

        for (int i = 0; i<exerciseNames.length; i++){
            exerciseItems.add(new ItemExercise(exerciseNames[i], exerciseReps[i], bodyPart[i], exerciseDifficulty[i]));
        }
    }
}