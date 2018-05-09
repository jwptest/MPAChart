package com.finance.ui.popupwindow;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.finance.App;
import com.finance.R;
import com.finance.interfaces.ICallback;
import com.finance.interfaces.IDismiss;
import com.finance.model.ben.DynamicEntity;
import com.finance.model.ben.DynamicsEntity;
import com.finance.ui.adapters.DynamicAdapter;
import com.finance.ui.main.MainContract;
import com.finance.widget.indexrecyclerview.expandRecyclerviewadapter.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 动态对话框
 */
public class DynamicPopupWindow extends LeftToRightPopupWindow implements ICallback<DynamicsEntity> {

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
    private IDismiss dismiss;

    private DynamicAdapter mAdapter;
    private DynamicsEntity mEntity;

    public DynamicPopupWindow(Activity activity, MainContract.Presenter presenter, int width, int x, int y) {
        super(activity, width, LinearLayout.LayoutParams.MATCH_PARENT, x, y);
        mActivity = activity;
        this.mPresenter = presenter;
        setTouchable(true);
        setOutsideTouchable(true);   //设置外部点击关闭ppw窗口
        tvTitle.setText("交易动态");
        tvEmptyText.setText("暂无动态记录");
        if (presenter != null)
            presenter.getDynamicPopupWindow(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.popupwindow_order;
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
            StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
            rvOrder.addItemDecoration(headersDecor);
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
        super.dismiss();
        if (dismiss != null)
            dismiss.onDismiss();
    }

    public void setOnDismiss(IDismiss dismiss) {
        this.dismiss = dismiss;
    }

    @Override
    public void onCallback(int code, DynamicsEntity dynamicsEntity, String message) {
        if (!this.isShowing()) return;
        if (dynamicsEntity == null) {
            App.getInstance().showErrorMsg(message);
        }
        mEntity = dynamicsEntity;
        initData();
    }
}
