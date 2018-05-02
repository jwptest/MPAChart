package com.finance.ui.popupwindow;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.finance.R;
import com.finance.model.ben.OrdersEntity;
import com.finance.ui.adapters.OrderAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单PopupWindow
 */
public class OrderPopupWindow extends PopupWindow {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.rvOrder)
    RecyclerView rvOrder;
    private Activity mActivity;

    private OrderAdapter mAdapter;
    private OrdersEntity ordersEntity;

    private CountDownTimer timer;//倒计时
    private long serviceTimer;//服务器时间

    public OrderPopupWindow(Activity activity, OrdersEntity ordersEntity, int width) {
        super(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        mActivity = activity;
        View rootView = LayoutInflater.from(activity).inflate(R.layout.popupwindow_order, null);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        this.ordersEntity = ordersEntity;
        setTouchable(true);
        setOutsideTouchable(true);   //设置外部点击关闭ppw窗口
        initData();
    }

    @OnClick(R.id.ivClose)
    public void onViewClicked() {
        dismiss();
    }

    private void initData() {
        mAdapter = new OrderAdapter(ordersEntity.getOrders());
        mAdapter.setServiceTimer(System.currentTimeMillis());
        rvOrder.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        rvOrder.setAdapter(mAdapter);
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


    public void showBottom(View parent) {
        showAsDropDown(parent);
    }

}
