package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.thomas.myprogress.adapters.ExerciseDetailsRVAdapter;

import java.util.ArrayList;

public class ChosenExercise extends AppCompatActivity {
    Button addSetButton, saveSetButton;
    RecyclerView rvLayout;
    ArrayList<String> repList, weightList;
    DataBaseHelper dbHelper;
    TextView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosen_exercise);

        TextView textView = findViewById(R.id.exerciseName);
        backBtn = findViewById(R.id.backButton);

        dbHelper = new DataBaseHelper(this);
        rvLayout = findViewById(R.id.rvLayout);

        String workoutName = getIntent().getStringExtra("WorkoutName");
        String name = getIntent().getStringExtra("Name");
        String detailsId = getIntent().getStringExtra("ID");
        String reps = getIntent().getStringExtra("Reps");
        String sets = getIntent().getStringExtra("Sets");
        String weight = getIntent().getStringExtra("Weight");

        int rvPosition = getIntent().getIntExtra("RVPosition", 0);
        int position = getIntent().getIntExtra("Position", 0);

        String[] repsArray = reps.split(", ");
        String[] weightArray = weight.split(", ");


        addSetButton = findViewById(R.id.addSetButton);
        saveSetButton = findViewById(R.id.saveSetButton);

        repList = new ArrayList<>();
        weightList = new ArrayList<>();

        textView.setText(name);

        if(!sets.equals("")){
            for(int j = 0; j < Integer.parseInt(sets); j++){
                repList.add(repsArray[j]);
                weightList.add(weightArray[j]);

            }
        }

        if(repList.size()==0){
            repList.add("");
            weightList.add("");
        }


        ExerciseDetailsRVAdapter adapter = new ExerciseDetailsRVAdapter(this, repList, weightList);
        rvLayout.setAdapter(adapter);
        rvLayout.setLayoutManager(new LinearLayoutManager(this));

        addSetButton.setOnClickListener(v -> {

            repList.add("");
            weightList.add("");
            adapter.notifyItemInserted(repList.size()-1);

        });

        saveSetButton.setOnClickListener(v -> {

            StringBuilder repsStringBuilder = new StringBuilder();
            int repSize = repList.size();

            for (int i = 0; i < repSize; i++) {
                repsStringBuilder.append(repList.get(i));

                if (i != repSize - 1) {
                    repsStringBuilder.append(", ");
                }
            }

            StringBuilder weightStringBuilder = new StringBuilder();
            int weightSize = weightList.size();

            for (int i = 0; i < weightSize; i++) {
                weightStringBuilder.append(weightList.get(i));

                if (i != weightSize - 1) {
                    weightStringBuilder.append(", ");
                }
            }

            //condition to not write 0 sets in StartWorkoutPage
            if(repList.size()==0){
                dbHelper.updateExerciseDetails(Integer.parseInt(detailsId), "",
                        repsStringBuilder.toString(), weightStringBuilder.toString());
            } else {
                dbHelper.updateExerciseDetails(Integer.parseInt(detailsId), String.valueOf(repList.size()),
                        repsStringBuilder.toString(), weightStringBuilder.toString());
            }

            Intent intent = new Intent(v.getContext(), StartWorkoutPage.class);
            intent.putExtra("WorkoutName", workoutName);
            intent.putExtra("Position", position);
            v.getContext().startActivity(intent);
        });

        backBtn.setOnClickListener(v -> {
            Intent intent3 = new Intent(ChosenExercise.this, StartWorkoutPage.class);
            intent3.putExtra("Position", position);
            startActivity(intent3);
        });
    }

}