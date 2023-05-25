package com.thomas.myprogress;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thomas.myprogress.dbhelper.DataBaseHelper;

import java.util.ArrayList;

public class ViewUsers extends AppCompatActivity {

    // creating variables for our array list,
    // dbhandler, adapter and recycler view.
    private ArrayList<UserModel> courseModalArrayList;
    private DataBaseHelper dbHandler;
    private UserRVAdapter courseRVAdapter;
    private RecyclerView coursesRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        // initializing our all variables.
        courseModalArrayList = new ArrayList<>();
        dbHandler = new DataBaseHelper(ViewUsers.this);

        // getting our course array
        // list from db handler class.
        courseModalArrayList = dbHandler.readUsers();

        // on below line passing our array list to our adapter class.
        courseRVAdapter = new UserRVAdapter(courseModalArrayList, ViewUsers.this);
        coursesRV = findViewById(R.id.idRVUsers);

        // setting layout manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewUsers.this, RecyclerView.VERTICAL, false);
        coursesRV.setLayoutManager(linearLayoutManager);

        // setting our adapter to recycler view.
        coursesRV.setAdapter(courseRVAdapter);
    }
}
