package com.finance.model.ben;

import com.github.mikephil.charting.data.Entry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static android.R.attr.data;

/**
 * 指数实体
 */
public class IndexMarkEntity extends Entry {

    private int prod;
    private String time;
    private int isUpdatePoint;
    private String id;

    public IndexMarkEntity(int x, int prod, long time, int exponentially, int isUpdatePoint, int decimalPointIndex, int digit, String data) {
        this.prod = prod;
        this.time = formatTime(time);
        this.isUpdatePoint = isUpdatePoint;
        this.id = data;
        setX(x);
        setY(managerExponentially((float) exponentially, decimalPointIndex, digit));
    }

    public IndexMarkEntity(int x, int prod, String time, float exponentially, int isUpdatePoint, String data) {
        this.prod = prod;
        this.time = time;
        this.isUpdatePoint = isUpdatePoint;
        this.id = data;
        setX(x);
        setY(exponentially);
    }

    public int getProd() {
        return this.prod;
    }

    public String getTime() {
        return this.time;
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

    public static String formatTime(long time) {
        System.out.println(time);
        long unknown1 = 621355968000000000L;
        long unknown2 = 28800000L;
        time -= 621355968000000000L;
        long str = time / 10000L;
        time = Long.parseLong(String.valueOf(str), 10);
        time -= 28800000L;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String sd = sdf.format(Long.valueOf(time));
        return sd;
    }
}
