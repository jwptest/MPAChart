package com.finance.ui.main;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.PopupWindow;

import com.finance.base.BasePresenter;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.IssuesEntity;
import com.finance.model.ben.ItemEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.model.ben.ProductsEntity;
import com.finance.model.http.BaseCallback;
import com.finance.model.http.BaseParams;
import com.finance.model.imps.NetworkRequest;
import com.finance.ui.popupwindow.IssuesPopupWindow;
import com.finance.ui.popupwindow.ProductPopupWindow;
import com.finance.ui.popupwindow.RecyclerPopupWindow;
import com.finance.utils.HandlerUtil;

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


    private ArrayList<ItemEntity<ProductEntity>> getProducts(ArrayList<ProductEntity> entities) {
        ArrayList<ItemEntity<ProductEntity>> products = new ArrayList<ItemEntity<ProductEntity>>(entities.size());
        for (ProductEntity entity : entities) {
            ItemEntity<ProductEntity> p = new ItemEntity<ProductEntity>();
            p.setData(entity);
            p.setTitle(entity.getProductName());
            p.setMessage(entity.getExpects() + "%");
            products.add(p);
        }
        return products;
    }

    private ProductPopupWindow mProductPopupWindow;
    private boolean isDismissProduct = true;//是否已经关闭产品
    private int productSelectIndex = -1;

    @Override
    public void showProductPopWindow(View view, ArrayList<ProductEntity> entities) {
        if (!isDismissProduct) {
            isDismissProduct = true;
            return;
        }
        ArrayList<ItemEntity<ProductEntity>> arrayList = getProducts(entities);
        if (mProductPopupWindow == null) {
            mProductPopupWindow = new ProductPopupWindow(mActivity, arrayList);
            mProductPopupWindow.setOnItemClicklistener(new RecyclerPopupWindow.OnItemClicklistener<ProductEntity>() {
                @Override
                public void onClickListener(int privation, ItemEntity<ProductEntity> itemEntity) {
                    if (mView == null) return;
                    productSelectIndex = privation;
                    mView.setProduct(itemEntity.getData());
                    mProductPopupWindow.dismiss();
                }
            });
            mProductPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    HandlerUtil.runOnUiThreadDelay(new Runnable() {
                        @Override
                        public void run() {
                            isDismissProduct = true;
                        }
                    }, 200);
                }
            });
        } else {
            mProductPopupWindow.setSelectIndex(productSelectIndex);
            mProductPopupWindow.setData(arrayList);
        }
        isDismissProduct = false;
        mProductPopupWindow.showBottom(view);
    }

    @Override
    public String issueNameFormat(String issueName, StringBuilder sb) {
        int length = TextUtils.isEmpty(issueName) ? 0 : issueName.length();
        if (length < 4) return issueName;
        sb.append(issueName.substring(issueName.length() - 4));
        sb.replace(2, 2, ":");
        return sb.toString();
    }

    private ArrayList<ItemEntity<IssueEntity>> getIssues(ArrayList<IssueEntity> entities) {
        ArrayList<ItemEntity<IssueEntity>> issues = new ArrayList<ItemEntity<IssueEntity>>(entities.size());
        StringBuilder sb = new StringBuilder();
        for (IssueEntity entity : entities) {
            sb.setLength(0);
            ItemEntity<IssueEntity> p = new ItemEntity<IssueEntity>();
            p.setData(entity);
            p.setTitle(issueNameFormat(entity.getIssueName(), sb));
            issues.add(p);
        }
        return issues;
    }

    private IssuesPopupWindow mIssuesPopupWindow;
    private boolean isDismissIssues = true;//是否已经关闭产品

    @Override
    public void showIssuePopWindow(View view, ArrayList<IssueEntity> entities, int selIndex) {
        if (!isDismissIssues) {
            isDismissIssues = true;
            return;
        }
        ArrayList<ItemEntity<IssueEntity>> arrayList = getIssues(entities);
        if (mIssuesPopupWindow == null) {
            mIssuesPopupWindow = new IssuesPopupWindow(mActivity, arrayList);
            mIssuesPopupWindow.setOnItemClicklistener(new RecyclerPopupWindow.OnItemClicklistener<IssueEntity>() {
                @Override
                public void onClickListener(int privation, ItemEntity<IssueEntity> itemEntity) {
                    if (mView == null) return;
                    mView.setIssue(itemEntity.getData(), privation);
                    mIssuesPopupWindow.dismiss();
                }
            });
            mIssuesPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    HandlerUtil.runOnUiThreadDelay(new Runnable() {
                        @Override
                        public void run() {
                            isDismissIssues = true;
                        }
                    }, 200);
                }
            });
        } else {
            mIssuesPopupWindow.setSelectIndex(selIndex);
            mIssuesPopupWindow.setData(arrayList);
        }
        isDismissIssues = false;
        mIssuesPopupWindow.showBottom(view);
    }


}
