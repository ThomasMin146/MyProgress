package com.thomas.myprogress.models;

public class Exercise {
    private int id;
    private String name;
    private String bodypart;
    private String difficulty;

    // Constructors, getters, and setters

    public Exercise(int id, String name, String bodypart, String difficulty) {
        this.id = id;
        this.name = name;
        this.bodypart = bodypart;
        this.difficulty = difficulty;
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

    public String getBodypart() {
        return bodypart;
    }

    public void setBodypart(String bodypart) {
        this.bodypart = bodypart;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}

