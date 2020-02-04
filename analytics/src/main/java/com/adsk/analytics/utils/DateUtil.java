package com.adsk.analytics.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by aenugav on 6/2/2015.
 */
public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    //TODO: should this method be moved to DateTimeUtils class?
    public static String getMonthStartDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        java.util.Date parse = null;

        try {
            parse = sdf.parse(date);
        } catch (ParseException e) {
            logger.error(" getMonthStartDate () : {} ", e);
        }

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(parse);

        calendar.set(Calendar.DAY_OF_MONTH, 1);

        String lastDayOfMonth = sdf.format(calendar.getTime());

        return lastDayOfMonth;
    }

    public static String getMonthEndDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        java.util.Date parse = null;

        try {
            parse = sdf.parse(date);
        } catch (ParseException e) {
            logger.error(" getMonthEndDate () : {} ", e);
        }

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(parse);

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        String lastDayOfMonth = sdf.format(calendar.getTime());

        return lastDayOfMonth;
    }

    //mmyyyy format
    public static String getMonthYearFromDate(String date) {
        String month;
        String year;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        java.util.Date parse = null;

        try {
            parse = sdf.parse(date);
        } catch (ParseException e) {
            logger.error(" getMonthYearFromDate () : {} ", e);
        }

        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.setTime(parse);
        if (c.get(Calendar.MONTH) > 10)
            month = String.valueOf(c.get(Calendar.MONTH) + 1);
        else
            month = "0" + (c.get(Calendar.MONTH) + 1);

        year = String.valueOf(c.get(Calendar.YEAR));

        return month + year;
    }

    public static String addMonth(String date, int interval) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            logger.error(" addMonth () : {} ", e);
        }
        c.add(Calendar.MONTH, interval);  // number of days to add
        date = sdf.format(c.getTime());  // dt is now the new date

        return date;
    }

    public static java.sql.Date getDateDiffWithInterval(int interval) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        java.sql.Date sqlDate;
        java.util.Date date = null;

        cal.add(Calendar.DATE, interval);
        try {
            date = dateFormat.parse(dateFormat.format(cal.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
    }

    public static String getDateRangeByInterval(int interval) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Date date = null;

        String newDate = "";
        cal.add(Calendar.DATE, interval);

        newDate = dateFormat.format(cal.getTime());

        return newDate;
    }

    public static String getDateRangeByInterval(int intervalType, int interval) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        String newDate = "";
        cal.add(intervalType, interval);

        newDate = dateFormat.format(cal.getTime());

        return newDate;
    }

    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        return dateFormat.format(cal.getTime());
    }

    public static String getDateRangeByInterval(String date, int interval) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        try {
            cal.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            logger.error(" getDateRangeByInterval () : {} ", e);
        }
        String newDate = "";
        cal.add(Calendar.DATE, interval);

        newDate = dateFormat.format(cal.getTime());

        return newDate;
    }

    public static long getDateDifferenceInDays(String dateString1, String dateString2) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date1, date2;
        long dateDiffDays = 0;

        try {
            date1 = dateFormat.parse(dateString1);
            date2 = dateFormat.parse(dateString2);
            long dateDiff = date2.getTime() - date1.getTime();
            dateDiffDays = dateDiff / (1000 * 60 * 60 * 24);

        } catch (ParseException e) {
            logger.error(" getDateDifferenceInDays () : {} ", e);
        }

        return dateDiffDays;
    }

    public static String getTime(String date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        try {
            cal.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            logger.error(" getDateRangeByInterval () : {} ", e);
        }

        String newDate = "";
        newDate = Long.toString(cal.getTimeInMillis());

        return newDate;
    }

}
