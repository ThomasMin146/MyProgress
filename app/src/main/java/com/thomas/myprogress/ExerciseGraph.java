package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.thomas.myprogress.adapters.GraphSpinnerAdapter;
import com.thomas.myprogress.models.Exercise;
import com.thomas.myprogress.models.ExerciseDetails;
import com.thomas.myprogress.models.Workout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExerciseGraph extends AppCompatActivity {
    Spinner exercise;
    String selectedExerciseOption, typeOfData, timeSpan;
    DataBaseHelper dbHelper;
    TextView repsChart, weightChart, timeChart, monthsChart, yearsChart, daysChart, backBtn;
    LineChart chart;
    List<Entry> entries;
    ArrayList<Exercise> exercises;
    ArrayList<ExerciseDetails> exerciseDetails;
    ArrayList<Workout> workouts;
    ArrayList<String> xLabels, data;
    ArrayList<String> exerciseNames;
    XAxis xAxis;
    int labelPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_graph);

        dbHelper = new DataBaseHelper(this);
        typeOfData = "reps";
        timeSpan = "Month";
        labelPart = 2;

        exercises = dbHelper.getAllExercises();
        workouts = dbHelper.getAllWorkouts();
        exerciseDetails = dbHelper.getAllExerciseDetails();

        exercise = findViewById(R.id.exercise);

        repsChart = findViewById(R.id.repsChart);
        weightChart = findViewById(R.id.weightChart);
        timeChart = findViewById(R.id.timeChart);

        yearsChart = findViewById(R.id.yearsChart);
        monthsChart = findViewById(R.id.monthsChart);
        daysChart = findViewById(R.id.daysChart);

        backBtn = findViewById(R.id.backButton);
        chart = findViewById(R.id.graph); //assign chart from layout

        //Setup spinner
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

        entries = new ArrayList<>();
        xLabels = new ArrayList<>();
        data = new ArrayList<>();

        // Customize chart
        chart.setDescription(null);
        chart.setDrawBorders(true);
        chart.setBorderWidth(4f);
        chart.getLegend().setEnabled(false);
        chart.getXAxis().setLabelCount(4);
        chart.setViewPortOffsets(150f, 125f, 75f, 125f);

        // Customize Y-axis labels
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextSize(25f); // Set text size for the left Y-axis labels

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
        xAxis.setTextSize(25f); // Set text size for the X-axis labels
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

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ExerciseGraph.this, HomePage.class);
            startActivity(intent);
        });

        repsChart.setOnClickListener(v -> {
            data.clear();
            entries.clear();
            xLabels.clear();

            typeOfData = "reps";

            if(selectedExerciseOption != null && !selectedExerciseOption.equals("Select an exercise")){
                repsAttribute();

                Date currentDate = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);

                switch (timeSpan) {
                    case "Month":
                        daysAttribute();
                        calendar.add(Calendar.MONTH, -1);
                        labelPart = 2;
                        break;
                    case "Year":
                        monthsAttribute();
                        calendar.add(Calendar.MONTH, -12);
                        labelPart = 1;
                        break;
                    case "All":
                        yearsAttribute();
                        calendar.add(Calendar.YEAR, -100);
                        labelPart = 0;
                        break;
                    default:
                }

                Date previousDate = calendar.getTime();
                ArrayList<Integer> workoutIDs = new ArrayList<>();

                for(Workout workout:workouts){
                    if(workout.getDate().after(previousDate) || workout.getDate().equals(previousDate)){
                        workoutIDs.add(workout.getId());
                    }
                }

                for(ExerciseDetails exerciseDetail : exerciseDetails){
                    if(exerciseDetail.getExerciseId() == dbHelper.getExerciseIdByName(selectedExerciseOption)){
                        for(Integer workoutID: workoutIDs){
                            if(exerciseDetail.getWorkoutId() == workoutID){
                                data.add(exerciseDetail.getReps());
                            }
                        }
                    }
                }

                for(int i = 0; i < data.size(); i++){
                    switch (typeOfData) {
                        case "reps":
                            entries.add(new Entry(i, getTotalReps(data.get(i))));
                            break;
                        case "weight":
                            entries.add(new Entry(i, getMaximumWeight(data.get(i))));
                            break;
                        case "time":
                            entries.add(new Entry(i, getWorkoutDuration(data.get(i))));
                            break;
                    }
                }

                LineDataSet dataSet = new LineDataSet(entries, "");
                LineData lineData = new LineData(dataSet);
                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet.setColor(Color.parseColor("#DB4437"));
                dataSet.setLineWidth(8);
                dataSet.setValueTextSize(25);
                dataSet.setValueTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                chart.setData(lineData);


                dataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.format(Locale.getDefault(), "%.0f", value);
                    }
                });

                chart.invalidate();
            }
        });

        weightChart.setOnClickListener(v -> {
            data.clear();
            entries.clear();
            xLabels.clear();
            typeOfData = "weight";


            if(selectedExerciseOption != null && !selectedExerciseOption.equals("Select an exercise")) {
                weightAttribute();

                Date currentDate = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);

                switch (timeSpan) {
                    case "Month":
                        daysAttribute();
                        calendar.add(Calendar.MONTH, -1);
                        labelPart = 2;
                        break;
                    case "Year":
                        monthsAttribute();
                        calendar.add(Calendar.MONTH, -12);
                        labelPart = 1;
                        break;
                    case "All":
                        yearsAttribute();
                        calendar.add(Calendar.YEAR, -100);
                        labelPart = 0;
                        break;
                    default:
                }

                Date previousDate = calendar.getTime();
                ArrayList<Integer> workoutIDs = new ArrayList<>();

                for(Workout workout:workouts){
                    if(workout.getDate().after(previousDate) || workout.getDate().equals(previousDate)){
                        workoutIDs.add(workout.getId());
                    }
                }

                for (ExerciseDetails exerciseDetail : exerciseDetails) {
                    if (exerciseDetail.getExerciseId() == dbHelper.getExerciseIdByName(selectedExerciseOption)){
                        for(Integer workoutID: workoutIDs){
                            if(exerciseDetail.getWorkoutId() == workoutID){
                                data.add(exerciseDetail.getWeight());
                            }
                        }
                    }
                }

                for (int i = 0; i < data.size(); i++) {
                    switch (typeOfData) {
                        case "reps":
                            entries.add(new Entry(i, getTotalReps(data.get(i))));
                            break;
                        case "weight":
                            entries.add(new Entry(i, getMaximumWeight(data.get(i))));
                            break;
                        case "time":
                            entries.add(new Entry(i, getWorkoutDuration(data.get(i))));
                            break;
                    }
                }

                LineDataSet dataSet = new LineDataSet(entries, "");
                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet.setColor(Color.parseColor("#DB4437"));
                dataSet.setLineWidth(8);
                dataSet.setValueTextSize(25);
                dataSet.setValueTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);

                dataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.format(Locale.getDefault(), "%.0f", value);
                    }
                });

                chart.invalidate();
            }
        });

        timeChart.setOnClickListener(v -> {
            data.clear();
            entries.clear();
            xLabels.clear();
            typeOfData = "time";

            if (selectedExerciseOption != null && !selectedExerciseOption.equals("Select an exercise")) {
                timeAttribute();

                Date currentDate = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);

                switch (timeSpan) {
                    case "Month":
                        daysAttribute();
                        calendar.add(Calendar.MONTH, -1);
                        labelPart = 2;
                        break;
                    case "Year":
                        monthsAttribute();
                        calendar.add(Calendar.MONTH, -12);
                        labelPart = 1;
                        break;
                    case "All":
                        yearsAttribute();
                        calendar.add(Calendar.YEAR, -100);
                        labelPart = 0;
                        break;
                    default:
                }

                Date previousDate = calendar.getTime();
                ArrayList<Integer> workoutIDs = new ArrayList<>();

                for(Workout workout:workouts){
                    if(workout.getDate().after(previousDate) || workout.getDate().equals(previousDate)){
                        workoutIDs.add(workout.getId());
                    }
                }

                for(ExerciseDetails exerciseDetail : exerciseDetails){
                    if(exerciseDetail.getExerciseId() == dbHelper.getExerciseIdByName(selectedExerciseOption)){
                        for(Workout workout: workouts){
                            if(exerciseDetail.getWorkoutId() == workout.getId()){
                                for(Integer workoutID: workoutIDs){
                                    if(exerciseDetail.getWorkoutId() == workoutID){
                                        int duration = workout.getRestingTime() + workout.getWorkingTime();
                                        data.add(String.valueOf(duration));
                                    }
                                }
                            }
                        }
                    }
                }

                for (int i = 0; i < data.size(); i++) {
                    switch (typeOfData) {
                        case "reps":
                            entries.add(new Entry(i, getTotalReps(data.get(i))));
                            break;
                        case "weight":
                            entries.add(new Entry(i, getMaximumWeight(data.get(i))));
                            break;
                        case "time":
                            entries.add(new Entry(i, getWorkoutDuration(data.get(i))));
                            break;
                    }
                }

                LineDataSet dataSet = new LineDataSet(entries, "");
                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet.setColor(Color.parseColor("#DB4437"));
                dataSet.setLineWidth(8);
                dataSet.setValueTextSize(25);
                dataSet.setValueTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);


                dataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.format(Locale.getDefault(), "%.0f", value);
                    }
                });

                chart.invalidate();
            }
        });

        daysChart.setOnClickListener(v -> {
            data.clear();
            entries.clear();
            xLabels.clear();
            timeSpan = "Month";
            labelPart = 2;

            if (selectedExerciseOption != null && !selectedExerciseOption.equals("Select an exercise")) {
                daysAttribute();

                Date currentDate = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.MONTH, -1);
                Date previousDate = calendar.getTime();
                ArrayList<Integer> workoutIDs = new ArrayList<>();
                ArrayList<String> workoutDates = new ArrayList<>();

                for(Workout workout:workouts){
                    if(workout.getDate().after(previousDate) || workout.getDate().equals(previousDate)){
                        workoutIDs.add(workout.getId());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
                        String dateString = dateFormat.format(workout.getDate());
                        workoutDates.add(dateString);
                    }
                }

                switch (typeOfData) {
                    case "reps":
                        repsAttribute();
                        for(ExerciseDetails exerciseDetail : exerciseDetails){
                            if(exerciseDetail.getExerciseId() == dbHelper.getExerciseIdByName(selectedExerciseOption)){

                                for(int i = 0; i < workoutIDs.size(); i++){
                                    if(exerciseDetail.getWorkoutId() == workoutIDs.get(i)){
                                        data.add(exerciseDetail.getReps());
                                        String datee = workoutDates.get(i);
                                        String[] parts = datee.split("-");
                                        xLabels.add(parts[labelPart]);
                                    }
                                }
                            }
                        }
                        break;
                    case "weight":
                        weightAttribute();
                        for(ExerciseDetails exerciseDetail : exerciseDetails){
                            if(exerciseDetail.getExerciseId() == dbHelper.getExerciseIdByName(selectedExerciseOption)){

                                for(int i = 0; i < workoutIDs.size(); i++){
                                    if(exerciseDetail.getWorkoutId() == workoutIDs.get(i)){
                                        data.add(exerciseDetail.getWeight());
                                        String datee = workoutDates.get(i);
                                        String[] parts = datee.split("-");
                                        xLabels.add(parts[labelPart]);
                                    }
                                }
                            }
                        }
                        break;
                    case "time":
                        timeAttribute();
                        for(ExerciseDetails exerciseDetail : exerciseDetails){
                            if(exerciseDetail.getExerciseId() == dbHelper.getExerciseIdByName(selectedExerciseOption)){
                                for(Workout workout: workouts){
                                    if(exerciseDetail.getWorkoutId() == workout.getId()){
                                        for(int i = 0; i < workoutIDs.size(); i++){
                                            if(exerciseDetail.getWorkoutId() == workoutIDs.get(i)){
                                                int duration = workout.getRestingTime() + workout.getWorkingTime();
                                                data.add(String.valueOf(duration));
                                                String datee = workoutDates.get(i);
                                                String[] parts = datee.split("-");
                                                xLabels.add(parts[labelPart]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    default:
                }
                for (int i = 0; i < data.size(); i++) {
                    switch (typeOfData) {
                        case "reps":
                            entries.add(new Entry(i, getTotalReps(data.get(i))));
                            break;
                        case "weight":
                            entries.add(new Entry(i, getMaximumWeight(data.get(i))));
                            break;
                        case "time":
                            entries.add(new Entry(i, getWorkoutDuration(data.get(i))));
                            break;
                    }
                }

                LineDataSet dataSet = new LineDataSet(entries, "");
                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet.setColor(Color.parseColor("#DB4437"));
                dataSet.setLineWidth(8);
                dataSet.setValueTextSize(25);
                dataSet.setValueTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);


                dataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.format(Locale.getDefault(), "%.0f", value);
                    }
                });

                xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
                chart.invalidate();
            }
        });

        monthsChart.setOnClickListener(v -> {
            data.clear();
            entries.clear();
            xLabels.clear();
            timeSpan = "Year";
            labelPart = 1;

            if (selectedExerciseOption != null && !selectedExerciseOption.equals("Select an exercise")) {
                monthsAttribute();

                Date currentDate = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.MONTH, -12);
                Date previousDate = calendar.getTime();
                ArrayList<Integer> workoutIDs = new ArrayList<>();
                ArrayList<String> workoutDates = new ArrayList<>();

                for(Workout workout:workouts){
                    if(workout.getDate().after(previousDate) || workout.getDate().equals(previousDate)){
                        workoutIDs.add(workout.getId());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
                        String dateString = dateFormat.format(workout.getDate());
                        workoutDates.add(dateString);
                    }
                }

                switch (typeOfData) {
                    case "reps":
                        repsAttribute();
                        for(ExerciseDetails exerciseDetail : exerciseDetails){
                            if(exerciseDetail.getExerciseId() == dbHelper.getExerciseIdByName(selectedExerciseOption)){

                                for(int i = 0; i < workoutIDs.size(); i++){
                                    if(exerciseDetail.getWorkoutId() == workoutIDs.get(i)){
                                        data.add(exerciseDetail.getReps());
                                        String datee = workoutDates.get(i);
                                        String[] parts = datee.split("-");
                                        xLabels.add(parts[labelPart]);
                                    }
                                }
                            }
                        }
                        break;
                    case "weight":
                        weightAttribute();
                        for(ExerciseDetails exerciseDetail : exerciseDetails){
                            if(exerciseDetail.getExerciseId() == dbHelper.getExerciseIdByName(selectedExerciseOption)){

                                for(int i = 0; i < workoutIDs.size(); i++){
                                    if(exerciseDetail.getWorkoutId() == workoutIDs.get(i)){
                                        data.add(exerciseDetail.getWeight());
                                        String datee = workoutDates.get(i);
                                        String[] parts = datee.split("-");
                                        xLabels.add(parts[labelPart]);
                                    }
                                }
                            }
                        }
                        break;
                    case "time":
                        timeAttribute();
                        for(ExerciseDetails exerciseDetail : exerciseDetails){
                            if(exerciseDetail.getExerciseId() == dbHelper.getExerciseIdByName(selectedExerciseOption)){
                                for(Workout workout: workouts){
                                    if(exerciseDetail.getWorkoutId() == workout.getId()){
                                        for(int i = 0; i < workoutIDs.size(); i++){
                                            if(exerciseDetail.getWorkoutId() == workoutIDs.get(i)){
                                                int duration = workout.getRestingTime() + workout.getWorkingTime();
                                                data.add(String.valueOf(duration));
                                                String datee = workoutDates.get(i);
                                                String[] parts = datee.split("-");
                                                xLabels.add(parts[labelPart]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    default:
                }
                for (int i = 0; i < data.size(); i++) {
                    switch (typeOfData) {
                        case "reps":
                            entries.add(new Entry(i, getTotalReps(data.get(i))));
                            break;
                        case "weight":
                            entries.add(new Entry(i, getMaximumWeight(data.get(i))));
                            break;
                        case "time":
                            entries.add(new Entry(i, getWorkoutDuration(data.get(i))));
                            break;
                    }
                }

                LineDataSet dataSet = new LineDataSet(entries, "");
                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet.setColor(Color.parseColor("#DB4437"));
                dataSet.setLineWidth(8);
                dataSet.setValueTextSize(25);
                dataSet.setValueTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);


                dataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.format(Locale.getDefault(), "%.0f", value);
                    }
                });

                xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
                chart.invalidate();
            }
        });

        yearsChart.setOnClickListener(v -> {
            data.clear();
            entries.clear();
            xLabels.clear();
            timeSpan = "All";
            labelPart = 0;

            if (selectedExerciseOption != null && !selectedExerciseOption.equals("Select an exercise")) {
                yearsAttribute();

                Date currentDate = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.YEAR, -100);
                Date previousDate = calendar.getTime();
                ArrayList<Integer> workoutIDs = new ArrayList<>();
                ArrayList<String> workoutDates = new ArrayList<>();

                for(Workout workout:workouts){
                    if(workout.getDate().after(previousDate) || workout.getDate().equals(previousDate)){
                        workoutIDs.add(workout.getId());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
                        String dateString = dateFormat.format(workout.getDate());
                        workoutDates.add(dateString);
                    }
                }

                switch (typeOfData) {
                    case "reps":
                        repsAttribute();
                        for(ExerciseDetails exerciseDetail : exerciseDetails){
                            if(exerciseDetail.getExerciseId() == dbHelper.getExerciseIdByName(selectedExerciseOption)){

                                for(int i = 0; i < workoutIDs.size(); i++){
                                    if(exerciseDetail.getWorkoutId() == workoutIDs.get(i)){
                                        data.add(exerciseDetail.getReps());
                                        String datee = workoutDates.get(i);
                                        String[] parts = datee.split("-");
                                        xLabels.add(parts[labelPart]);
                                    }
                                }
                            }
                        }
                        break;
                    case "weight":
                        weightAttribute();
                        for(ExerciseDetails exerciseDetail : exerciseDetails){
                            if(exerciseDetail.getExerciseId() == dbHelper.getExerciseIdByName(selectedExerciseOption)){

                                for(int i = 0; i < workoutIDs.size(); i++){
                                    if(exerciseDetail.getWorkoutId() == workoutIDs.get(i)){
                                        data.add(exerciseDetail.getWeight());
                                        String datee = workoutDates.get(i);
                                        String[] parts = datee.split("-");
                                        xLabels.add(parts[labelPart]);
                                    }
                                }
                            }
                        }
                        break;
                    case "time":
                        timeAttribute();
                        for(ExerciseDetails exerciseDetail : exerciseDetails){
                            if(exerciseDetail.getExerciseId() == dbHelper.getExerciseIdByName(selectedExerciseOption)){
                                for(Workout workout: workouts){
                                    if(exerciseDetail.getWorkoutId() == workout.getId()){
                                        for(int i = 0; i < workoutIDs.size(); i++){
                                            if(exerciseDetail.getWorkoutId() == workoutIDs.get(i)){
                                                int duration = workout.getRestingTime() + workout.getWorkingTime();
                                                data.add(String.valueOf(duration));
                                                String datee = workoutDates.get(i);
                                                String[] parts = datee.split("-");
                                                xLabels.add(parts[labelPart]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    default:
                }
                for (int i = 0; i < data.size(); i++) {
                    switch (typeOfData) {
                        case "reps":
                            entries.add(new Entry(i, getTotalReps(data.get(i))));
                            break;
                        case "weight":
                            entries.add(new Entry(i, getMaximumWeight(data.get(i))));
                            break;
                        case "time":
                            entries.add(new Entry(i, getWorkoutDuration(data.get(i))));
                            break;
                    }
                }

                LineDataSet dataSet = new LineDataSet(entries, "");
                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet.setColor(Color.parseColor("#DB4437"));
                dataSet.setLineWidth(8);
                dataSet.setValueTextSize(25);
                dataSet.setValueTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);


                dataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.format(Locale.getDefault(), "%.0f", value);
                    }
                });

                xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
                chart.invalidate();
            }
        });

        exercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle the selected item
                if (position != 0) {
                    // Update the selectedOption variable with the selected string
                    selectedExerciseOption = exerciseNames.get(position);
                    data.clear();
                    entries.clear();
                    xLabels.clear();

                    Date currentDate = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currentDate);

                    switch (timeSpan) {
                        case "Month":
                            daysAttribute();
                            calendar.add(Calendar.MONTH, -1);
                            labelPart = 2;
                            break;
                        case "Year":
                            monthsAttribute();
                            calendar.add(Calendar.MONTH, -12);
                            labelPart = 1;
                            break;
                        case "All":
                            yearsAttribute();
                            calendar.add(Calendar.YEAR, -100);
                            labelPart = 0;
                            break;
                        default:
                    }

                    Date previousDate = calendar.getTime();

                    ArrayList<Integer> workoutIDs = new ArrayList<>();
                    ArrayList<String> workoutDates = new ArrayList<>();

                    for(Workout workout:workouts){
                        if(workout.getDate().after(previousDate) || workout.getDate().equals(previousDate)){
                            workoutIDs.add(workout.getId());
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
                            String dateString = dateFormat.format(workout.getDate());
                            workoutDates.add(dateString);
                        }
                    }

                    switch (typeOfData) {
                        case "reps":
                            repsAttribute();
                            for(ExerciseDetails exerciseDetail : exerciseDetails){
                                if(exerciseDetail.getExerciseId() == dbHelper.getExerciseIdByName(selectedExerciseOption)){

                                    for(int i = 0; i < workoutIDs.size(); i++){
                                        if(exerciseDetail.getWorkoutId() == workoutIDs.get(i)){
                                            data.add(exerciseDetail.getReps());
                                            String datee = workoutDates.get(i);
                                            String[] parts = datee.split("-");
                                            xLabels.add(parts[labelPart]);
                                        }
                                    }
                                }
                            }
                            break;
                        case "weight":
                            weightAttribute();
                            for(ExerciseDetails exerciseDetail : exerciseDetails){
                                if(exerciseDetail.getExerciseId() == dbHelper.getExerciseIdByName(selectedExerciseOption)){

                                    for(int i = 0; i < workoutIDs.size(); i++){
                                        if(exerciseDetail.getWorkoutId() == workoutIDs.get(i)){
                                            data.add(exerciseDetail.getWeight());
                                            String datee = workoutDates.get(i);
                                            String[] parts = datee.split("-");
                                            xLabels.add(parts[labelPart]);
                                        }
                                    }
                                }
                            }
                            break;
                        case "time":
                            timeAttribute();
                            for(ExerciseDetails exerciseDetail : exerciseDetails){
                                if(exerciseDetail.getExerciseId() == dbHelper.getExerciseIdByName(selectedExerciseOption)){
                                    for(Workout workout: workouts){
                                        if(exerciseDetail.getWorkoutId() == workout.getId()){
                                            for(int i = 0; i < workoutIDs.size(); i++){
                                                if(exerciseDetail.getWorkoutId() == workoutIDs.get(i)){
                                                    int duration = workout.getRestingTime() + workout.getWorkingTime();
                                                    data.add(String.valueOf(duration));
                                                    String datee = workoutDates.get(i);
                                                    String[] parts = datee.split("-");
                                                    xLabels.add(parts[labelPart]);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        default:
                    }

                    for(int i = 0; i < data.size(); i++){
                        switch (typeOfData) {
                            case "reps":
                                entries.add(new Entry(i, getTotalReps(data.get(i))));
                                break;
                            case "weight":
                                entries.add(new Entry(i, getMaximumWeight(data.get(i))));
                                break;
                            case "time":
                                entries.add(new Entry(i, getWorkoutDuration(data.get(i))));
                                break;
                        }
                    }

                    LineDataSet dataSet = new LineDataSet(entries, "");
                    dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    dataSet.setColor(Color.parseColor("#DB4437"));
                    dataSet.setLineWidth(8);
                    dataSet.setValueTextSize(25);
                    dataSet.setValueTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    LineData lineData = new LineData(dataSet);
                    chart.setData(lineData);


                    dataSet.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return String.format(Locale.getDefault(), "%.0f", value);
                        }
                    });

                    xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
                    chart.invalidate();

                } else {
                    // Handle the case when the initial selection is made
                    selectedExerciseOption = null;

                    //Set attribute buttons to nothing selected
                    weightChart.setTextColor(Color.WHITE);
                    weightChart.setBackgroundResource(R.drawable.textview_frame_black);

                    repsChart.setTextColor(Color.WHITE);
                    repsChart.setBackgroundResource(R.drawable.textview_frame_black);

                    timeChart.setTextColor(Color.WHITE);
                    timeChart.setBackgroundResource(R.drawable.textview_frame_black);

                    //Set timespan buttons to nothing selected
                    daysChart.setTextColor(Color.WHITE);
                    daysChart.setBackgroundResource(R.drawable.textview_frame_black);

                    monthsChart.setTextColor(Color.WHITE);
                    monthsChart.setBackgroundResource(R.drawable.textview_frame_black);

                    yearsChart.setTextColor(Color.WHITE);
                    yearsChart.setBackgroundResource(R.drawable.textview_frame_black);

                    entries.clear();
                    chart.clear();
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
        weightChart.setTextColor(Color.WHITE);
        weightChart.setBackgroundResource(R.drawable.textview_frame_black);

        repsChart.setTextColor(Color.parseColor("#DB4437"));
        repsChart.setBackgroundResource(R.drawable.textview_frame_red);

        timeChart.setTextColor(Color.WHITE);
        timeChart.setBackgroundResource(R.drawable.textview_frame_black);
    }

    public void weightAttribute(){
        weightChart.setTextColor(Color.parseColor("#DB4437"));
        weightChart.setBackgroundResource(R.drawable.textview_frame_red);

        repsChart.setTextColor(Color.WHITE);
        repsChart.setBackgroundResource(R.drawable.textview_frame_black);

        timeChart.setTextColor(Color.WHITE);
        timeChart.setBackgroundResource(R.drawable.textview_frame_black);
    }

    public void timeAttribute(){
        timeChart.setTextColor(Color.parseColor("#DB4437"));
        timeChart.setBackgroundResource(R.drawable.textview_frame_red);

        repsChart.setTextColor(Color.WHITE);
        repsChart.setBackgroundResource(R.drawable.textview_frame_black);

        weightChart.setTextColor(Color.WHITE);
        weightChart.setBackgroundResource(R.drawable.textview_frame_black);
    }

    private void daysAttribute(){
        daysChart.setTextColor(Color.parseColor("#DB4437"));
        daysChart.setBackgroundResource(R.drawable.textview_frame_red);

        monthsChart.setTextColor(Color.WHITE);
        monthsChart.setBackgroundResource(R.drawable.textview_frame_black);

        yearsChart.setTextColor(Color.WHITE);
        yearsChart.setBackgroundResource(R.drawable.textview_frame_black);
    }

    private void monthsAttribute(){
        monthsChart.setTextColor(Color.parseColor("#DB4437"));
        monthsChart.setBackgroundResource(R.drawable.textview_frame_red);

        daysChart.setTextColor(Color.WHITE);
        daysChart.setBackgroundResource(R.drawable.textview_frame_black);

        yearsChart.setTextColor(Color.WHITE);
        yearsChart.setBackgroundResource(R.drawable.textview_frame_black);
    }

    private void yearsAttribute(){
        yearsChart.setTextColor(Color.parseColor("#DB4437"));
        yearsChart.setBackgroundResource(R.drawable.textview_frame_red);

        daysChart.setTextColor(Color.WHITE);
        daysChart.setBackgroundResource(R.drawable.textview_frame_black);

        monthsChart.setTextColor(Color.WHITE);
        monthsChart.setBackgroundResource(R.drawable.textview_frame_black);
    }

    private int getTotalReps(String exerciseDetail){
        String[] exerciseDetailArray = exerciseDetail.split(",");
        int totalreps = 0;
        for (String s : exerciseDetailArray) {
            if (!s.trim().equals("")) {
                totalreps = totalreps + Integer.parseInt(s.trim());
            }
        }
        return totalreps;
    }

    private int getMaximumWeight(String exerciseDetail){
        String[] exerciseDetailArray = exerciseDetail.split(",");
        int maxWeight = 0;
        for (String s : exerciseDetailArray) {
            if (!s.trim().equals("")) {
                if (maxWeight < Integer.parseInt(s.trim())) {
                    maxWeight = Integer.parseInt(s.trim());
                }
            }
        }
        return maxWeight;
    }

    private int getWorkoutDuration(String duration){
        return Integer.parseInt(duration) / (1000 * 60);
    }


}