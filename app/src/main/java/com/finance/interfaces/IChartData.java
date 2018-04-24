package com.finance.interfaces;

/**
 * 数据处理接口
 */
public interface IChartData extends IDestroy {

    IChartData onInit();

    void onResume(String type);

    void onStop();
}
