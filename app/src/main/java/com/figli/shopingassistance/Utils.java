package com.figli.shopingassistance;

import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;

/**
 * Created by Figli on 11.02.2016.
 */
public class Utils {

    public static String getDate(long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }

    public static String getTime(long time) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(time);
    }

    public static String getFullDate (long date) {
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
        return fullDateFormat.format(date);
    }
}
