package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomePage extends AppCompatActivity {
    CardView startWorkoutCard, workoutHistoryCard, exerciseGraphCard;
    boolean isLastWorkoutSaved;
    DataBaseHelper dbHelper;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        startWorkoutCard = findViewById(R.id.startWorkoutCard);
        workoutHistoryCard = findViewById(R.id.workoutHistoryCard);
        exerciseGraphCard = findViewById(R.id.exerciseGraphCard);
        dbHelper = new DataBaseHelper(this);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        isLastWorkoutSaved = sharedPreferences.getBoolean("isLastWorkoutSaved", true);

        startWorkoutCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, StartWorkoutPage.class);

            if(isLastWorkoutSaved){
                Date currentDate = new Date();
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String formattedDate = dateFormat2.format(currentDate);

                dbHelper.addWorkout("", formattedDate, 0L, 0L);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLastWorkoutSaved", false);
                editor.apply();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("elapsed_time", 0L);
                editor.apply();
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