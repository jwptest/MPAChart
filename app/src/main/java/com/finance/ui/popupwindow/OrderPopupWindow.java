package com.finance.ui.popupwindow;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.finance.R;
import com.finance.common.Constants;
import com.finance.interfaces.ICallback;
import com.finance.listener.EventDistribution;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.OrderEntity;
import com.finance.model.ben.OrdersEntity;
import com.finance.model.datasource.DataSource;
import com.finance.ui.adapters.OrderAdapter;
import com.finance.ui.main.MainContract;
import com.finance.utils.TimerUtil;
import com.finance.widget.indexrecyclerview.expandRecyclerviewadapter.StickyRecyclerHeadersDecoration;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订单PopupWindow
 */
public class OrderPopupWindow extends LeftToRightPopupWindow implements EventDistribution.IPurchase, EventDistribution.IChartDraw, OrderAdapter.ICallback {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.llEmpty)
    View llEmpty;
    @BindView(R.id.tvEmptyText)
    TextView tvEmptyText;
    @BindView(R.id.rvOrder)
    RecyclerView rvOrder;

    private Activity mActivity;
    private MainContract.View mView;
    private MainContract.Presenter mPresenter;
    private boolean isRefesh = false;//是否刷新列表

    private OrderAdapter mAdapter;

    //    private CountDownTimer timer;//倒计时
//    private long serviceTimer;//服务器时间
    private OrderDataSource mDataSource;

    @OnClick(R.id.ivClose)
    public void onViewClicked() {
        dismiss();
    }

    public OrderPopupWindow(Activity activity, MainContract.View view, MainContract.Presenter presenter, int width, int x, int y) {
        super(activity, width, LinearLayout.LayoutParams.MATCH_PARENT, x, y);
        mActivity = activity;
        this.mView = view;
        this.mPresenter = presenter;
        setTouchable(true);
        setOutsideTouchable(true);   //设置外部点击关闭ppw窗口
        tvTitle.setText("交易订单");
        tvEmptyText.setText("暂无购买记录");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.popupwindow_order;
    }

    private void initData(ArrayList<OrderEntity> orderEntities) {
        if (orderEntities == null || orderEntities.isEmpty()) {
            llEmpty.setVisibility(View.VISIBLE);
            rvOrder.setVisibility(View.GONE);
            return;
        }
        llEmpty.setVisibility(View.GONE);
        rvOrder.setVisibility(View.VISIBLE);
        if (mAdapter == null) {
            mAdapter = new OrderAdapter(orderEntities);
            mAdapter.setICallback(this);//回调接口
            final LinearLayoutManager manager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
            rvOrder.setLayoutManager(manager);
            rvOrder.setAdapter(mAdapter);
            StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
            rvOrder.addItemDecoration(headersDecor);
            rvOrder.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState != RecyclerView.SCROLL_STATE_IDLE) return;
                    int lastVisiblePosition = manager.findLastVisibleItemPosition();
                    if (lastVisiblePosition < manager.getItemCount() - 1) return;
                    mDataSource.loadMore();//加载数据
                }
            });
        } else {
            mAdapter.clear();
            mAdapter.addAll(orderEntities);
        }
    }

    @Override
    protected boolean isBindView() {
        return true;
    }

    @Override
    public void dismiss() {
        EventDistribution.getInstance().removeChartDraws(this);
        EventDistribution.getInstance().removePurchase(this);
        super.dismiss();
    }

    public void showPopupWindow(View parent) {
        super.showPopupWindow(parent);
        if (mDataSource == null) {
            mDataSource = new OrderDataSource();
        }
        mDataSource.refresh();//加载数据
        EventDistribution.getInstance().addChartDraws(this);
        EventDistribution.getInstance().addPurchase(this);
    }

    @Override
    public void stopPurchase(boolean isOrder) {
//        if (isOrder)return;
    }

    @Override
    public void openPrize(boolean isOrder) {
        if (isOrder) return;
        dismiss();
    }

    @Override
    public void openPrize() {
        //到达开奖点
        if (mDataSource != null)
            mDataSource.refresh();//重新加载数据
    }

    @Override
    public long[] getIssueOpenTotalTime(int productId, String issueName) {
        //开奖时间和当前期总时长
        IssueEntity entity = mView.getIssue(productId, issueName);
        long[] openTotal = new long[]{1, 1};
        if (entity != null) {
            openTotal[0] = TimerUtil.timerToLong(entity.getBonusTime());//开奖时间
            openTotal[1] = TimerUtil.timerToLong(entity.getStartTime());//开始时间
            if (openTotal[1] < Constants.SERVERCURRENTTIMER) {
                openTotal[1] = (openTotal[0] - openTotal[1]) / 1000;
            } else {
                openTotal[1] = (openTotal[0] - Constants.SERVERCURRENTTIMER) / 1000;
            }
        }
        return openTotal;
    }

    @Override
    public long getServerTimer() {
        //服务器时间
        return Constants.SERVERCURRENTTIMER;
    }

    @Override
    public void onDraw(Entry entry) {
        if (mAdapter == null || !isRefesh) return;
        mAdapter.notifyDataSetChanged();
    }

    private class OrderDataSource extends DataSource implements ICallback<OrdersEntity> {

        private ArrayList<OrderEntity> mOrderEntities = new ArrayList<>(20);
        private long maxOpenTimer;//最大开奖时间

        public void refresh() {
            super.refresh();
            mOrderEntities.clear();
        }

        @Override
        public void load(int page, int size) {
            isRefesh = false;
            mPresenter.getOrderRecord(size, page, this);
        }

        @Override
        public void onCallback(int code, OrdersEntity ordersEntity, String message) {
            if (!isShowing()) return;
            if (ordersEntity == null || ordersEntity.getOrders().isEmpty()) {
                loadFail();
                return;
            }
//            serviceTimer = timerToLong(ordersEntity.getCurrDateTime());
            mOrderEntities.addAll(ordersEntity.getOrders());
            maxOpenTimer = 0;
            for (OrderEntity entity : mOrderEntities) {
                if (maxOpenTimer < entity.getOpenTimer()) maxOpenTimer = entity.getOpenTimer();
            }
            initData(mOrderEntities);
            long d = maxOpenTimer - getServerTimer();
            if (d > 0) isRefesh = true;
//            startCountDown(d);//启动倒计时
        }
    }

}
