package com.yk.player.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {
    private static final String TAG = "TimeUtils";

    public static String getDuration(long duration) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        format.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return format.format(new Date(duration));
    }

    public static String getTime(long time) {
        Log.d(TAG, "getTime: " + time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(new Date(time));
    }
}
