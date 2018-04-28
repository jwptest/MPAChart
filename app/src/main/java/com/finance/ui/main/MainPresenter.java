package com.finance.ui.main;

import android.app.Activity;

import com.finance.base.BasePresenter;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.IssuesEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.model.ben.ProductsEntity;
import com.finance.model.http.BaseCallback;
import com.finance.model.http.BaseParams;
import com.finance.model.imps.NetworkRequest;

import java.util.ArrayList;

/**
 * 首页逻辑处理
 */
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    private Activity mActivity;

    public MainPresenter(Activity activity, MainContract.View view) {
        super(view);
        this.mActivity = activity;
    }

    @Override
    public int[] getProductIds(ArrayList<ProductEntity> products) {
        if (products == null || products.isEmpty()) return null;
        int[] ids = new int[products.size()];
        int count = 0;
        for (ProductEntity entity : products) {
            ids[count] = entity.getProductId();
            count++;
        }
        return ids;
    }

    @Override
    public ArrayList<IssueEntity> getProductIssue(int productId, ArrayList<IssueEntity> issues) {
        if (issues == null || issues.isEmpty()) return null;
        ArrayList<IssueEntity> issueEntities = new ArrayList<>(5);
        for (IssueEntity entity : issues) {
            if (entity.getProductId() != productId) continue;
            issueEntities.add(entity);
        }
        return issueEntities;
    }

    @Override
    public void getProduct() {
        BaseParams baseParams = new BaseParams();
        baseParams.addParam("SourceCode", 310);
        NetworkRequest.getInstance()
                .getHttpConnection()
                .setTag(mActivity)
                .setT(200)
                .setToken(baseParams.getToken())
                .setParams(baseParams)
                .execute(new BaseCallback<ProductsEntity>(ProductsEntity.class) {
                    @Override
                    public void onSuccessed(int code, String msg, boolean isFromCache, ProductsEntity result) {
                        if (mView == null) return;
                        mView.product(result.getProducts(), msg);
                    }

                    @Override
                    public void onFailed(int code, String msg, boolean isFromCache) {
                        if (mView == null) return;
                        mView.product(null, msg);
                    }
                });
    }

    public void getProductIssue(int[] productIds) {
        if (productIds == null || productIds.length == 0) return;
        BaseParams baseParams = new BaseParams();
        baseParams.addParam("SourceCode", 302);
        baseParams.addParam("ProductId", productIds);
        NetworkRequest.getInstance()
                .getHttpConnection()
                .setTag(mActivity)
                .setT(200)
                .setToken(baseParams.getToken())
                .setParams(baseParams)
                .execute(new BaseCallback<IssuesEntity>(IssuesEntity.class) {
                    @Override
                    public void onSuccessed(int code, String msg, boolean isFromCache, IssuesEntity result) {
                        if (mView == null) return;
                        mView.issue(result.getIssueInfo(), msg);
                    }

                    @Override
                    public void onFailed(int code, String msg, boolean isFromCache) {
                        if (mView == null) return;
                        mView.issue(null, msg);
                    }
                });
    }

}
