package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.thomas.myprogress.dbhelper.DataBaseHelper;


public class RegisterPage extends AppCompatActivity {
    DataBaseHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        Button createAcc = findViewById(R.id.createAccountButton);
        EditText newUsername = findViewById(R.id.newUsername);
        EditText newPassword = findViewById(R.id.newPassword);

        //DataBase connection
        dbHelper = new DataBaseHelper(this);


        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.addRow(newUsername.getText().toString(), newPassword.getText().toString());

                newUsername.setText("");
                newPassword.setText("");
            }
        });

    }

}