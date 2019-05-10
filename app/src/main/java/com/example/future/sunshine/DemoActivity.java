package com.example.future.sunshine;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class DemoActivity extends FragmentActivity {
    AlertDemo alert;
    MediaPlayer mp;
    boolean snooze;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean checkBox = prefs.getBoolean("noti", false);
        if(checkBox)
        {createNotificationChannel(context);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "H")
                    .setSmallIcon(R.mipmap.noti) //set icon for notification
                    .setContentTitle("Notifications Example") //set title of notification
                    .setContentText("Heba loves Ahmed")//this is notification message
                    .setAutoCancel(true) // makes auto cancel of notification
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification


            Intent notificationIntent = new Intent(context, NotificationView.class);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //notification message will get at NotificationView
            notificationIntent.putExtra("message", "This is a notification message");

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);

            // Add as notification
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, mBuilder.build());

        }
        else{
            /** Creating an Alert Dialog Window */
             alert = new AlertDemo();



            /** Opening the Alert Dialog Window. This will be opened when the alarm goes off */
            alert.show(getSupportFragmentManager(), "AlertDemo");
            Log.d("DDDDDDDDDDDD", "galy: "+snooze);
            /*if(snooze){
                Intent intent = new Intent(this,DemoActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 545,intent,0);

                AlarmManager alarmManagers = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);
                alarmManagers.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +6000, pendingIntent);

            }*/

            mp= MediaPlayer.create(context, R.raw.alarm);
            mp.start();
            alert.ha(mp);
            Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
            // Vibrate the mobile phone
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(2000);


        }



    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "heba";
            String description = "heba loves ahmed";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("H", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    }
