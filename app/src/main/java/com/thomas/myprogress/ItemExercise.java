package com.thomas.myprogress;

import android.content.ClipData;

public class ItemExercise {
    private String name;
    private String reps;
    private String sets;
    private String weight;
    private int id;

    public ItemExercise(String name, String reps, String sets, String weight) {
        this.name = name;
        this.reps = reps;
        this.sets = sets;
        this.weight = weight;
    }
    public ItemExercise(){

    }

    public ItemExercise(String sets){
        this.sets = sets;
    }

    /*public ItemExercise(String name) {
        this.name = name;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
