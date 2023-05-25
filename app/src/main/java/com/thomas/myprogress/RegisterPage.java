package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.thomas.myprogress.dbhelper.DbHelper;

import java.io.IOException;
import java.util.List;

public class RegisterPage extends AppCompatActivity {
    private DbHelper dbHelper;
    private ListView lvUsers;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        Button createAcc = (Button) findViewById(R.id.createAccountButton);


        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DbHelper(getApplicationContext());
                try {
                    dbHelper.createDataBase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                lvUsers = (ListView)findViewById(R.id.activityList);
                List<String> listUsers = dbHelper.getAllUsers();
                if(listUsers != null){
                    adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, android.R.id.text1, listUsers);
                    lvUsers.setAdapter(adapter);
                }
            }
        });

    }

}