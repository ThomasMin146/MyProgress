package com.thomas.myprogress.models;

import java.util.Date;

public class Workout implements  Comparable<Workout>{
    private int id;
    private String name;
    private Date date;
    private String timer;


    // Constructors, getters, and setters

    public Workout(int id, String name, Date date, String timer) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.timer = timer;
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

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    @Override
    public int compareTo(Workout o) {
        return this.date.compareTo(o.date);
    }
}

