package com.finance.interfaces;

import com.finance.model.ben.IndexMarkEntity;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.ProductEntity;
import com.github.mikephil.charting.data.Entry;

/**
 * 数据处理接口
 */
public interface IChartData extends IDestroy {

    void onResume(String type);
    //刷新期号
    void updateIssue(ProductEntity productEntity, IssueEntity issueEntity);

    Entry getEntry(String trim);

    IndexMarkEntity getIndexMarkEntity(String indexMark);

    boolean isRefrshChartData();//是否在刷新走势图数据

}
