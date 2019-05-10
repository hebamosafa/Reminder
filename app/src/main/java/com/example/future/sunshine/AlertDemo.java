package com.example.future.sunshine;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.WindowManager.LayoutParams;

import java.util.Calendar;

public class AlertDemo extends DialogFragment {

    MediaPlayer mp;
    static boolean snooze=true;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        /** Turn Screen On and Unlock the keypad when this alert dialog is displayed */
        getActivity().getWindow().addFlags(LayoutParams.FLAG_TURN_SCREEN_ON | LayoutParams.FLAG_DISMISS_KEYGUARD);

        /** Creating a alert dialog builder */
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        /** Setting title for the alert dialog */
        builder.setTitle("Alarm");

        /** Setting the content for the alert dialog */
        builder.setMessage("An Alarm by AlarmManager");

        /** Defining an OK button event listener */
        builder.setPositiveButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /** Exit application on click OK */
                getActivity().finish();
                snooze = false;
                Log.d("hhhhhhhhhhh", "onClick ok: " + snooze);
                mp.stop();
            }
        });
        builder.setNegativeButton("SNOOZE", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /** Exit application on click OK */
                getActivity().finish();


            }
        });
        if(snooze)

        {
            Log.d("hhhhhhhhhhh", "onClick snooze: " + snooze);
            mp.stop();
            Intent intent = new Intent("in.wptrafficanalyzer.servicealarmdemo.demoactivity");
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 545, intent, 0);

            AlarmManager alarmManagers = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            alarmManagers.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 6000, pendingIntent);

        }


        /** Creating the alert dialog window */
        return builder.create();

    }

    /**
     * The application should be exit, if the user presses the back button
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }

    public boolean getsnoze() {
        return snooze;
    }

    public void ha(MediaPlayer mp) {
        this.mp = mp;

    }

    public void alarm(Context context) {


    }

}