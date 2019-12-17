package com.zhenlong.Fitness.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTransfer {
    private static final SimpleDateFormat sdf;
    static {
       sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
       sdf.setTimeZone(TimeZone.getTimeZone("GMT+11"));
    }

    public static String dateToString(Date date){
        return sdf.format(date);
    }

    public static Date stringToDate(String dateString){
        Date date = null;
        try {
           date =  sdf.parse(dateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}
