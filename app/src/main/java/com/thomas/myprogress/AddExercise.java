package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.thomas.myprogress.dbhelper.DataBaseHelper;

import java.util.ArrayList;

public class AddExercise extends AppCompatActivity {
    AddExerciseAdapter addExerciseAdapter;
    ArrayList<ExerciseModel> exerciseItems;
    RecyclerView exerciseRV;
    DataBaseHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise);

        dbHelper = new DataBaseHelper(this);
        db = dbHelper.getWritableDatabase();

        exerciseRV = findViewById(R.id.exerciseRV);

        exerciseItems = new ArrayList<>();

        addExerciseAdapter = new AddExerciseAdapter(this, exerciseItems);
        exerciseRV.setAdapter(addExerciseAdapter);
        exerciseRV.setLayoutManager(new LinearLayoutManager(this));

        // database operations
        Cursor cursor = db.query("Exercise", null, null, null, null, null, null);
        while (cursor.moveToNext()) {

            ExerciseModel item = new ExerciseModel();
            item.setExerciseName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            exerciseItems.add(item);

        }
        cursor.close();
    }


}