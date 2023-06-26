package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thomas.myprogress.dbhelper.DataBaseHelper;
import com.thomas.myprogress.dbhelper.MyWorkout;

import java.util.ArrayList;
import java.util.Locale;

public class StartWorkoutPage extends AppCompatActivity implements RVInterface{
    DataBaseHelper dbHelper;
    Stopwatch stopwatch;
    TextView minTime;
    private Button playButton, resumeButton, setButton, pauseButton, stopButton, addButton;
    ArrayList<ItemExercise> test;
    RecyclerView exerciseRecyclerView;
    ExerciseAdapter exerciseAdapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_workout_page);

        dbHelper = new DataBaseHelper(this);
        stopwatch = new Stopwatch();


        test = new ArrayList<>();

        exerciseRecyclerView = findViewById(R.id.exerciseRecyclerView);
        addButton = findViewById(R.id.addExercise);
        minTime = findViewById(R.id.minTime);

        playButton = findViewById(R.id.playButton);
        resumeButton = findViewById(R.id.resumeButton);
        stopButton = findViewById(R.id.stopButton);
        pauseButton = findViewById(R.id.pauseButton);
        setButton = findViewById(R.id.setButton);

        db = dbHelper.getWritableDatabase();

        //setUpExerciseItems();

        exerciseAdapter = new ExerciseAdapter(this, test, this);
        exerciseRecyclerView.setAdapter(exerciseAdapter);
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // database operations
        Cursor cursor = db.query("MyWorkout", null, null, null, null, null, null);
        while (cursor.moveToNext()) {

            ItemExercise itemExercise = new ItemExercise();
            itemExercise.setId(cursor.getInt(cursor.getColumnIndexOrThrow("MyWorkout_id")));

            itemExercise.setName(cursor.getString(cursor.getColumnIndexOrThrow("Exercise_name")));

            if(cursor.getString(cursor.getColumnIndexOrThrow("Exercise_reps"))==null){
                itemExercise.setReps(" ");
            } else {
                itemExercise.setReps(cursor.getString(cursor.getColumnIndexOrThrow("Exercise_reps")));
            }

            if(cursor.getString(cursor.getColumnIndexOrThrow("Exercise_sets"))==null){
                itemExercise.setSets(" ");
            } else {
                itemExercise.setSets(cursor.getString(cursor.getColumnIndexOrThrow("Exercise_sets")));
            }

            if(cursor.getString(cursor.getColumnIndexOrThrow("Exercise_weight"))==null){
                itemExercise.setWeight(" ");
            } else {
                itemExercise.setWeight(cursor.getString(cursor.getColumnIndexOrThrow("Exercise_weight")));
            }

            //itemExercise.setReps(cursor.getString(cursor.getColumnIndexOrThrow("Exercise_reps")));
            test.add(itemExercise);


        }
        cursor.close();
        //exerciseAdapter.notifyDataSetChanged();
        //addButton.setText(String.valueOf(exerciseAdapter.getItemCount()));



        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (stopwatch.isRunning()) {
                    long elapsedMillis = stopwatch.getElapsedTime();
                    String formattedTime = formatTime(elapsedMillis);
                    minTime.setText(formattedTime);
                }
                handler.postDelayed(this, 10); // Update every 100 milliseconds
            }
        };
        handler.postDelayed(runnable, 10);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopwatch.start();
                playButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
                setButton.setVisibility(View.VISIBLE);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopwatch.pause();
                setButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
                resumeButton.setVisibility(View.VISIBLE);

            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopwatch.resume();
                stopButton.setVisibility(View.GONE);
                resumeButton.setVisibility(View.GONE);
                setButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.VISIBLE);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopwatch.reset();
                minTime.setText("00 : 00 . 00");
                stopButton.setVisibility(View.GONE);
                resumeButton.setVisibility(View.GONE);
                playButton.setVisibility(View.VISIBLE);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartWorkoutPage.this, AddExercise.class);
                startActivity(intent);
            }
        });

    }

    private String formatTime(long elapsedMillis) {
        int hours = (int) (elapsedMillis / (1000 * 60 * 60));
        int minutes = (int) (elapsedMillis % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) (elapsedMillis % (1000 * 60)) / 1000;
        int milliseconds = (int) (elapsedMillis % 1000);

        return String.format(Locale.getDefault(), "%02d : %02d . %02d", minutes, seconds, milliseconds/10);
    }

    /*private void setUpExerciseItems(){
        String[] exerciseNames = {"Pushups", "Pullups", "Dips", "Squats", "L-sit"};
        int[] exerciseReps = {15, 10, 10, 25, 20};
        int[] exerciseSets = {15, 10, 10, 25, 20};
        int[] exerciseWeight = {15, 10, 10, 25, 20};


        for (int i = 0; i<exerciseNames.length; i++){
            exerciseItems.add(new ItemExercise(exerciseNames[i], exerciseReps[i], exerciseSets[i], exerciseWeight[i]));
        }
    }*/

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(StartWorkoutPage.this, ChosenExercise2.class);
        intent.putExtra("Name", test.get(position).getName());
        intent.putExtra("ID", test.get(position).getId());
        intent.putExtra("position", position);

        startActivity(intent);


    }
}