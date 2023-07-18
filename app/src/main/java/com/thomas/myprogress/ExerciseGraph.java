package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.thomas.myprogress.adapters.GraphSpinnerAdapter;
import com.thomas.myprogress.models.Exercise;
import com.thomas.myprogress.models.ExerciseDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExerciseGraph extends AppCompatActivity {
    Spinner exercise;
    String selectedExerciseOption;
    DataBaseHelper dbHelper;

    ArrayList<ExerciseDetails> exerciseDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_graph);

        dbHelper = new DataBaseHelper(this);

        exercise = findViewById(R.id.exercise);
        LineChart chart = findViewById(R.id.graph);


        ArrayList<Exercise> exercises = dbHelper.getAllExercises();


        ArrayList<String> exerciseNames = new ArrayList<>();
        exerciseNames.add("Select an exercise");

        for (Exercise exercise : exercises) {
            exerciseNames.add(exercise.getName());
        }

        GraphSpinnerAdapter exerciseAdapter = new GraphSpinnerAdapter
                (this, android.R.layout.simple_spinner_item, exerciseNames);

        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        exercise.setAdapter(exerciseAdapter);

        exercise.setSelection(0, false);

        // Customize chart
        chart.setDescription(null);
        chart.setDrawBorders(true);

        // Create dummy data
        List<Entry> entries = new ArrayList<>();

        LineDataSet dataSet = new LineDataSet(entries, "Sample Data");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.getLegend().setEnabled(false);


        // Customize Y-axis labels
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextSize(20f); // Set text size for the left Y-axis labels
        leftAxis.setEnabled(true);
        leftAxis.setDrawLabels(true);
        leftAxis.setAxisMinimum(0);
        leftAxis.setSpaceTop(0);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setGranularity(1);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setDrawGridLines(false);


        // Hide and disable the right Y-axis
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        // Customize X-axis labels
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(20f); // Set text size for the X-axis labels
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(true);
        xAxis.setAxisMinimum(0);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);




        /*xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Calculate the relative time from the current date
                long currentDate = System.currentTimeMillis();
                long dateValue = (long) value;

                long diff = currentDate - dateValue;
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;
                long months = days / 30;
                long years = months / 12;

                if (months > 0) {
                    return months + "m";
                } else if (years > 0) {
                    return years + "y";
                } else {
                    return days + "d";
                }
            }
        });*/

        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        // Refresh chart
        chart.invalidate();

        exercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle the selected item
                if (position != 0) {
                    // Update the selectedOption variable with the selected string
                    selectedExerciseOption = exerciseNames.get(position);

                    for(Exercise exercise:exercises){
                        if(exercise.getName().equals(selectedExerciseOption)){
                            exerciseDetails = dbHelper.getExerciseDetailsByExerciseId(exercise.getId());
                        }

                    }

                    for(ExerciseDetails exerciseDetail:exerciseDetails){
                        int j = 0;
                        String[] getRepsArray = exerciseDetail.getReps().split(",");
                        int totalreps = 0;

                        for(int i = 0; i < getRepsArray.length; i++){
                            if(getRepsArray[i].trim().equals("")){
                                totalreps = totalreps + 0;
                            } else {
                                totalreps = totalreps + Integer.parseInt(getRepsArray[i].trim());
                            }

                        }
                        j++;
                        entries.add(new Entry(j,totalreps));

                    }

                    // Populate a different dataset based on the selected exercise
                    //List<Entry> entries = new ArrayList<>();
                    // Add your logic here to populate the entries based on the selected exercise

                    /*entries.add(new Entry(1,1));
                    entries.add(new Entry(2,2));
                    entries.add(new Entry(3,3));
                    entries.add(new Entry(4,4));
                    entries.add(new Entry(5,5));*/

                    LineDataSet dataSet = new LineDataSet(entries, "");
                    LineData lineData = new LineData(dataSet);
                    chart.setData(lineData);
                    dataSet.setLineWidth(8);
                    dataSet.setValueTextSize(20);

                    dataSet.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return String.format(Locale.getDefault(), "%.0f", value);
                        }
                    });

                    // Refresh chart
                    chart.invalidate();

                } else {
                    // Handle the case when the initial selection is made
                    selectedExerciseOption = null;
                }
                // Do something with the selected option
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
                selectedExerciseOption = null;
            }
        });



    }

}