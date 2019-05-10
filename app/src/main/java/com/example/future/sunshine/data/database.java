package com.example.future.sunshine.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper for Students app. Manages database creation and version management.
 */
public class database extends SQLiteOpenHelper {


    public static final String LOG_TAG = database.class.getSimpleName();


    /** Name of the database file */
    private static final String DATABASE_NAME = "alarm.db";


    private static final int DATABASE_VERSION = 1;

    public database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a String that contains the SQL statement to create the pets table
        final String SQL_CREATE_ALARM_TABLE = "CREATE TABLE " + alarmcontract.AlarmEntry.TABLE_NAME + " ("
                + alarmcontract.AlarmEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + alarmcontract.AlarmEntry.COLUMN_ALARM_NAME + " TEXT NOT NULL, "
                + alarmcontract.AlarmEntry.COLUMN_ALARM_DATE + " TEXT, "
                + alarmcontract.AlarmEntry.COLUMN_ALARM_SNOOZE + " TEXT, "
                + alarmcontract.AlarmEntry.COLUMN_ALARM_TIME + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ALARM_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // The database is still at version 1, so there's nothing to do be done here.

    }
}
