package com.thomas.myprogress.models;

import java.util.Date;

public class Workout implements  Comparable<Workout>{
    private int id;
    private String name;
    private Date date;
    private int workingTime;
    private int restingTime;


    // Constructors, getters, and setters

    public Workout(int id, String name, Date date, int workingTime, int restingTime) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.workingTime = workingTime;
        this.restingTime = restingTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(int workingTime) {
        this.workingTime = workingTime;
    }

    public int getRestingTime() {
        return restingTime;
    }

    public void setRestingTime(int restingTime) {
        this.restingTime = restingTime;
    }

    @Override
    public int compareTo(Workout o) {
        return this.date.compareTo(o.date);
    }
}

