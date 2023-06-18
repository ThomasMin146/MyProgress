package com.thomas.myprogress;

public class ItemExercise {
    private String name;
    private int reps;
    private int sets;
    private int weight;

    public ItemExercise(String name, int reps, int sets, int weight) {
        this.name = name;
        this.reps = reps;
        this.sets = sets;
        this.weight = weight;
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
}
