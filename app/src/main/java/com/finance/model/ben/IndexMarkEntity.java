package com.finance.model.ben;

import com.finance.utils.TimerUtil;
import com.github.mikephil.charting.data.Entry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * 指数实体
 */
public class IndexMarkEntity extends Entry {

    private int prod;
    private String time;//时间字符串
    private long timerLong;//时间戳
    private int isUpdatePoint;
    private String id;

    private IndexMarkEntity() {
    }

    public IndexMarkEntity(int x, int prod, long time, int exponentially, int isUpdatePoint, int decimalPointIndex, int digit, String data) {
        this.prod = prod;
        this.isUpdatePoint = isUpdatePoint;
        this.id = data;
        this.timerLong = formatTimeLong(time);
        setX(x);//将时间转换为下标
        setY(managerExponentially((float) exponentially, decimalPointIndex, digit));
    }

    public int getProd() {
        return this.prod;
    }

    public String getTime() {
        return formatTimeStr(timerLong);
    }

    public long getTimeLong() {
        return timerLong;
    }

    public double getExponentially() {
        return getY();
    }

    public int getIsUpdatePoint() {
        return this.isUpdatePoint;
    }

    public String getId() {
        return id;
    }

    public IndexMarkEntity copy() {
        IndexMarkEntity entity = new IndexMarkEntity();
        entity.time = time;
        entity.prod = prod;
        entity.timerLong = timerLong;
        entity.isUpdatePoint = isUpdatePoint;
        entity.id = id;
        entity.setX(getX());
        entity.setY(getY());
        entity.setData(getData());
        return entity;
    }

    public static float managerExponentially(double exponentially, int decimalPointIndex, int digit) {
        if (decimalPointIndex <= 0) {
            throw new Error("鎸囨暟鏍煎紡涓嶆\ue11c纭�");
        } else {
            double divisor = Math.pow(10.0D, (double) decimalPointIndex);
            double temp = exponentially / divisor;
            BigDecimal tem1;
            if (digit > 0) {
                tem1 = (new BigDecimal(temp)).setScale(digit, RoundingMode.HALF_EVEN);
            } else {
                tem1 = (new BigDecimal(temp)).setScale(2, RoundingMode.HALF_EVEN);
            }
            float back = Float.parseFloat(String.valueOf(tem1));
            return back;
        }
    }

    public static long getETimeToUTC(long eTime) {
        return eTime - 28800000L;
    }


    private static SimpleDateFormat sdf;

    private static SimpleDateFormat sdf2;

    public static long getTimeMillis(long time) {
        time -= 621355968000000000L;
        long str = time / 10000L;
        time = Long.parseLong(String.valueOf(str), 10);
        time = getETimeToUTC(time);
        getSimpleDateFormat();
        try {
            time = sdf.parse(sdf.format(time)).getTime();
        } catch (ParseException | IllegalArgumentException e) {
            e.printStackTrace();
            time = -1;
        }
        return time;
    }

    public static String formatTime(long time) {
        time -= 621355968000000000L;
        long str = time / 10000L;
        time = Long.parseLong(String.valueOf(str), 10);
        time -= 28800000L;
        getSimpleDateFormat();
        String sd = sdf.format(Long.valueOf(time));
        return sd;
    }

    public static String formatTimeStr(long time) {
//        String sd = sdf.format(String.valueOf(time));
        return TimerUtil.timerFormatStr(time);
    }

    public static long formatTimeLong(long time) {
        time -= 621355968000000000L;
        long str = time / 10000L;
        time = Long.parseLong(String.valueOf(str), 10);
        time -= 28800000L;
        getSimpleDateFormat();
        getSimpleDateFormat2();
        try {
            time = sdf2.parse(sdf.format(time)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            time = -1;
        }
        return time;
    }

    private static SimpleDateFormat getSimpleDateFormat() {
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.FFF");
//            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        }
        return sdf;
    }

    private static SimpleDateFormat getSimpleDateFormat2() {
        if (sdf2 == null) {
            sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.FFF");
//            sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return sdf2;
    }

}
