package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterPage extends AppCompatActivity {
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        Button createAcc = findViewById(R.id.createAccountButton);
        EditText newUsername = findViewById(R.id.newUsername);
        EditText newPassword = findViewById(R.id.newPassword);

        //DataBase connection
        dbHelper = new DataBaseHelper(this);

        createAcc.setOnClickListener(v -> {
            dbHelper.addUser(newUsername.getText().toString(), newPassword.getText().toString());

            newUsername.setText("");
            newPassword.setText("");
            Toast.makeText(RegisterPage.this, "Account created.", Toast.LENGTH_SHORT).show();
        });

    }

}