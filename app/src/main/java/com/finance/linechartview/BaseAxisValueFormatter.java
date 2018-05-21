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

    public BaseAxisValueFormatter(int digit) {
        format = new IndexFormatUtil(digit);
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        lasTwo = lasOne;
        lasOne = value;
        return format.format(value);
    }

    public float getLasOne() {
        return lasOne;
    }

    public float getLasTwo() {
        return lasTwo;
    }

}
