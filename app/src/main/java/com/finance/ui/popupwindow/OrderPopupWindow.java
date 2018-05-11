package com.finance.ui.popupwindow;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.finance.R;
import com.finance.interfaces.ICallback;
import com.finance.interfaces.IDismiss;
import com.finance.model.ben.OrderEntity;
import com.finance.model.ben.OrdersEntity;
import com.finance.model.datasource.DataSource;
import com.finance.ui.adapters.OrderAdapter;
import com.finance.ui.main.MainContract;
import com.finance.widget.indexrecyclerview.expandRecyclerviewadapter.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订单PopupWindow
 */
public class OrderPopupWindow extends LeftToRightPopupWindow {

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
    private MainContract.Presenter mPresenter;

    private OrderAdapter mAdapter;
    private OrdersEntity ordersEntity;

    private CountDownTimer timer;//倒计时
    private long serviceTimer;//服务器时间
    private IDismiss dismiss;
    private OrderDataSource mDataSource;

    @OnClick(R.id.ivClose)
    public void onViewClicked() {
        dismiss();
    }

    public OrderPopupWindow(Activity activity, MainContract.Presenter presenter, int width, int x, int y) {
        super(activity, width, LinearLayout.LayoutParams.MATCH_PARENT, x, y);
        mActivity = activity;
        this.mPresenter = presenter;
        setTouchable(true);
        setOutsideTouchable(true);   //设置外部点击关闭ppw窗口
        tvTitle.setText("交易订单");
        tvEmptyText.setText("暂无购买记录");
//        OrdersEntity entity = new OrdersEntity();
//        ArrayList<OrderEntity> entities = new ArrayList<>(10);
//        entities.add(new OrderEntity());
//        entities.add(new OrderEntity());
//        entities.add(new OrderEntity());
//        entities.add(new OrderEntity());
//        entities.add(new OrderEntity());
//        entities.add(new OrderEntity());
//        entities.add(new OrderEntity());
//        entities.add(new OrderEntity());
//        entities.add(new OrderEntity());
//        entities.add(new OrderEntity());
//        entity.setOrders(entities);
//        setOrdersEntity(entity);
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
            mAdapter.setServiceTimer(System.currentTimeMillis());
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

    private void startCountDown(final long l) {
        if (timer != null) timer.cancel();
        timer = new CountDownTimer(l, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                serviceTimer = serviceTimer + (l - millisUntilFinished);
                mAdapter.setServiceTimer(serviceTimer);
                if (isShowing()) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFinish() {
                dismiss();
            }
        };
        timer.start();
    }

    @Override
    protected boolean isBindView() {
        return true;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (dismiss != null)
            dismiss.onDismiss();
    }

    public void setOnDismiss(IDismiss dismiss) {
        this.dismiss = dismiss;
    }

    public void showPopupWindow(View parent) {
        super.showPopupWindow(parent);
        if (mDataSource == null) {
            mDataSource = new OrderDataSource();
        }
        mDataSource.refresh();//加载数据
    }

    private class OrderDataSource extends DataSource implements ICallback<OrdersEntity> {

        ArrayList<OrderEntity> mOrderEntities = new ArrayList<>(20);

        public void refresh() {
            super.refresh();
            mOrderEntities.clear();
        }

        @Override
        public void load(int page, int size) {
            mPresenter.getOrderRecord(page, size, this);
        }

        @Override
        public void onCallback(int code, OrdersEntity ordersEntity, String message) {
            if (ordersEntity == null || ordersEntity.getOrders().isEmpty()) {
                loadFail();
                return;
            }
            mOrderEntities.addAll(ordersEntity.getOrders());
            initData(mOrderEntities);
        }
    }

}
