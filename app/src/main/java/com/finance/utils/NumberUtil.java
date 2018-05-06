package com.finance.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 数字处理工具类
 */
public class NumberUtil {

    public static double digitDouble(double d) {
        return digitDouble(d, 2);
    }

    public static double digitDouble(double d, int scale) {
        BigDecimal b = new BigDecimal(d);
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String digitFloatStr2(float f) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(f);
    }


}
