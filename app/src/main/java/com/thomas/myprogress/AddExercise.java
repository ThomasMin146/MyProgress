package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import com.thomas.myprogress.adapters.ExerciseRVAdapter;
import com.thomas.myprogress.models.Exercise;
import java.util.ArrayList;
import java.util.Iterator;

public class AddExercise extends AppCompatActivity implements RVInterface{
    ExerciseRVAdapter addExerciseAdapter;
    ArrayList<Exercise> exercises, filteredList;
    RecyclerView exerciseRV;
    DataBaseHelper dbHelper;
    TextView createExercise;
    EditText searchEditText;
    TextView backBtn;
    int listPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise);

        dbHelper = new DataBaseHelper(this);

        exerciseRV = findViewById(R.id.exerciseRV);
        createExercise = findViewById(R.id.createExercise);
        searchEditText = findViewById(R.id.searchExerciseET);
        backBtn = findViewById(R.id.backButton);

        exercises = dbHelper.getAllExercises();
        //filteredList = dbHelper.getAllExercises();
        filteredList = new ArrayList<>();

        addExerciseAdapter = new ExerciseRVAdapter(this, exercises, this, dbHelper.getLastWorkoutId());
        exerciseRV.setAdapter(addExerciseAdapter);
        exerciseRV.setLayoutManager(new LinearLayoutManager(this));

        listPosition = getIntent().getIntExtra("Position", 0);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AddExercise.this, StartWorkoutPage.class);
            startActivity(intent);
        });

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
        filteredList.clear();

        for (Exercise exercise : exercises) {
            // Perform the search logic based on your item's properties
            // For example, if you have a "name" property in YourItem class:
            String itemName = exercise.getName();
            String itemBodyPart = exercise.getBodypart();
            String itemDifficulty = exercise.getDifficulty();

            itemBodyPart = (itemBodyPart != null) ? itemBodyPart.toLowerCase() : "";
            itemDifficulty = (itemDifficulty != null) ? itemDifficulty.toLowerCase() : "";

            if (itemName.toLowerCase().contains(searchText.toLowerCase()) ||
                    itemBodyPart.contains(searchText.toLowerCase()) ||
                    itemDifficulty.contains(searchText.toLowerCase())) {
                filteredList.add(exercise);
            }


        }

        // Update the RecyclerView adapter with the filtered list
        addExerciseAdapter.setItems(filteredList);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(AddExercise.this, StartWorkoutPage.class);
        intent.putExtra("ExerciseId", exercises.get(position).getId());
        intent.putExtra("ExerciseName", exercises.get(position).getName());
        intent.putExtra("Position", listPosition);
        startActivity(intent);
    }

    @Override
    public void onAddItemClick(int position) {
        Intent intent = new Intent(AddExercise.this, UpdateExercise.class);
        intent.putExtra("ID", exercises.get(position).getId());
        intent.putExtra("ExerciseName", exercises.get(position).getName());
        intent.putExtra("Bodypart", exercises.get(position).getBodypart());
        intent.putExtra("Difficulty", exercises.get(position).getDifficulty());
        intent.putExtra("position", position);

        startActivity(intent);

    }

    @Override
    public void onLongItemClick(int position, boolean isChecked) {
        exercises.get(position).setChecked(isChecked);

    }

}