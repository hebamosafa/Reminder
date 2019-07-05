package com.example.future.sunshine;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.FRANCE;
import static java.util.Locale.getAvailableLocales;

import com.example.future.sunshine.data.alarmcontract.AlarmEntry;
import com.example.future.sunshine.data.database;
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ListView listView;
    TextView textView;
    String[] listItem;
    Mycorsuradaptor mCursorAdapter;
    private static final int ALARM_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView studentListView = findViewById(R.id.listView);
        Button fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, add.class);
                startActivity(intent);
            }
        });



        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There is no pet data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new Mycorsuradaptor(this, null);
        studentListView.setAdapter(mCursorAdapter);
        studentListView.setTextFilterEnabled(true);

        mCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {

            @Override
            public Cursor runQuery(CharSequence constraint) {

                database mDbHelper = new database(getBaseContext());
                SQLiteDatabase db = mDbHelper.getReadableDatabase();
                // in real life, do something more secure than concatenation
                // but it will depend on your schema
                // This is the query that will run on filtering
                String query =
                        "SELECT " + AlarmEntry._ID + " , " + AlarmEntry.COLUMN_ALARM_NAME
                                + " , " + AlarmEntry.COLUMN_ALARM_TIME
                                + " , " + AlarmEntry.COLUMN_ALARM_DATE
                                + " FROM " + AlarmEntry.TABLE_NAME
                                + " where " + AlarmEntry.COLUMN_ALARM_NAME + " like '%" + constraint + "%' "
                                + " OR " + AlarmEntry.COLUMN_ALARM_TIME + " like '%" + constraint + "%' "
                                + " OR " + AlarmEntry.COLUMN_ALARM_DATE + " like '%" + constraint + "%' "
                                + " ORDER BY NAME ASC";
                return db.rawQuery(query, null);
            }
        });

        getLoaderManager().initLoader(ALARM_LOADER, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                AlarmEntry._ID,
                AlarmEntry.COLUMN_ALARM_NAME,
                AlarmEntry.COLUMN_ALARM_DATE,
                AlarmEntry.COLUMN_ALARM_TIME};


        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                AlarmEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}