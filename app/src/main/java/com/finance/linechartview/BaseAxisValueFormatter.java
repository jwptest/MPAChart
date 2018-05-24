package com.finance.linechartview;

import com.finance.utils.IndexFormatUtil;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * 框标签显示内容格式化类
 */
public class BaseAxisValueFormatter implements IAxisValueFormatter {

    protected float lasOne, lasTwo;
    protected IndexFormatUtil format;

    public BaseAxisValueFormatter() {

    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        lasTwo = lasOne;
        lasOne = value;
        if (format == null) {
            return value + "";
        }
        return format.format(value);
    }

    public float getLasOne() {
        return lasOne;
    }

    public float getLasTwo() {
        return lasTwo;
    }


    public void setDigit(int digit) {
        format = new IndexFormatUtil(digit);
    }

}
