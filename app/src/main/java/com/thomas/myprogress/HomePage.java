package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;

public class HomePage extends AppCompatActivity {
    CardView startWorkoutCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        startWorkoutCard = findViewById(R.id.startWorkoutCard);

        startWorkoutCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, StartWorkoutPage.class);
            startActivity(intent);

        });

    }
}