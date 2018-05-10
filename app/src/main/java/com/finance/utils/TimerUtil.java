package com.finance.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public class TimerUtil {

//    private static final String sDateFormat = "yyyy-MM-ddTHH:mm:ss";

    public static String formatStr(String timer) {
        if (TextUtils.isEmpty(timer) || timer.length() < 19) throw new Error("时间格式错误");
        return timer.substring(0, 19).replace("T", " ");
    }

    public static long timerToLong(String timer) {
        timer = formatStr(timer);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return formatter.parse(timer).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String timerFormatStr(long timer) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date(timer));
    }

}
