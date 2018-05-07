package com.finance.interfaces;

import com.finance.model.ben.IndexMarkEntity;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.PurchaseViewEntity;
import com.github.mikephil.charting.data.Entry;

/**
 * MLineChart 事件处理
 */
public interface IChartListener extends IDestroy {

    IChartListener initListener();

    IndexMarkEntity getCurrentEntry();

    void addPurchaseView(PurchaseViewEntity entity);

    void clearPurchaseView();

    void updateIssue(IssueEntity entity);

    void setIChartData(IChartData iChartData);
}
