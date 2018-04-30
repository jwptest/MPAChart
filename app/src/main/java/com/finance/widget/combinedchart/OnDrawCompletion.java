package com.finance.widget.combinedchart;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

/**
 * 图表绘制完成回调
 */
public interface OnDrawCompletion {

    /**
     * 图表绘制完成回调
     *
     * @param lastEntry 绘制的最后一个点
     * @param dataSet   当前绘制的数据
     */
    void completion(Entry lastEntry, IDataSet dataSet);

}
