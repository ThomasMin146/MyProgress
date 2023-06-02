package com.thomas.myprogress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thomas.myprogress.dbhelper.DataBaseHelper;

import java.util.ArrayList;
import java.util.Locale;

public class StartWorkoutPage extends AppCompatActivity {
    DataBaseHelper dbHelper;
    Stopwatch stopwatch;
    TextView minTime;
    private Button buttonStart, buttonReset;
    //ArrayList<ExerciseItem> exerciseItems;
    ArrayList<String> test;
    RecyclerView exerciseRecyclerView;
    ExerciseAdapter exerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing);

        dbHelper = new DataBaseHelper(this);
        stopwatch = new Stopwatch();
        test = new ArrayList<>();

        exerciseRecyclerView = findViewById(R.id.mRecyclerView);
        //minTime = findViewById(R.id.minTime);
        //buttonStart = findViewById(R.id.startTimerButton);
        //buttonReset = findViewById(R.id.stopTimerButton);

        setUpExerciseItems();
        exerciseAdapter = new ExerciseAdapter(this, test);
        exerciseAdapter.getItemCount();
        exerciseRecyclerView.setAdapter(exerciseAdapter);
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (stopwatch.isRunning()) {
                    long elapsedMillis = stopwatch.getElapsedTime();
                    String formattedTime = formatTime(elapsedMillis);
                    minTime.setText(formattedTime);
                }
                handler.postDelayed(this, 100); // Update every 100 milliseconds
            }
        };
        handler.postDelayed(runnable, 100);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopwatch.start();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopwatch.reset();
            }
        });*/

    }

    private String formatTime(long elapsedMillis) {
        int hours = (int) (elapsedMillis / (1000 * 60 * 60));
        int minutes = (int) (elapsedMillis % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) (elapsedMillis % (1000 * 60)) / 1000;
        int milliseconds = (int) (elapsedMillis % 1000);

        return String.format(Locale.getDefault(), "%02d : %02d : %02d", hours, minutes, seconds);
    }

    private void setUpExerciseItems(){
        String[] exerciseNames = {"pushup", "pullup", "dips"};
        //int[] exerciseReps = {15, 10, 10};
        //ExerciseEnums.BodyPart[] bodyPart = {ExerciseEnums.BodyPart.CHEST, ExerciseEnums.BodyPart.BACK, ExerciseEnums.BodyPart.TRICEPS};
        //ExerciseEnums.ExerciseDifficulty[] exerciseDifficulty = {ExerciseEnums.ExerciseDifficulty.BEGINNER, ExerciseEnums.ExerciseDifficulty.BEGINNER, ExerciseEnums.ExerciseDifficulty.BEGINNER};

        for (int i = 0; i<exerciseNames.length; i++){
            test.add(exerciseNames[i]);
        }
    }
}