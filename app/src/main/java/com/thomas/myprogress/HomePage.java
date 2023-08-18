package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomePage extends AppCompatActivity {
    CardView startWorkoutCard, workoutHistoryCard, exerciseGraphCard;
    public static boolean isLastWorkoutSaved = true;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        startWorkoutCard = findViewById(R.id.startWorkoutCard);
        workoutHistoryCard = findViewById(R.id.workoutHistoryCard);
        exerciseGraphCard = findViewById(R.id.exerciseGraphCard);
        dbHelper = new DataBaseHelper(this);

        Log.d("test", String.valueOf(isLastWorkoutSaved));

        startWorkoutCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, StartWorkoutPage.class);

            if(isLastWorkoutSaved){
                Date currentDate = new Date();
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat2.format(currentDate);

                dbHelper.addWorkout("Workout", formattedDate, 0L, 0L);
                isLastWorkoutSaved = false;
            }

            startActivity(intent);

        });

        workoutHistoryCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, WorkoutHistory.class);
            startActivity(intent);
        });

        exerciseGraphCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, ExerciseGraph.class);
            startActivity(intent);
        });

    }

}