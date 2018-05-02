package com.finance.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * ===============================
 * 描    述：
 * 作    者：pjw
 * 创建日期：2018/5/2 17:57
 * ===============================
 */
public class TimerUtil {

    private static final String sDateFormat = "yyyy-MM-ddTHH:mm:ss";

    private static String formatStr(String timer) {
        if (TextUtils.isEmpty(timer) || timer.length() < 19) throw new Error("时间格式错误");
        return timer.substring(0, 19);
    }

    public static long timerFormat(String timer) {
        timer = formatStr(timer);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
        try {
            return formatter.parse(timer).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String timerFormatStr(String timer) {
        timer = formatStr(timer);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
        try {
            formatter.
            return formatter.parse(timer);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
