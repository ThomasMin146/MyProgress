package com.thomas.myprogress.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
    private final String DB_PATH;
    private static final String DB_NAME = "AppDatabase.db";
    private static final String TABLE_NAME = "Users";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 7);
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
                this.getReadableDatabase();
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
        String query = "CREATE TABLE IF NOT EXISTS Users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,password TEXT)";
        String query2 = "CREATE TABLE IF NOT EXISTS Exercise (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL,difficulty TEXT, type TEXT)";
        String query3 = "CREATE TABLE IF NOT EXISTS MyWorkout (MyWorkout_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " MyWorkout_name TEXT NOT NULL, Exercise_name TEXT, Exercise_sets INTEGER, Exercise_reps INTEGER, Exercise_weight INTEGER)";

        db.execSQL(query);
        db.execSQL(query2);
        db.execSQL(query3);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addRow(String username, String password) {
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
        String query = "SELECT * FROM " + TABLE_NAME +" WHERE name = ? AND password = ?";
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




}
