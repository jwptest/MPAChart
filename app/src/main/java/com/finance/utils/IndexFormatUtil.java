package com.finance.utils;

import java.text.DecimalFormat;

/**
 *
 */
public class IndexFormatUtil {

    protected DecimalFormat decimalFormat;

    public IndexFormatUtil(int digit) {
        if (digit < 2) digit = 2;
        StringBuilder sb = new StringBuilder(".");
        for (int i = 0; i < digit; i++) {
            sb.append("0");
        }
        decimalFormat = new DecimalFormat(sb.toString());//构造方法的字符格式这里如果小数不足2位,会以0补足.
    }

    public String format(float value) {
        return decimalFormat.format(value);
    }

}
