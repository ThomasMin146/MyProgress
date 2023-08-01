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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.thomas.myprogress.adapters.GraphSpinnerAdapter;
import com.thomas.myprogress.chart.CustomXAxisValueFormatter;
import com.thomas.myprogress.models.Exercise;
import com.thomas.myprogress.models.ExerciseDetails;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExerciseGraph extends AppCompatActivity {
    Spinner exercise;
    String selectedExerciseOption;
    DataBaseHelper dbHelper;
    TextView repsChart, weightChart, timeChart, monthsChart, yearsChart, daysChart;
    LineChart chart;
    List<Entry> entries;
    ArrayList<Exercise> exercises;
    ArrayList<ExerciseDetails> exerciseDetails;
    ArrayList<String> xLabels;
    ArrayList<String> exerciseNames;
    XAxis xAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_graph);

        dbHelper = new DataBaseHelper(this);

        exercise = findViewById(R.id.exercise);

        repsChart = findViewById(R.id.repsChart);
        weightChart = findViewById(R.id.weightChart);
        timeChart = findViewById(R.id.timeChart);

        yearsChart = findViewById(R.id.yearsChart);
        monthsChart = findViewById(R.id.monthsChart);
        daysChart = findViewById(R.id.daysChart);

        chart = findViewById(R.id.graph); //assign chart from layout

        setupSpinner();

        entries = new ArrayList<>();
        exerciseDetails = new ArrayList<>();
        xLabels = new ArrayList<>();

        // Customize chart
        chart.setDescription(null);
        chart.setDrawBorders(true);
        chart.getLegend().setEnabled(false);
        chart.setViewPortOffsets(150f, 125f, 75f, 125f);

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
        xAxis = chart.getXAxis();
        xAxis.setTextSize(20f); // Set text size for the X-axis labels
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
            repsAttribute();
        });

        weightChart.setOnClickListener(v -> {
            weightAttribute();
        });

        timeChart.setOnClickListener(v -> {
            timeAttribute();
        });

        daysChart.setOnClickListener(v -> {
            daysAttribute();
        });

        monthsChart.setOnClickListener(v -> {
            monthsAttribute();
        });

        yearsChart.setOnClickListener(v -> {
            yearsAttribute();
        });

        exercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle the selected item
                if (position != 0) {
                    // Update the selectedOption variable with the selected string
                    selectedExerciseOption = exerciseNames.get(position);
                    repsAttribute();
                    daysAttribute();

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

    public void repsAttribute(){
        repsChart.setTextColor(Color.RED);
        repsChart.setBackgroundResource(R.drawable.textview_frame_red);

        weightChart.setTextColor(Color.BLACK);
        weightChart.setBackgroundResource(R.drawable.textview_frame_black);

        timeChart.setTextColor(Color.BLACK);
        timeChart.setBackgroundResource(R.drawable.textview_frame_black);

        if(selectedExerciseOption != null && !selectedExerciseOption.equals("Select an exercise")){
            entries.clear();
            exerciseDetails.clear();
            xLabels.clear();

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
                    }

                }

                String dateee = dbHelper.getWorkoutDate(exerciseDetail.getWorkoutId());
                String[] parts = dateee.split(" ");
                xLabels.add(parts[0]);

                entries.add(new Entry(j,totalreps));
                j++;
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

            xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));

            // Refresh chart
            chart.invalidate();

        }

    }

    public void weightAttribute(){
        weightChart.setTextColor(Color.RED);
        weightChart.setBackgroundResource(R.drawable.textview_frame_red);

        repsChart.setTextColor(Color.BLACK);
        repsChart.setBackgroundResource(R.drawable.textview_frame_black);

        timeChart.setTextColor(Color.BLACK);
        timeChart.setBackgroundResource(R.drawable.textview_frame_black);

        if(selectedExerciseOption != null && !selectedExerciseOption.equals("Select an exercise")){
            entries.clear();
            exerciseDetails.clear();
            xLabels.clear();

            for(Exercise exercise:exercises){
                if(exercise.getName().equals(selectedExerciseOption)){
                    exerciseDetails = dbHelper.getExerciseDetailsByExerciseId(exercise.getId());
                }

            }

            int j = 0;

            for(ExerciseDetails exerciseDetail:exerciseDetails){
                String[] getWeightArray = exerciseDetail.getWeight().split(",");
                int maxweight = 0;

                for(int i = 0; i < getWeightArray.length; i++){
                    if(getWeightArray[i].trim().equals("")){

                    } else {
                        if(maxweight < Integer.valueOf(getWeightArray[i].trim())){
                            maxweight = Integer.valueOf(getWeightArray[i].trim());
                        }
                    }

                }

                String dateee = dbHelper.getWorkoutDate(exerciseDetail.getWorkoutId());
                String[] parts = dateee.split(" ");
                xLabels.add(parts[0]);

                entries.add(new Entry(j,maxweight));
                j++;
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

            xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));

            // Refresh chart
            chart.invalidate();

        }

    }

    public void timeAttribute(){
        timeChart.setTextColor(Color.RED);
        timeChart.setBackgroundResource(R.drawable.textview_frame_red);

        repsChart.setTextColor(Color.BLACK);
        repsChart.setBackgroundResource(R.drawable.textview_frame_black);

        weightChart.setTextColor(Color.BLACK);
        weightChart.setBackgroundResource(R.drawable.textview_frame_black);

        if(selectedExerciseOption != null && !selectedExerciseOption.equals("Select an exercise")){
            entries.clear();
            exerciseDetails.clear();
            xLabels.clear();

            for(Exercise exercise:exercises){
                if(exercise.getName().equals(selectedExerciseOption)){
                    exerciseDetails = dbHelper.getExerciseDetailsByExerciseId(exercise.getId());
                }

            }

            int j = 0;

            for(ExerciseDetails exerciseDetail:exerciseDetails){

                int duration = dbHelper.getWorkoutDuration(exerciseDetail.getWorkoutId());
                int durationInMinutes = duration / (1000 * 60);

                String dateee = dbHelper.getWorkoutDate(exerciseDetail.getWorkoutId());
                String[] parts = dateee.split(" ");
                xLabels.add(parts[0]);


                entries.add(new Entry(j,durationInMinutes));
                j++;
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

            xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));

            // Refresh chart
            chart.invalidate();

        }

    }

    private void daysAttribute(){
        daysChart.setTextColor(Color.RED);
        daysChart.setBackgroundResource(R.drawable.textview_frame_red);

        monthsChart.setTextColor(Color.BLACK);
        monthsChart.setBackgroundResource(R.drawable.textview_frame_black);

        yearsChart.setTextColor(Color.BLACK);
        yearsChart.setBackgroundResource(R.drawable.textview_frame_black);

        if(selectedExerciseOption != null && !selectedExerciseOption.equals("Select an exercise")){
            entries.clear();
            exerciseDetails.clear();
            xLabels.clear();

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
                    }

                }

                String dateee = dbHelper.getWorkoutDate(exerciseDetail.getWorkoutId());
                String[] parts = dateee.split(" ");
                xLabels.add(parts[0]);

                entries.add(new Entry(j,totalreps));
                j++;
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

            xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));

            // Refresh chart
            chart.invalidate();

        }
    }

    private void monthsAttribute(){
        monthsChart.setTextColor(Color.RED);
        monthsChart.setBackgroundResource(R.drawable.textview_frame_red);

        daysChart.setTextColor(Color.BLACK);
        daysChart.setBackgroundResource(R.drawable.textview_frame_black);

        yearsChart.setTextColor(Color.BLACK);
        yearsChart.setBackgroundResource(R.drawable.textview_frame_black);

        if(selectedExerciseOption != null && !selectedExerciseOption.equals("Select an exercise")){
            entries.clear();
            exerciseDetails.clear();
            xLabels.clear();

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
                    }

                }

                String dateee = dbHelper.getWorkoutDate(exerciseDetail.getWorkoutId());
                String[] parts = dateee.split(" ");
                xLabels.add(parts[1]);

                entries.add(new Entry(j,totalreps));
                j++;
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

            xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));

            // Refresh chart
            chart.invalidate();

        }
    }

    private void yearsAttribute(){
        yearsChart.setTextColor(Color.RED);
        yearsChart.setBackgroundResource(R.drawable.textview_frame_red);

        daysChart.setTextColor(Color.BLACK);
        daysChart.setBackgroundResource(R.drawable.textview_frame_black);

        monthsChart.setTextColor(Color.BLACK);
        monthsChart.setBackgroundResource(R.drawable.textview_frame_black);

        if(selectedExerciseOption != null && !selectedExerciseOption.equals("Select an exercise")){
            entries.clear();
            exerciseDetails.clear();
            xLabels.clear();

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
                    }

                }

                String dateee = dbHelper.getWorkoutDate(exerciseDetail.getWorkoutId());
                String[] parts = dateee.split(" ");
                xLabels.add(parts[2]);

                entries.add(new Entry(j,totalreps));
                j++;
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

            xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));

            // Refresh chart
            chart.invalidate();

        }
    }

    private void setupSpinner(){
        exercises = dbHelper.getAllExercises();
        exerciseNames = new ArrayList<>();
        exerciseNames.add("Select an exercise");

        for (Exercise exercise : exercises) {
            exerciseNames.add(exercise.getName());
        }

        GraphSpinnerAdapter exerciseAdapter = new GraphSpinnerAdapter
                (this, android.R.layout.simple_spinner_item, exerciseNames);

        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exercise.setAdapter(exerciseAdapter);
        exercise.setSelection(0, false);

    }

}