package com.thomas.myprogress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.thomas.myprogress.models.Exercise;
import com.thomas.myprogress.models.ExerciseDetails;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    private final String DB_PATH;
    private static final String DB_NAME = "Database.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        this.createDataBase();
        this.DB_PATH = context.getFilesDir().getPath();
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase(){
        try {
            boolean dbExist = checkDataBase();
            if(!dbExist) {
                this.getWritableDatabase();
                copyDataBase();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){
            //database doesn't exist yet.
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null;
    } // returns true if db exists, else false

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transferring bitstream.
     * */
    private void copyDataBase(){

        try{
            //Open your local db as the input stream
            InputStream myInput = myContext.getAssets().open(DB_NAME);

            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            //transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (Exception e) {
            //catch exception
        }
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null){
            myDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE IF NOT EXISTS Users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE,password TEXT NOT NULL)";
        String query2 = "CREATE TABLE IF NOT EXISTS Workouts (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, date DATE, timer INTEGER)";
        String query3 = "CREATE TABLE IF NOT EXISTS Exercises (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE,bodypart TEXT, difficulty TEXT)";
        String query4 = "CREATE TABLE IF NOT EXISTS ExerciseDetails (id INTEGER PRIMARY KEY AUTOINCREMENT, workout_id INTEGER REFERENCES Workouts(id), " +
                        "exercise_id INTEGER REFERENCES Exercises(id), sets INTEGER, reps INTEGER, weight INTEGER)";

        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add a new row to the Workouts table
    public long addWorkout(String name, String date, int timer) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("date", date);
        values.put("timer", timer);
        long newRowId = db.insert("Workouts", null, values);
        db.close();
        return newRowId;
    }

    // Add a new row to the Exercises table
    public long addExercise(String name, String bodypart, String difficulty) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("bodypart", bodypart);
        values.put("difficulty", difficulty);
        long newRowId = db.insert("Exercises", null, values);
        db.close();
        return newRowId;
    }

    // Add a new row to the ExerciseDetails table
    public long addExerciseDetails(long workoutId, int exerciseId, int sets, int reps, int weight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("workout_id", workoutId);
        values.put("exercise_id", exerciseId);
        values.put("sets", sets);
        values.put("reps", reps);
        values.put("weight", weight);
        long newRowId = db.insert("ExerciseDetails", null, values);
        db.close();
        return newRowId;
    }

    // Update a row in the Workouts table
    public int updateWorkout(long workoutId, String name, String date, int timer) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("date", date);
        values.put("timer", timer);
        String selection = "id=?";
        String[] selectionArgs = { String.valueOf(workoutId) };
        int count = db.update("Workouts", values, selection, selectionArgs);
        db.close();
        return count;
    }

    // Update a row in the Exercises table
    public int updateExercise(int exerciseId, String name, String bodypart, String difficulty) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("bodypart", bodypart);
        values.put("difficulty", difficulty);
        String selection = "id=?";
        String[] selectionArgs = { String.valueOf(exerciseId) };
        int count = db.update("Exercises", values, selection, selectionArgs);
        db.close();
        return count;
    }

    // Update a row in the ExerciseDetails table
    public int updateExerciseDetails(int detailsId, int sets, int reps, int weight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sets", sets);
        values.put("reps", reps);
        values.put("weight", weight);
        String selection = "id=?";
        String[] selectionArgs = { String.valueOf(detailsId) };
        int count = db.update("ExerciseDetails", values, selection, selectionArgs);
        db.close();
        return count;
    }

    // Delete a row from the Workouts table
    public int deleteWorkout(int workoutId) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = "id=?";
        String[] selectionArgs = { String.valueOf(workoutId) };
        int count = db.delete("Workouts", selection, selectionArgs);
        db.close();
        return count;
    }

    // Delete a row from the Exercises table
    public int deleteExercise(int exerciseId) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = "id=?";
        String[] selectionArgs = { String.valueOf(exerciseId) };
        int count = db.delete("Exercises", selection, selectionArgs);
        db.close();
        return count;
    }

    // Delete a row from the ExerciseDetails table
    public int deleteExerciseDetails(int detailsId) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = "id=?";
        String[] selectionArgs = { String.valueOf(detailsId) };
        int count = db.delete("ExerciseDetails", selection, selectionArgs);
        db.close();
        return count;
    }


    public void addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);

        ContentValues values = new ContentValues();
        values.put("name", username);
        values.put("password", password);

        db.insert("Users", null, values);
        db.close();

    }

    public Boolean checkUser(String name, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Users" + " WHERE name = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{name, password});

        if (cursor != null && cursor.moveToFirst()) {
            // Username and password match in the database
            cursor.close();
            db.close();
            return true;
        } else {
            // Invalid username or password
            assert cursor != null;
            cursor.close();
            db.close();
            return false;
        }
    }

    public void deleteWorkoutsByExercise(String exerciseName) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "Exercise_name=?";
        String[] whereArgs = { exerciseName };

        db.delete("MyWorkout", whereClause, whereArgs);
        db.close();
    }

    public void deleteExerciseByName(String exerciseName) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "name=?";
        String[] whereArgs = { exerciseName };

        db.delete("Exercise", whereClause, whereArgs);
        db.close();
    }

    public void updateMyWorkoutColumn(int workoutId, String columnName, String columnValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(columnName, columnValue);

        String whereClause = "MyWorkout_id = ?";
        String[] whereArgs = { String.valueOf(workoutId) };

        db.update("MyWorkout", values, whereClause, whereArgs);
        db.close();
    }

    public void updateExerciseColumn(int id, String columnName, String columnValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(columnName, columnValue);

        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(id) };

        db.update("Exercise", values, whereClause, whereArgs);
        db.close();
    }

    public void createExercise(String name, String difficulty, String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("difficulty", difficulty);
        values.put("type", type);

        db.insert("Exercise", null, values);
        db.close();
    }

    public void createWorkout(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("MyWorkout_name", "Workout A");
        values.put("Exercise_name", name);

        db.insert("MyWorkout", null, values);
        db.close();
    }

    public ArrayList<ExerciseDetails> getAllExerciseDetails() {
        ArrayList<ExerciseDetails> exerciseList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("ExerciseDetails", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            int workoutId = cursor.getInt(cursor.getColumnIndexOrThrow("workout_id"));
            int exerciseId = cursor.getInt(cursor.getColumnIndexOrThrow("exercise_id"));
            int sets = cursor.getInt(cursor.getColumnIndexOrThrow("sets"));
            int reps = cursor.getInt(cursor.getColumnIndexOrThrow("reps"));
            double weight = cursor.getDouble(cursor.getColumnIndexOrThrow("weight"));

            ExerciseDetails exerciseDetails = new ExerciseDetails(id, workoutId, exerciseId, sets, reps, weight);
            exerciseList.add(exerciseDetails);
        }

        cursor.close();
        db.close();

        return exerciseList;
    }

    public ArrayList<Exercise> getAllExercises() {
        ArrayList<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Exercises", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String bodypart = cursor.getString(cursor.getColumnIndexOrThrow("bodypart"));
            String difficulty = cursor.getString(cursor.getColumnIndexOrThrow("difficulty"));

            Exercise exercise = new Exercise(id, name, bodypart, difficulty);
            exercises.add(exercise);
        }

        cursor.close();
        db.close();

        return exercises;
    }

    public String getExerciseName(int exerciseId) {
        SQLiteDatabase db = getReadableDatabase();
        String exerciseName = "";

        Cursor cursor = null;
        try {
            String[] projection = {"name"};
            String selection = "id = ?";
            String[] selectionArgs = {String.valueOf(exerciseId)};

            cursor = db.query("Exercises", projection, selection, selectionArgs, null, null, null);
            if (cursor.moveToFirst()) {
                exerciseName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // Close the database connection
        db.close();

        return exerciseName;
    }





}
