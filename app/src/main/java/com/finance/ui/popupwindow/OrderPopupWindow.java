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
import com.finance.interfaces.IDismiss;
import com.finance.model.ben.OrderEntity;
import com.finance.model.ben.OrdersEntity;
import com.finance.ui.adapters.OrderAdapter;
import com.finance.widget.indexrecyclerview.expandRecyclerviewadapter.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订单PopupWindow
 */
public class OrderPopupWindow extends BasePopupWindow {

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

    private OrderAdapter mAdapter;
    private OrdersEntity ordersEntity;

    private CountDownTimer timer;//倒计时
    private long serviceTimer;//服务器时间
    private IDismiss dismiss;

    @OnClick(R.id.ivClose)
    public void onViewClicked() {
        dismiss();
    }

    public OrderPopupWindow(Activity activity, int width, int x, int y) {
        super(activity, width, LinearLayout.LayoutParams.MATCH_PARENT, x, y);
        mActivity = activity;
        setTouchable(true);
        setOutsideTouchable(true);   //设置外部点击关闭ppw窗口
        tvTitle.setText("交易订单");
        tvEmptyText.setText("暂无购买记录");

        OrdersEntity entity = new OrdersEntity();
        ArrayList<OrderEntity> entities = new ArrayList<>(10);
        entities.add(new OrderEntity());
        entities.add(new OrderEntity());
        entities.add(new OrderEntity());
        entities.add(new OrderEntity());
        entities.add(new OrderEntity());
        entities.add(new OrderEntity());
        entities.add(new OrderEntity());
        entities.add(new OrderEntity());
        entities.add(new OrderEntity());
        entities.add(new OrderEntity());
        entity.setOrders(entities);
        setOrdersEntity(entity);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.popupwindow_order;
    }

    private void setOrdersEntity(OrdersEntity ordersEntity) {
        this.ordersEntity = ordersEntity;
        initData();
    }

    private void initData() {
        ArrayList<OrderEntity> orderEntities = ordersEntity == null ? null : ordersEntity.getOrders() == null ? null : ordersEntity.getOrders();
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
            rvOrder.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
            rvOrder.setAdapter(mAdapter);
            StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
            rvOrder.addItemDecoration(headersDecor);
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

    public void showBottom(View parent) {
        showAsDropDown(parent);
    }

}
