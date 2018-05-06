package com.finance.ui.main;


import com.finance.base.IBasePresenter;
import com.finance.base.IBaseView;
import com.finance.interfaces.ICallback;
import com.finance.interfaces.IDismiss;
import com.finance.model.ben.DynamicsEntity;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.OrdersEntity;
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

        void showOrderPopWindow(IDismiss dismiss);

        void showDynamicPopupWindow(IDismiss dismiss);

        void rise(int money);

        void fall(int money);

        //刷新期号
        void refreshIessue();
    }

    interface Presenter extends IBasePresenter<View> {
        int[] getProductIds(ArrayList<ProductEntity> products);

        ArrayList<IssueEntity> getProductIssue(int productId, ArrayList<IssueEntity> issues);

        void getProduct();

        void getProductIssue(int[] productIds);

        void getHistoryIssues(int ProductId, final ICallback<ArrayList<String>> callback);

        HttpConnection getAlwaysIssues(int ProductId, final BaseCallback callback);

        void getOrderRecord();

        String issueNameFormat(String issueName);

        void showProductPopWindow(android.view.View view, ArrayList<ProductEntity> entities);

        void showIssuePopWindow(android.view.View view, ArrayList<IssueEntity> entities, int selIndex);

        void showOrderPopWindow(android.view.View anchor, android.view.View leftView, int width, OrdersEntity entity, IDismiss dismiss);

        void showDynamicPopWindow(android.view.View anchor, android.view.View leftView, int width, DynamicsEntity entity, IDismiss dismiss);
    }

}
