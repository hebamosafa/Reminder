package com.example.future.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.example.future.sunshine.data.alarmcontract.AlarmEntry;

/**
 * {@link ContentProvider} for Students app.
 */
public class alarmprovider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = alarmprovider.class.getSimpleName();

    /** URI matcher code for the content URI for the students table */
    private static final int ALARMS = 1;

    /** URI matcher code for the content URI for a single student in the students table */
    private static final int ALARM_ID = 2;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        sUriMatcher.addURI(alarmcontract.CONTENT_AUTHORITY, alarmcontract.PATH_ALARM, ALARMS);

        sUriMatcher.addURI(alarmcontract.CONTENT_AUTHORITY, alarmcontract.PATH_ALARM + "/#", ALARM_ID);
    }

    /** Database helper object */
    private database mDbHelper;


    @Override
    public boolean onCreate() {
        mDbHelper = new database(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ALARMS:
                // For the STUDENTS code, query the students table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the students table.
                cursor = database.query(AlarmEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ALARM_ID:
                // For the STUDENT_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.students/students/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.z
                selection = AlarmEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the students table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(AlarmEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

Throwable mth;

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ALARMS:
                return insertStudent(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri,mth.getCause() );
        }
    }

    /**
     * Insert a student into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertStudent(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(AlarmEntry.COLUMN_ALARM_NAME);
        if (name == null) {
            throw new IllegalArgumentException("student requires a name");
        }

        // No need to check the others, any value is valid (including null).

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new student with the given values
        long id = database.insert(AlarmEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ALARMS:
                return updateStudent(uri, contentValues, selection, selectionArgs);
            case ALARM_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = AlarmEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateStudent(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    /**
     * Update students in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more students).
     * Return the number of rows that were successfully updated.
     */
    private int updateStudent(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // If the {@link StudentEntry#COLUMN_STUDENT_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(AlarmEntry.COLUMN_ALARM_NAME)) {
            String name = values.getAsString(AlarmEntry.COLUMN_ALARM_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Student requires a name");
            }
        }

        // No need to check the others, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(AlarmEntry.TABLE_NAME, values, selection, selectionArgs);
        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Returns the number of database rows affected by the update statement
        return database.update(AlarmEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ALARMS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(AlarmEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ALARM_ID:
                // Delete a single row given by the ID in the URI
                selection = AlarmEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(AlarmEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;

    }


    @Override
    public String getType(Uri uri) {
    return "";
    }
}
