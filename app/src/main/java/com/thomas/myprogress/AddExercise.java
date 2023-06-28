package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.thomas.myprogress.dbhelper.DataBaseHelper;

import java.util.ArrayList;

public class AddExercise extends AppCompatActivity implements RVInterface{
    AddExerciseAdapter addExerciseAdapter;
    ArrayList<ExerciseModel> exerciseItems;
    RecyclerView exerciseRV;
    DataBaseHelper dbHelper;
    SQLiteDatabase db;
    Button createExercise;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise);

        dbHelper = new DataBaseHelper(this);
        db = dbHelper.getWritableDatabase();

        exerciseRV = findViewById(R.id.exerciseRV);
        createExercise = findViewById(R.id.createExercise);
        searchEditText = findViewById(R.id.searchExerciseET);

        exerciseItems = new ArrayList<>();

        addExerciseAdapter = new AddExerciseAdapter(this, exerciseItems, this);
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

        createExercise.setOnClickListener(v -> {
            Intent intent = new Intent(AddExercise.this, CreateExercise.class);
            startActivity(intent);
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used in this case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the RecyclerView items based on the search text
                filterItems(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used in this case
            }
        });

    }

    private void filterItems(String searchText) {
        ArrayList<ExerciseModel> filteredList = new ArrayList<>();

        for (ExerciseModel item : exerciseItems) {
            // Perform the search logic based on your item's properties
            // For example, if you have a "name" property in YourItem class:
            String itemName = item.getExerciseName();
            String itemBodyPart = item.getBodypart();
            String itemDifficulty = item.getDifficulty();

            itemBodyPart = (itemBodyPart != null) ? itemBodyPart.toLowerCase() : "";
            itemDifficulty = (itemDifficulty != null) ? itemDifficulty.toLowerCase() : "";

            if (itemName.toLowerCase().contains(searchText.toLowerCase()) ||
                    itemBodyPart.contains(searchText.toLowerCase()) ||
                    itemDifficulty.contains(searchText.toLowerCase())) {
                filteredList.add(item);
            }


        }

        // Update the RecyclerView adapter with the filtered list
        addExerciseAdapter.setItems(filteredList);
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(AddExercise.this, UpdateExercise.class);
        intent.putExtra("ExerciseName", exerciseItems.get(position).getExerciseName());
        intent.putExtra("Bodypart", exerciseItems.get(position).getBodypart());
        intent.putExtra("Difficulty", exerciseItems.get(position).getDifficulty());
        intent.putExtra("position", position);

        startActivity(intent);
    }
}