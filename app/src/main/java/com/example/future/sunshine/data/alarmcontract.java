package com.example.future.sunshine.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class alarmcontract {
    public static final String CONTENT_AUTHORITY = "com.example.future.sunshine";

    private  static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ALARM = "alarm";

    private alarmcontract() {
    }

    /**
     * Inner class that defines constant values for the students database table.
     * Each entry in the table represents a single student.
     */
    public static final class AlarmEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ALARM);


        public final static String TABLE_NAME = "alarms";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_ALARM_NAME = "name";
        public final static String COLUMN_ALARM_DATE = "date";
        public final static String COLUMN_ALARM_TIME = "time";
        public final static String COLUMN_ALARM_SNOOZE= "snooze";

    }



}
