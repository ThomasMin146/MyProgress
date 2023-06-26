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
import android.util.Pair;
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

public class ChosenExercise2 extends AppCompatActivity {
    private Button saveSetButton;
    EditText repsET1, repsET2, repsET3, repsET4, repsET5, repsET6;
    EditText weightET1, weightET2, weightET3, weightET4, weightET5, weightET6;
    ArrayList<String> repsList;
    DataBaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosen_exercise2);

        String name = getIntent().getStringExtra("Name");
        TextView textView = findViewById(R.id.exerciseName);

        dbHelper = new DataBaseHelper(this);

        repsET1 = findViewById(R.id.repsET1);
        repsET2 = findViewById(R.id.repsET2);
        repsET3 = findViewById(R.id.repsET3);
        repsET4 = findViewById(R.id.repsET4);
        repsET5 = findViewById(R.id.repsET5);
        repsET6 = findViewById(R.id.repsET6);

        weightET1 = findViewById(R.id.weightET1);
        weightET2 = findViewById(R.id.weightET2);
        weightET3 = findViewById(R.id.weightET3);
        weightET4 = findViewById(R.id.weightET4);
        weightET5 = findViewById(R.id.weightET5);
        weightET6 = findViewById(R.id.weightET6);

        repsList = new ArrayList<>();

        saveSetButton = findViewById(R.id.saveSetButton);

        textView.setText(name);


        saveSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int workoutid = getIntent().getIntExtra("ID", -1);

                String reps = repsET1.getText() + " " + repsET2.getText() + " " + repsET3.getText() + " " +
                        repsET4.getText() + " " +  repsET5.getText() + " " + repsET6.getText();

                String weight = weightET1.getText() + " " + weightET2.getText() + " " + weightET3.getText() + " " +
                        weightET4.getText() + " " +  weightET5.getText() + " " + weightET6.getText();

                if(!repsET1.getText().toString().isEmpty()){
                    repsList.add(repsET1.getText().toString());
                }
                if(!repsET2.getText().toString().isEmpty()){
                    repsList.add(repsET2.getText().toString());
                }
                if(!repsET3.getText().toString().isEmpty()){
                    repsList.add(repsET3.getText().toString());
                }
                if(!repsET4.getText().toString().isEmpty()){
                    repsList.add(repsET4.getText().toString());
                }
                if(!repsET5.getText().toString().isEmpty()){
                    repsList.add(repsET5.getText().toString());
                }
                if(!repsET6.getText().toString().isEmpty()){
                    repsList.add(repsET6.getText().toString());
                }

                dbHelper.updateMyWorkoutColumn(workoutid, "Exercise_sets", String.valueOf(repsList.size()));
                dbHelper.updateMyWorkoutColumn(workoutid, "Exercise_reps", reps);
                dbHelper.updateMyWorkoutColumn(workoutid, "Exercise_weight", weight);
                Intent intent = new Intent(v.getContext(), StartWorkoutPage.class);

                v.getContext().startActivity(intent);
            }
        });
    }

}
