package com.example.future.sunshine;

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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import static java.util.Locale.FRANCE;
import static java.util.Locale.getAvailableLocales;

public class add extends AppCompatActivity {
    private static final String TAG = "mytag";
    Button start;
    EditText mDate;
    EditText mTime,mname;
    Calendar myCalendar;
    Calendar mcurrentTime;
    int m, h,myear,mmonth,mday;
    int diffyear, diffmonth, diffday, diffminute, diffhour;
    PendingIntent pendingIntent;
    RadioGroup mygroup;
    RadioButton myradio;
    ArrayList<Integer> minutes= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        // timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        myCalendar = Calendar.getInstance();
        mygroup=findViewById(R.id.radio);



        mDate = (EditText) findViewById(R.id.textdate);
        mname=findViewById(R.id.reminderto);
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

                        minutes.add((diffminute*1000*60)+(diffhour*1000*60*60));


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
            String y=String.valueOf(myear);
            String m=String.valueOf(mmonth);
            String d=String.valueOf(mday);


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

    public void startAlert() {


    /*
      our alarmManager array size will be that minutes list size
    */
        AlarmManager[] alarmManagers = new AlarmManager[minutes.size()];
        Intent intents[] = new Intent[minutes.size()];
        for (int i = 0; i <minutes.size(); i++) {


                intents[i] = new Intent(this,MyBroadcastReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(getBaseContext(), i,intents[i],0);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MINUTE, minutes.get(i));
                alarmManagers[i] = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);
                alarmManagers[i].set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + minutes.get(i), pendingIntent);
                Toast.makeText(this, "Alarm set after " + diffhour + "h&" + diffminute + "m"+i+"minutes"+minutes.size(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "startAlert: " + minutes.get(i));


        }


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
                startAlert();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void insert() {
        int selcted=mygroup.getCheckedRadioButtonId();
        myradio=findViewById(selcted);
        String myselscted= (String) myradio.getText();

        Toast.makeText(this, myradio.getText(), Toast.LENGTH_LONG).show();
       String sel_Date=mDate.getText().toString().trim();
       if(sel_Date.isEmpty())
       {
           sel_Date= String.valueOf(myCalendar.get(Calendar.DAY_OF_MONTH)+"/"+String.valueOf((myCalendar.get(Calendar.MONTH)+1))+"/"+String.valueOf(myCalendar.get(Calendar.YEAR)));
       }
       String sel_Time=mTime.getText().toString().trim();
       if(sel_Time.isEmpty()){
           Toast.makeText(this,"select the time ya hiawan",Toast.LENGTH_SHORT).show();
       }
       String sel_name=mname.getText().toString().trim();
        if(sel_name.isEmpty()){
            Toast.makeText(this,"select the title ya hiawan",Toast.LENGTH_SHORT).show();
        }

        ContentValues value=new ContentValues();
        value.put(alarmcontract.AlarmEntry.COLUMN_ALARM_DATE,sel_Date);
        value.put(alarmcontract.AlarmEntry.COLUMN_ALARM_TIME,sel_Time);
        value.put(alarmcontract.AlarmEntry.COLUMN_ALARM_NAME,sel_name);
        value.put(alarmcontract.AlarmEntry.COLUMN_ALARM_SNOOZE,myselscted);

if(!(sel_Time.isEmpty()||sel_name.isEmpty())) {
            Uri muri = getContentResolver().insert(alarmcontract.AlarmEntry.CONTENT_URI, value);

            //finish();
            if (muri == null)
                Toast.makeText(this, "Ya KHBTK YA HEBA", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "HHHHHHHHHH DONE", Toast.LENGTH_SHORT).show();
        }

    }

}