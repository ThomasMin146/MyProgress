package com.thomas.myprogress.chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class CustomXAxisValueFormatter extends ValueFormatter {
    private final String[] timeIntervals;

    public CustomXAxisValueFormatter(String[] timeIntervals) {
        this.timeIntervals = timeIntervals;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        int index = (int) value;
        if (index >= 0 && index < timeIntervals.length) {
            return timeIntervals[index];
        }
        return "";
    }
}
