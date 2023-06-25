package com.thomas.myprogress;

import android.content.ClipData;

public class ItemExercise {
    private String name;
    private int reps;
    private int sets;
    private int weight;
    private int id;

    public ItemExercise(String name, int reps, int sets, int weight) {
        this.name = name;
        this.reps = reps;
        this.sets = sets;
        this.weight = weight;
    }
    public ItemExercise(){

    }

    public ItemExercise(int sets){
        this.sets = sets;
    }

    public ItemExercise(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
