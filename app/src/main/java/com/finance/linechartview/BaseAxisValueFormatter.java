package com.finance.linechartview;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * 框标签显示内容格式化类
 */
public class BaseAxisValueFormatter implements IAxisValueFormatter {

    protected float lasOne, lasTwo;

    protected DecimalFormat decimalFormat;

    public BaseAxisValueFormatter() {
        decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        lasTwo = lasOne;
        lasOne = value;
        return decimalFormat.format(value);
    }

    public float getLasOne() {
        return lasOne;
    }

    public float getLasTwo() {
        return lasTwo;
    }

}
