package com.finance.interfaces;

import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.ProductEntity;
import com.github.mikephil.charting.data.Entry;

/**
 * 数据处理接口
 */
public interface IChartData extends IDestroy {

    IChartData onInit();

    void onResume(String type);

    void onStop();

    //刷新期号
    void updateIssue(ProductEntity productEntity, IssueEntity issueEntity);

    Entry getEntry(String trim);

    boolean isRefrshChartData();//是否在刷新走势图数据

}
