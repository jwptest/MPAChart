package com.finance.widget.combinedchart;

import com.github.mikephil.charting.interfaces.datasets.IDataSet;

/**
 * 图表绘制完成回调
 */
public interface OnDrawCompletion {

    /**
     * 图表绘制完成回调
     */
    void completion(float X, float Y, IDataSet dataSet);

}
