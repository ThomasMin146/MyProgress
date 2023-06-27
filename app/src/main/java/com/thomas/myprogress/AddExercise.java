package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.thomas.myprogress.dbhelper.DataBaseHelper;

import java.util.ArrayList;

public class AddExercise extends AppCompatActivity {
    AddExerciseAdapter addExerciseAdapter;
    ArrayList<ExerciseModel> exerciseItems;
    RecyclerView exerciseRV;
    DataBaseHelper dbHelper;
    SQLiteDatabase db;

    Button createExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise);

        dbHelper = new DataBaseHelper(this);
        db = dbHelper.getWritableDatabase();

        exerciseRV = findViewById(R.id.exerciseRV);
        createExercise = findViewById(R.id.createExercise);

        exerciseItems = new ArrayList<>();

        addExerciseAdapter = new AddExerciseAdapter(this, exerciseItems);
        exerciseRV.setAdapter(addExerciseAdapter);
        exerciseRV.setLayoutManager(new LinearLayoutManager(this));

        // database operations
        Cursor cursor = db.query("Exercise", null, null, null, null, null, null);
        while (cursor.moveToNext()) {

            ExerciseModel item = new ExerciseModel();
            item.setExerciseName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            item.setBodypart(cursor.getString(cursor.getColumnIndexOrThrow("type")));
            item.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow("difficulty")));
            exerciseItems.add(item);

        }
        cursor.close();
        db.close();

        createExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddExercise.this, CreateExercise.class);
                startActivity(intent);
            }
        });

    }



}