package com.finance.ui.main;


import android.view.View;

import com.finance.base.IBasePresenter;
import com.finance.base.IBaseView;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.IssuesEntity;
import com.finance.model.ben.OrdersEntity;
import com.finance.model.ben.ProductEntity;

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
    }

    interface Presenter extends IBasePresenter<View> {
        int[] getProductIds(ArrayList<ProductEntity> products);

        ArrayList<IssueEntity> getProductIssue(int productId, ArrayList<IssueEntity> issues);

        void getProduct();

        void getProductIssue(int[] productIds);

        void getOrderRecord();

        String issueNameFormat(String issueName, StringBuilder sb);

        void showProductPopWindow(android.view.View view, ArrayList<ProductEntity> entities);

        void showIssuePopWindow(android.view.View view, ArrayList<IssueEntity> entities, int selIndex);

        void showOrderPopWindow(android.view.View view, OrdersEntity entity,int x,int y);

    }

}
