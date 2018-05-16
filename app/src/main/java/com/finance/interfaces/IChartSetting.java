package com.finance.interfaces;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * MLineChart 初始化配置接口
 */
public interface IChartSetting {

    IChartSetting initLineChart(BarLineChartBase lineChart, boolean isOffsets);

    IChartSetting setRightIAxisValueFormatter(IAxisValueFormatter rightIAxisValueFormatter);

    IChartSetting setXIAxisValueFormatter(IAxisValueFormatter XIAxisValueFormatter);

}
