package com.maciejkrolik.meteostats.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StringUtils {

    public static String getTodayDateAsString() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return simpleDateFormat.format(date);
    }

    public static String headingToString(float x) {
        String directions[] = {"S", "SW", "W", "NW", "N", "NE", "E", "SE", "S"};
        return directions[Math.round(((x % 360) / 45))];
    }

    public static String convertUTCToLocalTime(String time) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getTimeZone("Poland"));
        return df.format(date).substring(10, 13);
    }
}
