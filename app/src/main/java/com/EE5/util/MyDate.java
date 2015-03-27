package com.EE5.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDate {

    public MyDate() {}

    /**
     * Returns the current date formatted according to ISO8601 standard. (yyyy-MM-dd HH:mm:ss)
     * @return Date as string. 
     */
    public static String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date); //2014-08-06 15:59:48
    }
}