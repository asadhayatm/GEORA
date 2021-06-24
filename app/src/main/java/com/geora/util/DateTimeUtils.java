package com.geora.util;


import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    public static Date getDateFromString(String finalString) {
        Date finaldate = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.US);
        try {
            finaldate = format.parse(finalString);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return finaldate;
    }

    public static Date getDateFromStringForCycle(String finalString) {
        Date finaldate = null;
        SimpleDateFormat format = new SimpleDateFormat("MMMM dd,yyyy hh:mm aa", Locale.US);
        try {
            finaldate = format.parse(finalString);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return finaldate;
    }

    /*   public static String getStringFromDae(Date date) {
           SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd,yyyy hh:mm aa", Locale.US);

           return sdf.format(date);
       }

   */
    public static String getStringFromDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd,yyyy hh:mm aa", Locale.US);

        return sdf.format(date);
    }

    public static String getDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

        return sdf.format(date);
    }

    public static String getDateToStringCycle(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd", Locale.US);

        return sdf.format(date);
    }

    public static String getMonthFromDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.US);

        return sdf.format(date);
    }

    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);

        return sdf.format(date);
    }

    public static String setDateFromMillisecs(Integer created) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,yyyy", Locale.US);
        return formatter.format(new Date(created * 1000L));
    }
/*
    public static final Comparator<DateListModel> ascendingDate = new Comparator<DateListModel>() {
        public int compare(DateListModel ord1, DateListModel ord2) {
            Date d1 = ord1.getDueDate();
            Date d2 = ord2.getDueDate();
            //return (d1.getTime() > d2.getTime() ? -1 : 1);     //descending
            return (d1.getTime() > d2.getTime() ? 1 : -1);     //ascending
        }
    };*/

    public static String parseDOBDate(String dob) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "MMM dd, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dob);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public static String DOBtoSendFormatDate(String dob) {
        String inputPattern = "MMM dd, yyyy";
        String outputPattern =  "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dob);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public static String orderDateFormat(String orderDate) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern =  "MMM dd, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(orderDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static long bdayToTimeStamp(String bday) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        Date date = null;
        long timeStamp;
        try {
            date = (Date) formatter.parse(bday);
            timeStamp = date.getTime();
        } catch (ParseException e) {
            timeStamp = 0;
        }
        return timeStamp/1000;
    }

    public static String timeStampToString(String timeStamp) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(timeStamp)*1000);
        Date date = cal.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

        return sdf.format(date);
    }
    public static String timeStampToStartCamp(String timeStamp) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(timeStamp)*1000);
        Date date = cal.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", Locale.US);

        return sdf.format(date);
    }
    public static String timeStampToEndCamp(String timeStamp) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(timeStamp)*1000);
        Date date = cal.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.US);

        return sdf.format(date);
    }
    public static String timeStampToEndCampWithTime(String timeStamp) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(timeStamp)*1000);
        Date date = cal.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm aa", Locale.US);

        return sdf.format(date);
    }
    public static String timeStampToStratCampWithTime(String timeStamp) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(timeStamp)*1000);
        Date date = cal.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm aa", Locale.US);

        return sdf.format(date);
    }
    public static long dobToTimeStamp(String bday) {
        DateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy",Locale.US);
        Date date = null;
        long timeStamp;
        try {
            date = (Date) formatter.parse(bday);
            timeStamp = date.getTime()/1000;
        } catch (ParseException e) {
            timeStamp = 0;
        }
        return timeStamp;
    }
    public static long dobToTimeStampForPicker(String bday) {
        DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy",Locale.US);
        Date date = null;
        long timeStamp;
        try {
            date = (Date) formatter.parse(bday);
            timeStamp = date.getTime();
        } catch (ParseException e) {
            timeStamp = 0;
        }
        return timeStamp;
    }


    /**
     * function to get time count
     */
    public static String getTimeCount(String createdAt) {
        String time = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date mDate = sdf.parse(createdAt);
            long itemTime = mDate.getTime();
            long currentTime = Calendar.getInstance().getTimeInMillis();
            long difference = currentTime - itemTime;
            CharSequence ago = DateUtils.getRelativeTimeSpanString(itemTime, currentTime, DateUtils.MINUTE_IN_MILLIS);
            if (ago.equals("0 minutes ago")) ago = "Just now";
            time = ago.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return time;
    }
}
