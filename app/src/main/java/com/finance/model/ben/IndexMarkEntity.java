package com.finance.model.ben;

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
    private String time;//时间戳
    private int isUpdatePoint;
    private String id;

    private IndexMarkEntity() {
    }

    public IndexMarkEntity(int x, int prod, long time, int exponentially, int isUpdatePoint, int decimalPointIndex, int digit, String data) {
        this.prod = prod;
        this.isUpdatePoint = isUpdatePoint;
        this.id = data;
        this.time = formatTime(time);
        setX(x);//将时间转换为下标
        setY(managerExponentially((float) exponentially, decimalPointIndex, digit));
    }

    public int getProd() {
        return this.prod;
    }

    public String getTime() {
        return time;
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
        entity.isUpdatePoint = isUpdatePoint;
        entity.id = id;
        entity.setX(getX());
        entity.setY(getY());
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
//        System.out.println(time);
        time -= 621355968000000000L;
        long str = time / 10000L;
        time = Long.parseLong(String.valueOf(str), 10);
        time -= 28800000L;
        getSimpleDateFormat();
        String sd = sdf.format(Long.valueOf(time));
        return sd;
    }

    private static SimpleDateFormat getSimpleDateFormat() {
        if (sdf == null) {
//            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.FFF");
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        }
        return sdf;
    }

}
