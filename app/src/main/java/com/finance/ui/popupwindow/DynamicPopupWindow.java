package com.finance.ui.popupwindow;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.finance.R;
import com.finance.interfaces.IDismiss;
import com.finance.model.ben.DynamicEntity;
import com.finance.model.ben.DynamicsEntity;
import com.finance.model.ben.OrderEntity;
import com.finance.ui.adapters.DynamicAdapter;
import com.finance.ui.adapters.OrderAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 动态对话框
 */
public class DynamicPopupWindow extends PopupWindow {

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
    private IDismiss dismiss;

    private DynamicAdapter mAdapter;

    private DynamicsEntity mEntity;

    public DynamicPopupWindow(Activity activity, int width) {
        super(width, LinearLayout.LayoutParams.MATCH_PARENT);
        mActivity = activity;
        View rootView = LayoutInflater.from(activity).inflate(R.layout.popupwindow_order, null);
        setContentView(rootView);
        //PhoneUtil.getScreenHeight(activity) / 2
        ButterKnife.bind(this, rootView);
        setTouchable(true);
        setOutsideTouchable(true);   //设置外部点击关闭ppw窗口
        tvTitle.setText("交易动态");
        tvEmptyText.setText("暂无动态记录");
    }

    public void setEntity(DynamicsEntity entity) {
        mEntity = entity;
        initData();
    }

    @OnClick(R.id.ivClose)
    public void onViewClicked() {
        dismiss();
    }

    private void initData() {
        ArrayList<DynamicEntity> orderEntities = mEntity == null ? null : mEntity.getTrends() == null ? null : mEntity.getTrends();
        if (orderEntities == null || orderEntities.isEmpty()) {
            llEmpty.setVisibility(View.VISIBLE);
            rvOrder.setVisibility(View.GONE);
            return;
        }
        llEmpty.setVisibility(View.GONE);
        rvOrder.setVisibility(View.VISIBLE);
        if (mAdapter == null) {
            mAdapter = new DynamicAdapter(orderEntities);
            rvOrder.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
            rvOrder.setAdapter(mAdapter);

        } else {
            mAdapter.clear();
            mAdapter.addAll(orderEntities);
        }
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

}
