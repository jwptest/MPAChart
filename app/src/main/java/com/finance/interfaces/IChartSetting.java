package com.finance.interfaces;

import com.finance.linechartview.LineChartSetting;
import com.finance.widget.combinedchart.MCombinedChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * MLineChart 初始化配置接口
 */
public interface IChartSetting {

    IChartSetting initLineChart(BarLineChartBase lineChart, boolean isOffsets);

    LineChartSetting setRightIAxisValueFormatter(IAxisValueFormatter rightIAxisValueFormatter);


    LineChartSetting setXIAxisValueFormatter(IAxisValueFormatter XIAxisValueFormatter);

}
