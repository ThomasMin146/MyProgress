package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thomas.myprogress.dbhelper.DataBaseHelper;

import org.w3c.dom.Text;

public class LoginPage extends AppCompatActivity {
    private DataBaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        dbHelper = new DataBaseHelper(this);

        TextView username = findViewById(R.id.username);
        TextView password = findViewById(R.id.password);
        //TextView testText = findViewById(R.id.testText);

        Button loginButton = findViewById(R.id.loginButton);
        Button registrateButton = findViewById(R.id.registrateButton);
        Button allUsers = findViewById(R.id.idBtnReadCourse);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    Toast.makeText(LoginPage.this, "Login Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginPage.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }


            }
        });

        registrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });

        allUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginPage.this, ViewUsers.class);
                startActivity(i);
            }
        });
    }
}