package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;

public class HomePage extends AppCompatActivity {
    CardView startWorkoutCard, workoutHistoryCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        startWorkoutCard = findViewById(R.id.startWorkoutCard);
        workoutHistoryCard = findViewById(R.id.workoutHistoryCard);

        startWorkoutCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, StartWorkoutPage.class);
            startActivity(intent);

        });

        workoutHistoryCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, WorkoutHistory.class);
            startActivity(intent);
        });

    }
}