package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomePage extends AppCompatActivity {
    Button firstButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        firstButton = findViewById(R.id.clickToContinue);
        firstButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomePage.this, LoginPage.class);
            startActivity(intent);
            finish();
        });

    }
}