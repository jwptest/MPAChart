package com.finance.interfaces;

import com.finance.model.ben.PurchaseViewEntity;
import com.github.mikephil.charting.data.Entry;

/**
 * MLineChart 事件处理
 */
public interface IChartListener extends IDestroy {

    IChartListener initListener();

    Entry getCurrentEntry();

    void addPurchaseView(PurchaseViewEntity entity);

    void clearPurchaseView();
}
