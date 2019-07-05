package com.example.future.sunshine;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.future.sunshine.data.alarmcontract;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import static java.util.Locale.FRANCE;
import static java.util.Locale.getAvailableLocales;
public class add extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String TAG = "mytag";
    Uri muri;
    Button start;
    EditText mDate;
    EditText mTime, mname;
    Calendar myCalendar;
    private static final int EXISTING_PET_LOADER = 1;
    String[] h;
    Calendar mcurrentTime;
    int  myear, mmonth, mday;
    int diffyear, diffmonth, diffday, diffminute, diffhour;
    RadioGroup mygroup;
    RadioButton myradio;
    Uri current_data_alarm;
    ArrayList<Integer> minutes = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        // timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        myCalendar = Calendar.getInstance();
        mygroup = findViewById(R.id.radio);


        Intent intent = getIntent();
        current_data_alarm = intent.getData();
        if (current_data_alarm == null) {
            // This is a new pet, so change the app bar to say "Add a Student"
            setTitle("setAlarm");

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a student that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing student, so change app bar to say "Edit Student"
            setTitle("UpdateAlarm");

            // Initialize a loader to read the student data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);
        }


        mDate = (EditText) findViewById(R.id.textdate);
        mname = findViewById(R.id.reminderto);
        mDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(add.this, dateD, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mTime = (EditText) findViewById(R.id.texttime);
        mTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(add.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mTime.setText(selectedHour + ":" + selectedMinute);
                        diffminute = selectedMinute - minute;
                        diffhour = selectedHour - hour;

                        minutes.add((diffminute * 1000 * 60) + (diffhour * 1000 * 60 * 60));


                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


    }


    DatePickerDialog.OnDateSetListener dateD = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // TODO Auto-generated method stub
            myear = myCalendar.get(Calendar.YEAR);
            mmonth = myCalendar.get(Calendar.MONTH);
            mday = myCalendar.get(Calendar.DAY_OF_MONTH);
            String y = String.valueOf(myear);
            String m = String.valueOf(mmonth);
            String d = String.valueOf(mday);


            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            diffyear = year - myear;
            diffmonth = monthOfYear - mmonth;
            diffday = dayOfMonth - mday;
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        mDate.setText(sdf.format(myCalendar.getTime()));

    }



    public static void startAlert(int seconds,Context context) {
    /*
      our alarmManager array size will be that minutes list size
    */
        AlarmManager alarmManagers;
        Intent intents;
        PendingIntent pendingIntent;

        intents = new Intent(context, MyBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 2, intents, 0);

        alarmManagers = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManagers.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + seconds, pendingIntent);
        //Toast.makeText(context, "Alarm set after " + diffhour + "h&" + diffminute + "min", Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:

                Intent intent = new Intent(add.this, prefsactivity.class);
                startActivity(intent);
                return true;
            case R.id.add:
                insert();




                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void insert() {
        int selcted = mygroup.getCheckedRadioButtonId();
        myradio = findViewById(selcted);
        String myselscted = (String) myradio.getText();
        h= myselscted.split(" ");
 Intent snooze=new Intent(this,dialog.class);
       int i=Integer.parseInt(h[0]);
snooze.putExtra("snooze",i);

        String sel_Date = mDate.getText().toString().trim();
        if (sel_Date.isEmpty()) {
            sel_Date = String.valueOf(myCalendar.get(Calendar.DAY_OF_MONTH) + "/" + String.valueOf((myCalendar.get(Calendar.MONTH) + 1)) + "/" + String.valueOf(myCalendar.get(Calendar.YEAR)));
        }
        String sel_Time = mTime.getText().toString().trim();
        if (sel_Time.isEmpty()) {
            Toast.makeText(this, "select the time please", Toast.LENGTH_SHORT).show();
        }
        String sel_name = mname.getText().toString().trim();
        if (sel_name.isEmpty()) {
            Toast.makeText(this, "select the title please", Toast.LENGTH_SHORT).show();
        }

        ContentValues value = new ContentValues();
        value.put(alarmcontract.AlarmEntry.COLUMN_ALARM_DATE, sel_Date);
        value.put(alarmcontract.AlarmEntry.COLUMN_ALARM_TIME, sel_Time);
        value.put(alarmcontract.AlarmEntry.COLUMN_ALARM_NAME, sel_name);
        value.put(alarmcontract.AlarmEntry.COLUMN_ALARM_SNOOZE, myselscted);



        if (!(sel_Time.isEmpty() || sel_name.isEmpty())) {
            if(current_data_alarm==null){
                 muri = getContentResolver().insert(alarmcontract.AlarmEntry.CONTENT_URI, value);
                startAlert((diffminute * 1000 * 60) + (diffhour * 1000 * 60 * 60),this);}

           else{
                int updated_num = getContentResolver().update(alarmcontract.AlarmEntry.CONTENT_URI, value,null,null);
                startAlert((diffminute * 1000 * 60) + (diffhour * 1000 * 60 * 60),this);
            }


            finish();
            if (muri == null && current_data_alarm==null)
                Toast.makeText(this, "Ya KHBTK YA HEBA", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "HHHHHHHHHH DONE", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] pro={
                alarmcontract.AlarmEntry.COLUMN_ALARM_NAME,
        alarmcontract.AlarmEntry.COLUMN_ALARM_TIME,
                alarmcontract.AlarmEntry.COLUMN_ALARM_DATE

        };
        return new CursorLoader(this,current_data_alarm,pro,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(alarmcontract.AlarmEntry.COLUMN_ALARM_NAME);
            int timeColumnIndex = cursor.getColumnIndex(alarmcontract.AlarmEntry.COLUMN_ALARM_TIME);
            int dateColumnIndex = cursor.getColumnIndex(alarmcontract.AlarmEntry.COLUMN_ALARM_DATE);


            // Extract out the value from the Cursor for the given column index

            String name = cursor.getString(nameColumnIndex);
            String time = cursor.getString(timeColumnIndex);
            String date = cursor.getString(dateColumnIndex);


            // Update the views on the screen with the values from the database
            mname.setText(name);
            mTime.setText(time);
            mDate.setText(date); }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mname.setText("");
        mTime.setText("");
        mDate.setText("");
    }
}

