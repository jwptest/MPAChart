package com.finance.interfaces;

import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.model.ben.PurchaseViewEntity;

import java.util.ArrayList;

/**
 * MLineChart 事件处理
 */
public interface IChartListener extends IDestroy {

    void onResume(String type);

    IChartListener initListener();

    void addPurchaseView(PurchaseViewEntity entity);

    void updateProductIssue(ProductEntity productEntity, IssueEntity issueEntity, IssueEntity oneIssueEntity);

    void setIChartData(IChartData iChartData);

//    void setShowOrder(int productId, String issueName);
//
//    void setOtherIssue(boolean isOtherIssue);

    ArrayList<PurchaseViewEntity> getPurchase(int productId, String issue);

    void hideView();

    void initView();

}
