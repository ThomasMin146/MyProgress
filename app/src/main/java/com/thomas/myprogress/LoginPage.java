package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thomas.myprogress.dbhelper.DataBaseHelper;


public class LoginPage extends AppCompatActivity {
    private DataBaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        dbHelper = new DataBaseHelper(this);

        TextView username = findViewById(R.id.username);
        TextView password = findViewById(R.id.password);

        username.setTextColor(Color.WHITE);
        password.setTextColor(Color.WHITE);

        TextView registerHere = findViewById(R.id.missingaccount);
        registerHere.setPaintFlags(registerHere.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dbHelper.checkUser(username.getText().toString(), password.getText().toString())){
                    Intent intent = new Intent(LoginPage.this, HomePage.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginPage.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });

    }
}