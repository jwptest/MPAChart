package com.finance.interfaces;

import com.finance.model.ben.IndexMarkEntity;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.ProductEntity;
import com.github.mikephil.charting.data.Entry;

/**
 * 数据处理接口
 */
public interface IChartData extends IDestroy {

    //刷新期号
    void updateIssue(ProductEntity productEntity, IssueEntity issueEntity);

    void onResume(int type);

    Entry getEntry(String trim);

    IndexMarkEntity getIndexMarkEntity(String indexMark);

    boolean isRefrshChartData();//是否在刷新走势图数据

    void stopNetwork();//关闭网络连接

    long getStartTimer();//获取起始时间

    IndexMarkEntity getCurrentEntry();

}
