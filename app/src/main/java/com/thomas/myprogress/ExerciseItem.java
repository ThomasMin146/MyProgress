package com.thomas.myprogress;

public class ExerciseItem {
    private String name;
    private int reps;
    private ExerciseEnums.ExerciseDifficulty difficulty;
    private ExerciseEnums.BodyPart bodyPart;

    public ExerciseItem(String name, int reps, ExerciseEnums.ExerciseDifficulty difficulty, ExerciseEnums.BodyPart bodyPart) {
        this.name = name;
        this.reps = reps;
        this.difficulty = difficulty;
        this.bodyPart = bodyPart;
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
