package com.thomas.myprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class ExerciseGraph extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_graph);

        // Find the GraphView element
        GraphView graphView = findViewById(R.id.graph);

        // Create some sample data points
        DataPoint[] dataPoints = new DataPoint[]{
                new DataPoint(0, 0),
                new DataPoint(1, 1),
                new DataPoint(2, 2),
                new DataPoint(3, 1),
                new DataPoint(4, 3)
        };

        // Create a LineGraphSeries using the data points
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);

        // Add the series to the GraphView
        graphView.addSeries(series);

    }

}