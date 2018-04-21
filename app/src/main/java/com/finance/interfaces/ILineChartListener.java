package com.finance.interfaces;

import com.finance.ben.PurchaseViewEntity;
import com.github.mikephil.charting.data.Entry;

/**
 * MLineChart 事件处理
 */
public interface ILineChartListener extends IDestroy {

    ILineChartListener initListener();

    Entry getCurrentEntry();

    void addPurchaseView(PurchaseViewEntity entity);

    void clearPurchaseView();
}
