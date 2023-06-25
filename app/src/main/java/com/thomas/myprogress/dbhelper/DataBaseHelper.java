package com.thomas.myprogress.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_PATH = "/data/data/com.thomas.myprogress/databases/";
    private static final String DB_NAME = "AppDatabase.db";
    private static final String TABLE_NAME = "Users";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 3);
        this.myContext = context;
        this.createDataBase();
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase(){
        try {
            boolean dbExist = checkDataBase();
            if(dbExist){
                //do nothing - database already exist
            }else{
                //By calling this method and empty database will be created into the default system path
                //of your application so we are gonna be able to overwrite that database with our database.
                this.getReadableDatabase();
                copyDataBase();
            }
        }
        catch (Exception e) {
        }
    }

    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){
            //database does't exist yet.
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    } // returns true if db exists, else false

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase(){

        try{
            //Open your local db as the input stream
            InputStream myInput = myContext.getAssets().open(DB_NAME);

            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            //transfer bytes from the inputfile to the outputfile
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
        String query = "CREATE TABLE IF NOT EXISTS Users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,password TEXT)";
        String query2 = "CREATE TABLE IF NOT EXISTS Exercise (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,difficulty TEXT, type TEXT)";
        String query3 = "CREATE TABLE IF NOT EXISTS MyWorkout (MyWorkout_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " MyWorkout_name TEXT NOT NULL, Exercise_name TEXT, Exercise_sets INTEGER, Exercise_reps INTEGER, Exercise_weight INTEGER)";
        db.execSQL(query);
        db.execSQL(query2);
        db.execSQL(query3);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query3 = "CREATE TABLE IF NOT EXISTS MyWorkout (MyWorkout_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " MyWorkout_name TEXT NOT NULL, Exercise_name TEXT, Exercise_sets INTEGER, Exercise_reps INTEGER, Exercise_weight INTEGER)";
        db.execSQL(query3);

    }

    public Boolean addRow(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);

        ContentValues values = new ContentValues();
        values.put("name", username);
        values.put("password", password);

        long result = db.insert("Users", null, values);
        db.close();

        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean checkUser(String name, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +" WHERE name = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{name, password});

        if (cursor != null && cursor.moveToFirst()) {
            // Username and password match in the database
            cursor.close();
            db.close();
            return true;
        } else {
            // Invalid username or password
            cursor.close();
            db.close();
            return false;
        }
    }

    public ArrayList<String> allExercisesNames(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> exerciseNames = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.query("Exercise", new String[]{"name"}, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex("name");
                do {
                    String exerciseName = cursor.getString(nameIndex);
                    exerciseNames.add(exerciseName);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return exerciseNames;
    }

    public void addWorkout(String workoutName, String exerciseName, int sets, int reps, int weight) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("MyWorkout_name", workoutName);
        values.put("Exercise_name", exerciseName);
        values.put("Exercise_sets", sets);
        values.put("Exercise_reps", reps);
        values.put("Exercise_weight", weight);

        db.insert("MyWorkout", null, values);

        db.close();
    }

    public void deleteWorkoutsByExercise(String exerciseName) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "Exercise_name=?";
        String[] whereArgs = { exerciseName };

        db.delete("MyWorkout", whereClause, whereArgs);

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

}
