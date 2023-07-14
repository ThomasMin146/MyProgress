package com.thomas.myprogress.models;

public class ExerciseInfo {

    private String sets, reps, weight;

    public ExerciseInfo(String sets, String reps, String weight) {
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    public ExerciseInfo() {
    }

    public ExerciseInfo(String reps, String weight) {
        this.reps = reps;
        this.weight = weight;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
