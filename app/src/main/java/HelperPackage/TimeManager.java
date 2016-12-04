package HelperPackage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Akash on 28-11-2016.
 */

public class TimeManager {

    static String[] MONTHS = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    public static String timeNow()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }

    public static String dateToday()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }


    public static String parseJoiningDate(String s)
    {

        String date = "";
        String[] date_array = s.split("-");
        date = date_array[2]+" "+ MONTHS[Integer.parseInt(date_array[1])-1];
        if(Integer.parseInt(date_array[0])!= new Date().getYear())
        {
            date+=" "+Integer.parseInt(date_array[0]);
        }
        return date;
    }

    public static String dateDifference(Date d1, Date d2)
    {


        long diff = d2.getTime() - d1.getTime();
        diff/=1000;
        long diffYear = diff / (12*30*24*60*60);
        diff %=(12*30*24*60*60);

        long diffMonth = diff /(30*24*60*60);
        diff%=(30*24*60*60);
        long diffDay = diff / (24*60*60);
        diff%=(24*60*60);
        long diffHours = diff / (60 * 60);
        diff%=(60*60);
        long diffMinutes = diff / 60;
        long diffSeconds = diff  % 60;
        String s="";
        if(diffYear>0)
        {
            s+=diffYear+" year ago";
            return s;
        }else if(diffMonth>0)
        {
            s+=diffMonth+" month ago";
            return s;
        }
        else
        if(diffDay>0)
        {
            s+=diffDay+" day ago";
            return s;
        }
        else
        if(diffHours>0)
        {
            s+=diffHours+" hour ago";
            return s;
        }
        else if(diffMinutes>0)
        {
            s+=diffMinutes+" minute ago";
            return s;
        }
        else
            return diffSeconds+" second ago";

    }

    public static Date stringToDate(String s)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d=df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static int monthNumber(String s)
    {
        int i,number=0;
        String[] months = {"Month","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        for(i=0;i<12;i++)
        {
            if(months[i].equalsIgnoreCase(s))
                number=(i);
        }
        return number;
    }
}
