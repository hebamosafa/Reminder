package com.example.future.sunshine;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class MyBroadcastReceiver extends BroadcastReceiver {

    static MediaPlayer mp;

public static MediaPlayer m(){
    return mp;
}
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean checkBox = prefs.getBoolean("noti", false);
        if(checkBox)
        {createNotificationChannel(context);

            Toast.makeText(context,"Alarm Saved",Toast.LENGTH_LONG).show();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "H")
                    .setSmallIcon(R.mipmap.noti) //set icon for notification
                    .setContentTitle("Alarm Notification") //set title of notification
                    .setContentText("Marry Ahmed Now")//this is notification message
                    .setAutoCancel(false)
                    .setColor(255)// makes auto cancel of notification
                    .setColorized(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification

                                   ////////////////////////////////////////////////////////////////////////
           Intent notificationIntent = new Intent(context, NotificationView.class);
           notificationIntent .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //notification message will get at NotificationView
            notificationIntent.putExtra("message", "");

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
                           ///////////////////////////////////////////////////////////////
            // Add as notification
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, mBuilder.build());

        }
        else{
            Toast.makeText(context,"Alarn Saved",Toast.LENGTH_LONG).show();
            mp=MediaPlayer.create(context, R.raw.alarm);
            mp.start();
            Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
           Intent intents = new Intent( context,dialog.class);
           context.startActivity(intents);
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