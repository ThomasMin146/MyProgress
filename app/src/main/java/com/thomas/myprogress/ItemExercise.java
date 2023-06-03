package com.thomas.myprogress;

public class ItemExercise {
    private String name;
    private int reps;
    private ExerciseEnums.BodyPart bodyPart;
    private ExerciseEnums.ExerciseDifficulty difficulty;

    public ItemExercise(String name, int reps, ExerciseEnums.BodyPart bodyPart, ExerciseEnums.ExerciseDifficulty difficulty) {
        this.name = name;
        this.reps = reps;
        this.bodyPart = bodyPart;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public int getReps() {
        return reps;
    }

    public ExerciseEnums.ExerciseDifficulty getDifficulty() {
        return difficulty;
    }

    public ExerciseEnums.BodyPart getBodyPart() {
        return bodyPart;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setDifficulty(ExerciseEnums.ExerciseDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setBodyPart(ExerciseEnums.BodyPart bodyPart) {
        this.bodyPart = bodyPart;
    }
}
