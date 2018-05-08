package com.finance.ui.main;


import com.finance.base.IBasePresenter;
import com.finance.base.IBaseView;
import com.finance.interfaces.ICallback;
import com.finance.interfaces.IDismiss;
import com.finance.model.ben.DynamicsEntity;
import com.finance.model.ben.IndexMarkEntity;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.OrdersEntity;
import com.finance.model.ben.PlaceOrderEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.model.http.BaseCallback;
import com.finance.model.http.HttpConnection;

import java.util.ArrayList;

/**
 * 首页接口
 */
public interface MainContract {

    interface View extends IBaseView {

        void product(ArrayList<ProductEntity> Products, String msg);

        void issue(ArrayList<IssueEntity> issues, String msg);

        void setProduct(ProductEntity product);

        void setIssue(IssueEntity issue, int issueSelIndex);

        int getStatusBarHigh();

        IndexMarkEntity getIndexMarkEntity(String indexMark);

        void showOrderPopWindow(IDismiss dismiss);

        void showDynamicPopupWindow(IDismiss dismiss);

        //刷新期号
        void refreshIessue();

        boolean isRefrshChartData();//是否在刷新走势图数据

        void placeOrder(PlaceOrderEntity entity, String msg);
    }

    interface Presenter extends IBasePresenter<View> {
        int[] getProductIds(ArrayList<ProductEntity> products);

        ArrayList<IssueEntity> getProductIssue(int productId, ArrayList<IssueEntity> issues);

        void getProduct();

        void getProductIssue(int[] productIds);

        void getHistoryIssues(int ProductId, final ICallback<ArrayList<String>> callback);

        HttpConnection getAlwaysIssues(int ProductId, final BaseCallback callback);

        void getOrderRecord();

        void getDynamicPopupWindow(ICallback<DynamicsEntity> iCallback);

        String issueNameFormat(String issueName);

        void showProductPopWindow(android.view.View view, int x, int y, ArrayList<ProductEntity> entities);

        void showIssuePopWindow(android.view.View view, int x, int y, ArrayList<IssueEntity> entities, int selIndex);

        void showOrderPopWindow(android.view.View anchor, android.view.View leftView, int width, int y, IDismiss dismiss);

        void showDynamicPopWindow(android.view.View anchor, android.view.View leftView, int width, int y, IDismiss dismiss);

        void placeOrder(String Issue, int IssueType, int Money, int ProductId, boolean Result, String StrIndexMark);
    }

}
