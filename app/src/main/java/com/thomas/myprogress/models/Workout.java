package com.thomas.myprogress.models;

import java.time.LocalDate;
import java.util.Date;

public class Workout {
    private int id;
    private String name;
    private Date date;
    private int timer;


    // Constructors, getters, and setters

    public Workout(int id, String name, Date date, int timer) {
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

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
}

