package com.finance.ui.main;


import com.finance.base.IBasePresenter;
import com.finance.base.IBaseView;
import com.finance.interfaces.ICallback;
import com.finance.interfaces.IDismiss;
import com.finance.model.ben.DynamicsEntity;
import com.finance.model.ben.HistoryIssueEntity;
import com.finance.model.ben.IndexMarkEntity;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.NotesMessage;
import com.finance.model.ben.OrdersEntity;
import com.finance.model.ben.PlaceOrderEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.model.ben.PurchaseViewEntity;
import com.finance.model.https.BaseCallback3;

import java.util.ArrayList;

/**
 * 首页接口
 */
public interface MainContract {

    interface View extends IBaseView {

        void product(ArrayList<ProductEntity> Products, String msg);

        void issue(ArrayList<IssueEntity> issues, String msg);

        void setProduct(ProductEntity product, int privation);

        void setIssue(IssueEntity issue, int issueSelIndex);

        int getStatusBarHigh();

        IndexMarkEntity getIndexMarkEntity(String indexMark);

        void showOrderPopWindow(IDismiss dismiss);

        void showDynamicPopupWindow(IDismiss dismiss);

        ArrayList<PurchaseViewEntity> getPurchase(int productId, String issue);

        //刷新期号
        void refreshIessueNextIssue();

        //刷新期号
        void refreshIessue();

        boolean isRefrshChartData();//是否在刷新走势图数据

        void placeOrder(PlaceOrderEntity entity, String msg);

        void openPrizeDialog(HistoryIssueEntity entity, String msg, IndexMarkEntity openIndex, int productId, String issue, String productName, int digit);

        IssueEntity getIssue(int productId, String issue);

//        IssueEntity getCountDownIssue();
//        void setShowOrder(int productId, String issueName);
//        void openIndex(OpenIndexEntity entity, String msg);
    }

    interface Presenter extends IBasePresenter<View> {
        int[] getProductIds(ArrayList<ProductEntity> products);

        ArrayList<IssueEntity> getProductIssue(int productId, ArrayList<IssueEntity> issues);

        void getProduct();

        void getProductIssue(int[] productIds);

//        void setProductIssue(ArrayList<IssueEntity> issues);
//
//        ArrayList<IssueEntity> getProductIssue();

        void getHistoryIssues(int ProductId, int timer, BaseCallback3 callback);

        void getOpenIndex(int ProductId, String productName, String issue, String Time, int digit);

        void getAlwaysIssues(int ProductId, BaseCallback3 callback);

        void getOrderRecord(int PageSize, int Page, ICallback<OrdersEntity> callback);

        void getDynamicPopupWindow(ICallback<DynamicsEntity> iCallback);

        IssueEntity getIssue(int productId, String issue, ArrayList<IssueEntity> issueEntities);

        void showProductPopWindow(android.view.View anchor, int x, int y, ArrayList<ProductEntity> entities, int selIndex, IDismiss dismiss);

        void showIssuePopWindow(android.view.View anchor, int x, int y, ArrayList<IssueEntity> entities, int selIndex, IDismiss dismiss);

        void showOrderPopWindow(android.view.View anchor, android.view.View leftView, int width, int y, IDismiss dismiss);

        void showDynamicPopWindow(android.view.View anchor, android.view.View leftView, int width, int y, IDismiss dismiss);

        void placeOrder(String Issue, int IssueType, int Money, int ProductId, boolean Result, String StrIndexMark);

        void receiveExperienceMoney();

        void notesMessage(ICallback<NotesMessage> callback);

        void unSubscribeProduct(int productId);//取消订阅

    }

}
