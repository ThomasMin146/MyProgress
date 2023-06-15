package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.os.Bundle;

import java.util.ArrayList;

public class AddExercise extends AppCompatActivity {

    ExerciseAdapter exerciseAdapter;
    ArrayList<ItemExercise> exerciseItems;
    RecyclerView exerciseRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise);

        exerciseRV = findViewById(R.id.exerciseRV);

        exerciseItems = new ArrayList<>();
        setUpExerciseItems();
        exerciseAdapter = new ExerciseAdapter(this, exerciseItems);
        exerciseRV.setAdapter(exerciseAdapter);
        exerciseRV.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpExerciseItems(){
        String[] exerciseNames = {"Pushups", "Pullups", "Dips", "Squats", "L-sit"};
        int[] exerciseReps = {15, 10, 10, 25, 20};
        ExerciseEnums.BodyPart[] bodyPart = {ExerciseEnums.BodyPart.CHEST, ExerciseEnums.BodyPart.BACK, ExerciseEnums.BodyPart.TRICEPS, ExerciseEnums.BodyPart.LEGS, ExerciseEnums.BodyPart.ABS};
        ExerciseEnums.ExerciseDifficulty[] exerciseDifficulty = {ExerciseEnums.ExerciseDifficulty.BEGINNER, ExerciseEnums.ExerciseDifficulty.BEGINNER, ExerciseEnums.ExerciseDifficulty.BEGINNER, ExerciseEnums.ExerciseDifficulty.BEGINNER, ExerciseEnums.ExerciseDifficulty.INTERMEDIATE};

        for (int i = 0; i<exerciseNames.length; i++){
            exerciseItems.add(new ItemExercise(exerciseNames[i], exerciseReps[i], bodyPart[i], exerciseDifficulty[i]));
        }
    }
}