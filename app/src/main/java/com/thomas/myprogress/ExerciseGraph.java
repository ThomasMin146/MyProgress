package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.thomas.myprogress.chart.CustomXAxisValueFormatter;
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
    TextView repsChart, weightChart, timeChart, allChart, yearChart, months6Chart, months3Chart, months1Chart;
    LineChart chart;
    List<Entry> entries;
    ArrayList<Exercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_graph);

        dbHelper = new DataBaseHelper(this);

        exercise = findViewById(R.id.exercise);

        repsChart = findViewById(R.id.repsChart);
        weightChart = findViewById(R.id.weightChart);
        timeChart = findViewById(R.id.timeChart);
        allChart = findViewById(R.id.allChart);
        yearChart = findViewById(R.id.yearChart);
        months6Chart = findViewById(R.id.months6Chart);
        months3Chart = findViewById(R.id.months3Chart);
        months1Chart = findViewById(R.id.months1Chart);

        chart = findViewById(R.id.graph);

        exercises = dbHelper.getAllExercises();

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
        entries = new ArrayList<>();

        chart.getLegend().setEnabled(false);
        chart.setViewPortOffsets(100f, 125f, 100f, 125f);

        String[] timeIntervals = {"Last Month", "Last 3 Months", "Last 6 Months", "Last Year"};
        CustomXAxisValueFormatter xAxisValueFormatter = new CustomXAxisValueFormatter(timeIntervals);


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
        //xAxis.setValueFormatter(xAxisValueFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(true);
        xAxis.setAxisMinimum(0);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);

        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        repsChart.setOnClickListener(v -> {
            chosenAttribute(repsChart);
        });

        exercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle the selected item
                if (position != 0) {
                    // Update the selectedOption variable with the selected string
                    selectedExerciseOption = exerciseNames.get(position);
                    chart.clear();
                    entries.clear();
                    ArrayList<ExerciseDetails> exerciseDetails = new ArrayList<>();
                    ArrayList<Integer> xValues = new ArrayList<>();

                    for(Exercise exercise:exercises){
                        if(exercise.getName().equals(selectedExerciseOption)){
                            exerciseDetails = dbHelper.getExerciseDetailsByExerciseId(exercise.getId());

                        }

                    }
                    int j = 0;

                    for(ExerciseDetails exerciseDetail:exerciseDetails){

                        String[] getRepsArray = exerciseDetail.getReps().split(",");
                        int totalreps = 0;

                        for(int i = 0; i < getRepsArray.length; i++){
                            if(getRepsArray[i].trim().equals("")){
                                totalreps = totalreps + 0;
                            } else {
                                totalreps = totalreps + Integer.parseInt(getRepsArray[i].trim());
                                Log.d("test", dbHelper.getWorkoutDate(exerciseDetail.getWorkoutId()));
                            }

                        }
                        j++;
                        String dateee = dbHelper.getWorkoutDate(exerciseDetail.getWorkoutId());
                        String[] parts = dateee.split(" ");

                        entries.add(new Entry(Integer.valueOf(parts[0]),totalreps));

                    }

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

    public void chosenAttribute(TextView textView){
        textView.setTextColor(Color.RED);
        textView.setBackgroundResource(R.drawable.textview_frame_red);

        if(selectedExerciseOption != null && !selectedExerciseOption.equals("Select an exercise")){
            chart.clear();
            entries.clear();
            ArrayList<ExerciseDetails> exerciseDetails = new ArrayList<>();

            for(Exercise exercise:exercises){
                if(exercise.getName().equals(selectedExerciseOption)){
                    exerciseDetails = dbHelper.getExerciseDetailsByExerciseId(exercise.getId());

                }

            }
            int j = 0;

            for(ExerciseDetails exerciseDetail:exerciseDetails){

                String[] getRepsArray = exerciseDetail.getReps().split(",");
                int totalreps = 0;

                for(int i = 0; i < getRepsArray.length; i++){
                    if(getRepsArray[i].trim().equals("")){
                        totalreps = totalreps + 0;
                    } else {
                        totalreps = totalreps + Integer.parseInt(getRepsArray[i].trim());
                        Log.d("test", dbHelper.getWorkoutDate(exerciseDetail.getWorkoutId()));
                    }

                }
                j++;
                String dateee = dbHelper.getWorkoutDate(exerciseDetail.getWorkoutId());
                String[] parts = dateee.split(" ");

                entries.add(new Entry(Integer.valueOf(parts[0]),totalreps));

            }

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

            chart.invalidate(); // Refresh chart

        }

    }

}