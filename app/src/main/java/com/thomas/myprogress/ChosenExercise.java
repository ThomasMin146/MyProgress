package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thomas.myprogress.adapters.ExerciseDetailsRVAdapter;
import com.thomas.myprogress.models.ExerciseDetails;
import com.thomas.myprogress.models.ExerciseInfo;

import java.util.ArrayList;

public class ChosenExercise extends AppCompatActivity implements RVInterface{
    private Button addSetButton, saveSetButton;
    RecyclerView rvLayout;
    //ArrayList<ExerciseInfo> exerciseInfo;
    ArrayList<String> repList, weightList;
    DataBaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosen_exercise);

        TextView textView = findViewById(R.id.exerciseName);

        dbHelper = new DataBaseHelper(this);
        rvLayout = findViewById(R.id.rvLayout);

        String workoutName = getIntent().getStringExtra("WorkoutName");
        String name = getIntent().getStringExtra("Name");
        String detailsId = getIntent().getStringExtra("ID");
        String reps = getIntent().getStringExtra("Reps");
        String sets = getIntent().getStringExtra("Sets");
        String weight = getIntent().getStringExtra("Weight");

        String[] repsArray = reps.split(", ");
        String[] weightArray = weight.split(", ");


        addSetButton = findViewById(R.id.addSetButton);
        saveSetButton = findViewById(R.id.saveSetButton);

        repList = new ArrayList<>();
        weightList = new ArrayList<>();

        textView.setText(name);

        if(!sets.equals("")){
            for(int j = 0; j < Integer.valueOf(sets); j++){
                repList.add(repsArray[j]);
                weightList.add(weightArray[j]);

            }
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

            String reps1 = repsStringBuilder.toString();

            StringBuilder weightStringBuilder = new StringBuilder();
            int weightSize = weightList.size();

            for (int i = 0; i < weightSize; i++) {
                weightStringBuilder.append(weightList.get(i));

                if (i != weightSize - 1) {
                    weightStringBuilder.append(", ");
                }
            }

            String weight1 = weightStringBuilder.toString();


            dbHelper.updateExerciseDetails(Integer.valueOf(detailsId), String.valueOf(repList.size()), reps1, weight1);

            Intent intent = new Intent(v.getContext(), StartWorkoutPage.class);

            intent.putExtra("WorkoutName", workoutName);

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onAddItemClick(int position) {

    }
}