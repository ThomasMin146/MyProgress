package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thomas.myprogress.dbhelper.DataBaseHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChosenExercise extends AppCompatActivity {
    private Button addSetButton, saveSetButton;
    RecyclerView rvLayout;
    ArrayList<ItemExercise> exerciseModels;
    ArrayList<EditText> editTextList;
    private int setCounter;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosen_exercise);

        String name = getIntent().getStringExtra("Name");
        TextView textView = findViewById(R.id.exerciseName);

        dbHelper = new DataBaseHelper(this);
        rvLayout = findViewById(R.id.rvLayout);

        setCounter = 1;


        addSetButton = findViewById(R.id.addSetButton);
        saveSetButton = findViewById(R.id.saveSetButton);
        editTextList = new ArrayList<>();

        exerciseModels = new ArrayList<>();

        textView.setText(name);
        exerciseModels.add(new ItemExercise(setCounter));

        CustomAdapter adapter = new CustomAdapter(this, exerciseModels);
        rvLayout.setAdapter(adapter);
        rvLayout.setLayoutManager(new LinearLayoutManager(this));


        addSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCounter++;
                exerciseModels.add(new ItemExercise(setCounter));
                adapter.notifyItemInserted(exerciseModels.size()-1);

            }
        });


        saveSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dbHelper = new DataBaseHelper(v.getContext());


                int position = getIntent().getIntExtra("position", -1);

                int workoutid = getIntent().getIntExtra("ID", -1);

                dbHelper.updateMyWorkoutColumn(workoutid, "Exercise_sets",
                        String.valueOf(adapter.getExerciseModels().get(0).getReps()));


                Intent intent = new Intent(v.getContext(), StartWorkoutPage.class);


                v.getContext().startActivity(intent);
            }
        });
    }

}