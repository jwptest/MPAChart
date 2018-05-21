package com.finance.ui.main;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.finance.App;
import com.finance.base.BasePresenter;
import com.finance.common.Constants;
import com.finance.common.UserShell;
import com.finance.event.EventBus;
import com.finance.event.OpenPrizeDialogEvent;
import com.finance.interfaces.ICallback;
import com.finance.interfaces.IDismiss;
import com.finance.model.ben.DynamicsEntity;
import com.finance.model.ben.HistoryIssueEntity;
import com.finance.model.ben.IndexMarkEntity;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.IssuesEntity;
import com.finance.model.ben.ItemEntity;
import com.finance.model.ben.NotesMessage;
import com.finance.model.ben.OpenIndexEntity;
import com.finance.model.ben.OrdersEntity;
import com.finance.model.ben.PlaceOrderEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.model.ben.ProductsEntity;
import com.finance.model.http.BaseParams;
import com.finance.model.https.BaseCallback3;
import com.finance.model.https.JsonCallback;
import com.finance.model.https.RequestParams;
import com.finance.model.imps.NetworkRequest;
import com.finance.ui.dialog.ToastDialog;
import com.finance.ui.dialog.UpdateUserInfoDialog;
import com.finance.ui.popupwindow.DynamicPopupWindow;
import com.finance.ui.popupwindow.IssuesPopupWindow;
import com.finance.ui.popupwindow.OrderPopupWindow;
import com.finance.ui.popupwindow.ProductPopupWindow;
import com.finance.ui.popupwindow.RecyclerPopupWindow;
import com.finance.utils.HandlerUtil;
import com.finance.utils.IndexUtil;
import com.finance.utils.TimerUtil;

import java.util.ArrayList;

import static com.finance.model.imps.NetworkRequest.getRequestParamSignBasic;

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
        BaseParams params = new BaseParams(310);
        params.setTag(mActivity);
        NetworkRequest.getInstance()
                .getHttpConnection()
                .request(NetworkRequest.getRequestParamSignImp(params), new JsonCallback<ProductsEntity>(ProductsEntity.class) {
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
        BaseParams params = new BaseParams(302);
        params.addParam("ProductId", productIds);
        params.setTag(mActivity);
        NetworkRequest.getInstance()
                .getHttpConnection()
                .request(NetworkRequest.getRequestParamSignImp(params), new JsonCallback<IssuesEntity>(IssuesEntity.class) {
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

//    @Override
//    public void getHistoryIssues(int ProductId, int timer, final CallbackIssues callbackIssues) {
//        BaseParams param = new BaseParams();
//        param.addParam("T", 20);
//        param.addParam("D", ProductId + ":300");
//        param.addParam("isRate", true);//默认值
//        param.addParam("productId", ProductId);
//        param.addParam("times", timer);//默认值
//        param.addParam("Token", "");
////        param.addParam("Token", UserShell.getInstance().getUserToken());
//        NetworkRequest.getInstance()
//                .getHttpConnection()
//                .setTag(mActivity)
//                .setT(20)
//                .setISign(NetworkRequest.getInstance().getSignBasic())
//                .setToken(param.getToken())
//                .setParams(param)
//                .execute(callbackIssues);
//    }

    @Override
    public void getHistoryIssues(int ProductId, int timer, BaseCallback3 callback) {
        BaseParams param = new BaseParams();
        param.setT(20);
        param.setD(ProductId + ":300");
        param.addParam("isRate", true);//默认值
        param.addParam("productId", ProductId);
        param.addParam("times", timer);//默认值
        RequestParams requestParams = NetworkRequest.getRequestParamSignBasic(param);
        requestParams.setRate(true);//指数数据
        NetworkRequest.getInstance()
                .getHttpConnection()
                .request(requestParams, callback);
//                .setTag(mActivity)
//                .setT(20)
//                .setISign(NetworkRequest.getInstance().getSignBasic())
//                .setToken(param.getToken())
//                .setParams(param)
//                .execute(callbackIssues);

//        return NetworkRequest.getInstance()
//                .getHttpConnection()
//                .setTag(mActivity)
//                .setT(20)
//                .setISign(NetworkRequest.getInstance().getSignBasic())
//                .setToken(param.getToken())
//                .setParams(param)
//                .execute(new JsonCallback2<ArrayList<String>>(new TypeToken<ArrayList<String>>() {
//                }.getType()) {
//                    @Override
//                    public void onSuccessed(int code, String msg, boolean isFromCache, ArrayList<String> result) {
//                        callbackIssues.onCallback(code, result, msg);
//                    }
//
//                    @Override
//                    public void onFailed(int code, String msg, boolean isFromCache) {
//                        callbackIssues.onCallback(code, null, msg);
//                    }
//                });

    }

    //获取开奖数据
    private void getOpenData(IndexMarkEntity indexEntity, int ProductId, String Issue, String productName) {
        BaseParams param = new BaseParams(210);
        param.addParam("ProductId", ProductId);
        param.addParam("Issue", Issue);
        param.addParam("Second", 360);
        param.setTag(mActivity);
        NetworkRequest.getInstance()
                .getHttpConnection()
                .request(NetworkRequest.getRequestParamSignImp(param), new JsonCallback<HistoryIssueEntity>(HistoryIssueEntity.class) {
                    @Override
                    public void onSuccessed(int code, String msg, boolean isFromCache, HistoryIssueEntity result) {
                        if (mView == null) return;
                        mView.openPrizeDialog(result, msg, indexEntity, ProductId, Issue, productName);
                    }

                    @Override
                    public void onFailed(int code, String msg, boolean isFromCache) {
                        if (mView == null) return;
                        EventBus.post(new OpenPrizeDialogEvent(false));
                        mView.openPrizeDialog(null, msg, indexEntity, ProductId, Issue, productName);
                    }
                });
    }

    @Override
    public void getOpenIndex(final int ProductId, final String productName, final String issue, String Time) {
        BaseParams param = new BaseParams(12);
        param.addParam("ProductId", ProductId);
        param.addParam("Time", Time);//默认值
        param.setTag(mActivity);
        NetworkRequest.getInstance()
                .getHttpConnection()
                .request(NetworkRequest.getRequestParamSignImp(param), new JsonCallback<OpenIndexEntity>(OpenIndexEntity.class) {
                    @Override
                    public void onSuccessed(int code, String msg, boolean isFromCache, OpenIndexEntity result) {
                        if (mView == null) return;
                        IndexMarkEntity indexEntity = new IndexUtil().parseExponentially(0, result.getIndexMark(), Constants.INDEXDIGIT);
                        if (indexEntity == null) return;
                        new ToastDialog(mActivity, indexEntity.getY() + "").show();//显示指数
//                        App.getInstance().showErrorMsg(indexEntity.getY() + "");//显示指数
                        HandlerUtil.runOnUiThreadDelay(new Runnable() {
                            @Override
                            public void run() {
                                getOpenData(indexEntity, ProductId, issue, productName);
                            }
                        }, 1500);
//                        if (mView == null) return;
//                        IndexMarkEntity indexEntity = new IndexUtil().parseExponentially(0, result.getIndexMark(), Constants.INDEXDIGIT);
//                        if (indexEntity == null) return;
//                        mView.openIndex(result, msg);
                    }

                    @Override
                    public void onFailed(int code, String msg, boolean isFromCache) {
                        if (mView == null) return;
                        App.getInstance().showErrorMsg(msg);
                    }
                });
    }

    @Override
    public void getAlwaysIssues(int ProductId, BaseCallback3 callback) {
        BaseParams param = new BaseParams();
        param.setT(0);
        param.setD(ProductId + "");
        param.setTag(mActivity);
        RequestParams requestParams = getRequestParamSignBasic(param);
        requestParams.setRate(true);
        NetworkRequest.getInstance()
                .getHttpConnection()
                .request(requestParams, callback);

//                .getHttpConnection2()
//                .setTag(mActivity)
//                .setT(0)
//                .setISign(NetworkRequest.getInstance().getSignBasic())
//                .setToken(param.getToken())
//                .setParams(param)
//                .execute(callback);
    }

    @Override
    public void getOrderRecord(int PageSize, int Page, ICallback<OrdersEntity> iCallback) {
        BaseParams param = new BaseParams(202);
        param.addParam("Page", Page);
        param.addParam("PageSize", PageSize);
//        param.addParam("ProductId", "");
        param.addParam("ORderStatus", new int[]{20, 40});
//        param.addParam("OrderId", "");
//        param.addParam("BonusStatus", "");
        param.addParam("range", 0);
        param.setTag(mActivity);
        NetworkRequest.getInstance()
                .getHttpConnection()
                .request(NetworkRequest.getRequestParamSignImp(param), new JsonCallback<OrdersEntity>(OrdersEntity.class) {
                    @Override
                    public void onSuccessed(int code, String msg, boolean isFromCache, OrdersEntity result) {
                        if (iCallback != null)
                            iCallback.onCallback(code, result, msg);
                    }

                    @Override
                    public void onFailed(int code, String msg, boolean isFromCache) {
                        if (iCallback != null)
                            iCallback.onCallback(code, null, msg);
                    }
                });
//                .setTag(mActivity)
//                .setT(200)
//                .setToken(param.getToken())
//                .setParams(param)
//                .execute();
    }

    @Override
    public void getDynamicPopupWindow(final ICallback<DynamicsEntity> iCallback) {
        BaseParams param = new BaseParams(206);
        param.setTag(mActivity);
        NetworkRequest.getInstance()
                .getHttpConnection()
                .request(NetworkRequest.getRequestParamSignImp(param), new JsonCallback<DynamicsEntity>(DynamicsEntity.class) {
                    @Override
                    public void onSuccessed(int code, String msg, boolean isFromCache, DynamicsEntity result) {
                        if (iCallback != null)
                            iCallback.onCallback(code, result, msg);
                    }

                    @Override
                    public void onFailed(int code, String msg, boolean isFromCache) {
                        if (iCallback != null)
                            iCallback.onCallback(code, null, msg);
                    }
                });
//                .setTag(mActivity)
//                .setT(200)
//                .setToken(param.getToken())
//                .setParams(param)
//                .execute();
    }

    @Override
    public IssueEntity getIssue(int productId, String issue, ArrayList<IssueEntity> issueEntities) {
        if (productId == 0 || TextUtils.isEmpty(issue) || issueEntities == null || issueEntities.isEmpty())
            return null;
        for (IssueEntity entity : issueEntities) {
            if (entity.getProductId() == productId && TextUtils.equals(issue, entity.getIssueName())) {
                return entity;
            }
        }
        return null;
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

    private int productSelectIndex;

    @Override
    public void showProductPopWindow(View view, int x, int y, ArrayList<ProductEntity> entities, IDismiss dismiss) {
        if (entities == null || entities.isEmpty()) return;
        ArrayList<ItemEntity<ProductEntity>> arrayList = getProducts(entities);
        final ProductPopupWindow mProductPopupWindow = new ProductPopupWindow(mActivity, view.getWidth(), x, y, arrayList);
        mProductPopupWindow.setOnItemClicklistener(new RecyclerPopupWindow.OnItemClicklistener<ProductEntity>() {
            @Override
            public void onClickListener(int privation, ItemEntity<ProductEntity> itemEntity) {
                if (mView == null) return;
                productSelectIndex = privation;
                mView.setProduct(itemEntity.getData());
                mProductPopupWindow.dismiss();
            }
        });
        mProductPopupWindow.setDismiss(dismiss);
        mProductPopupWindow.setSelectIndex(productSelectIndex);
        mProductPopupWindow.showPopupWindow(view);
    }

    private ArrayList<ItemEntity<IssueEntity>> getIssues(ArrayList<IssueEntity> entities) {
        ArrayList<ItemEntity<IssueEntity>> issues = new ArrayList<ItemEntity<IssueEntity>>(entities.size());
        for (IssueEntity entity : entities) {
            ItemEntity<IssueEntity> p = new ItemEntity<IssueEntity>();
            p.setData(entity);
            p.setTitle(TimerUtil.getTimer(entity.getBonusTime()));
            issues.add(p);
        }
        return issues;
    }

    @Override
    public void showIssuePopWindow(View view, int x, int y, ArrayList<IssueEntity> entities, int selIndex, IDismiss dismiss) {
        if (entities == null || entities.isEmpty()) return;
        ArrayList<ItemEntity<IssueEntity>> arrayList = getIssues(entities);
        IssuesPopupWindow mIssuesPopupWindow = new IssuesPopupWindow(mActivity, view.getWidth(), x, y, arrayList);
        mIssuesPopupWindow.setOnItemClicklistener(new RecyclerPopupWindow.OnItemClicklistener<IssueEntity>() {
            @Override
            public void onClickListener(int privation, ItemEntity<IssueEntity> itemEntity) {
                if (mView == null) return;
                mView.setIssue(itemEntity.getData(), privation);
                mIssuesPopupWindow.dismiss();
            }
        });
        mIssuesPopupWindow.setDismiss(dismiss);
        mIssuesPopupWindow.setSelectIndex(selIndex);
        mIssuesPopupWindow.showPopupWindow(view);
    }

    @Override
    public void showOrderPopWindow(View anchor, View leftView, int width, int y, IDismiss dismiss) {
        OrderPopupWindow mOrderPopupWindow = new OrderPopupWindow(mActivity, mView, this, width, leftView.getWidth(), y);
        mOrderPopupWindow.setDismiss(dismiss);
        mOrderPopupWindow.showPopupWindow(anchor);
    }

    @Override
    public void showDynamicPopWindow(View anchor, View leftView, int width, int y, IDismiss dismiss) {
        DynamicPopupWindow mDynamicPopupWindow = new DynamicPopupWindow(mActivity, this, width, leftView.getWidth(), y);
        mDynamicPopupWindow.setDismiss(dismiss);
        mDynamicPopupWindow.showPopupWindow(anchor);
    }
//
//    //关闭对话框
//    private void dismiss(PopupWindow popupWindow) {
//        if (popupWindow != null && popupWindow.isShowing())
//            popupWindow.dismiss();
//    }

    @Override
    public void placeOrder(String Issue, int IssueType, int Money, int ProductId, boolean Result, String StrIndexMark) {
        if (UserShell.getInstance().getUserMoney() < Money) {
            //余额不足
            new UpdateUserInfoDialog(mActivity).show();
            return;
        }
        BaseParams baseParams = new BaseParams(201);
//        baseParams.addParam("IndexMark", IndexMark);
        baseParams.addParam("Issue", Issue);
        baseParams.addParam("IssueType", IssueType);
        baseParams.addParam("Money", Money);
        baseParams.addParam("ProductId", ProductId);
        baseParams.addParam("Result", Result);
        baseParams.addParam("StrIndexMark", StrIndexMark);
        baseParams.addParam("Ticks", 0);
        baseParams.addParam("taskid", "00000000-0000-0000-0000-000000000000");
        NetworkRequest.getInstance()
                .getHttpConnection()
                .request(NetworkRequest.getRequestParamSignImp(baseParams), new JsonCallback<PlaceOrderEntity>(PlaceOrderEntity.class) {
                    @Override
                    public void onSuccessed(int code, String msg, boolean isFromCache, PlaceOrderEntity result) {
                        if (mView == null) return;
                        mView.placeOrder(result, msg);
                    }

                    @Override
                    public void onFailed(int code, String msg, boolean isFromCache) {
                        if (mView == null) return;
                        mView.placeOrder(null, msg);
                    }
                });
//                .setTag(mActivity)
//                .setT(200)
//                .setToken(baseParams.getToken())
//                .setParams(baseParams)
//                .execute();
    }

    @Override
    public void notesMessage(final ICallback<NotesMessage> callback) {
        BaseParams baseParams = new BaseParams(206);
        baseParams.setTag(mActivity);
//        baseParams.addParam("Begin", "2018-05-11T11:24:30+08:00");//开始时间
//        baseParams.addParam("End", "2018-05-12T17:24:30+08:00");//结束时间
//        baseParams.addParam("Page", 1);
//        baseParams.addParam("PageSize", 20);
//        baseParams.addParam("StatusMsg", 0);
//        baseParams.addParam("TypeMsg", new int[]{0,1, 2});
        NetworkRequest.getInstance()
                .getHttpConnection()
                .request(NetworkRequest.getRequestParamSignImp(baseParams), new JsonCallback<String>(String.class) {
                    @Override
                    public void onSuccessed(int code, String msg, boolean isFromCache, String result) {
                        if (mView == null || callback == null) return;
                        callback.onCallback(code, null, msg);
                    }

                    @Override
                    public void onFailed(int code, String msg, boolean isFromCache) {
                        if (mView == null || callback == null) return;
                        callback.onCallback(code, null, msg);
                    }
                });
//                .setTag(mActivity)
//                .setT(200)
//                .setToken(baseParams.getToken())
//                .setParams(baseParams)
//                .execute();
    }

    @Override
    public void unSubscribeProduct(int productId) {
        BaseParams param = new BaseParams();
        param.setT(10);
        param.setD("");
        param.addParam("productId", productId);
        param.setTag(mActivity);
        NetworkRequest.getInstance()
                .getHttpConnection()
                .request(getRequestParamSignBasic(param), NetworkRequest.getBaseCallback());
//                .setTag(mActivity)
//                .setT(0)
//                .setISign(NetworkRequest.getInstance().getSignBasic())
//                .setToken(param.getToken())
//                .setParams(param)
//                .execute(BaseCallback2.getBaseCallback());
    }

}
