package com.finance.interfaces;

/**
 * 数据处理接口
 */
public interface ILineChartDataSetting extends IDestroy {

    ILineChartDataSetting onInit();

    void onResume();

    void onStop();
}
