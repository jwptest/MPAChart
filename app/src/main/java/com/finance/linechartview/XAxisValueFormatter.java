package com.finance.linechartview;

import com.github.mikephil.charting.components.AxisBase;

/**
 * X轴显示标签格式类
 */
public class XAxisValueFormatter extends BaseAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        lasTwo = lasOne;
        lasOne = value;
        return ((int) value) + "";
    }

}
