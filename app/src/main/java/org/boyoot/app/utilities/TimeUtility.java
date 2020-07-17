package org.boyoot.app.utilities;

import android.annotation.SuppressLint;

import org.boyoot.app.model.Branch;
import org.boyoot.app.model.CurrentCalenderDate;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtility {

    public static String dateFormatter(long appointment){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(appointment);
        String pattern = "YYYY-MM-dd  HH:mm";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Riyadh"));
        return format.format(calendar.getTime());

    }

    public static String getMonthOfYear(int year,int month){
        String y = String.valueOf(year);
        String m = String.valueOf(month);
        return y+m;
    }

    public static  String getDayName(CurrentCalenderDate c){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,c.getYear());
        calendar.set(Calendar.MONTH,c.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH,c.getDay());
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        return symbols.getWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)];
    }

    public static  String getDateFormat(CurrentCalenderDate c){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,c.getYear());
        calendar.set(Calendar.MONTH,c.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH,c.getDay());
        String pattern = "YYYY-MM-dd";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Riyadh"));
        return format.format(calendar.getTime());
    }

    public static Calendar calculateAppointmentFromOriginalDayStart(CurrentCalenderDate date, int start ){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Riyadh"));
        calendar.set(Calendar.YEAR,date.getYear());
        calendar.set(Calendar.MONTH,date.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH,date.getDay());
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.HOUR_OF_DAY,start);

        return calendar;
    }

    public static long calculateFinishTime(long appointment,int duration){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Riyadh"));
        calendar.setTimeInMillis(appointment);
        calendar.add(Calendar.MINUTE,duration);
        calendar.add(Calendar.MINUTE,5);
        return calendar.getTimeInMillis();
    }

    public static String getDurationText(int value){
        int h = value / 60;
        int m = value - (h*60);

        return h+" : "+m;
    }


    public static String getFinishTimeText(long value){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);
        String pattern = "YYYY-MM-dd  HH:mm";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Riyadh"));
        return format.format(calendar.getTime());
    }
}
