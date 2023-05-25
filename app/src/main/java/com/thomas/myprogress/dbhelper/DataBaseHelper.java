package com.thomas.myprogress.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.thomas.myprogress.UserModel;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    //replace com.binarybricks.shippingwithsqllite with you Application package nae
    //This should be same as which you used package section in your manifest
    private static String DB_PATH = "/data/data/com.thomas.myprogress/databases/";

    //replace this with name of your db file which you copied into asset folder
    private static String DB_NAME = "UserLogin.db";

    private static final String TABLE_NAME = "Users";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
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

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
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
    }

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

    public SQLiteDatabase openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        return myDataBase;

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
        { myDataBase.close();}

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name" + " TEXT,"
                + "password" + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addRow(String username, String password) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair
        values.put("name", username);
        values.put("password", password);

        // after adding all values we are passing
        // content values to our table.
        db.insert("Users", null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public ArrayList<UserModel> readUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorUser = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<UserModel> allUsers = new ArrayList<>();

        if (cursorUser.moveToFirst()) {
            do {
                // on below line we are adding the data from
                // cursor to our array list.
                allUsers.add(new UserModel(
                        cursorUser.getInt(1),
                        cursorUser.getString(2),
                        cursorUser.getString(3)));
            } while (cursorUser.moveToNext());
            // moving our cursor to next.
        }

        cursorUser.close();
        return allUsers;
    }



}
