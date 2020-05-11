package com.tokyonth.clashpanel.utils;

public class TimeUtils {

    public static String secToTime(int time) {
        String timeStr;
        int hour;
        int minute;
        int second;
        if (time <= 0)
            return "00 : 00 : 00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00 : " + unitFormat(minute) + " : " + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99 : 59 : 59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + " : " + unitFormat(minute) + " : " + unitFormat(second);
            }
        }
        return timeStr;
    }

    private static String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + i;
        else
            retStr = "" + i;
        return retStr;
    }

}
