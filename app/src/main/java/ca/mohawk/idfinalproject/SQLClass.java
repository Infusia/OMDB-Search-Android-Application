package ca.mohawk.idfinalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
//SQL class to create table and columns
//allows for CRUD on movie details
public class SQLClass extends SQLiteOpenHelper {
    private static final String TAG = "A";

    public static final String DATABASE_FILE_NAME = "FinalProj.db";
    public static final int DATABASE_VERSION = 1;
    public static final String FAVOURITESTABLE = "favouritestable";
    public static final String ID = "_id";
    public static final String IMAGE = "image";
    public static final String IMDBID = "imdbid";
    public static final String TITLE = "title";

    private static final String SQL_CREATE =
            "CREATE TABLE " + FAVOURITESTABLE + " ( " + ID + " INTEGER PRIMARY KEY, " +
                    IMAGE + " TEXT, " + IMDBID + " TEXT," + TITLE + " TEXT) ";

    public SQLClass(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate " + SQL_CREATE);
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This is only called if the DATABASE_VERSION changes
        // Possible actions - delete table (ie DROP TABLE IF EXISTS mytable), then call onCreate
    }


}
