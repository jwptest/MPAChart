package com.finance.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.finance.R;
import com.finance.event.EventBus;
import com.finance.event.UpdateUserInfoEvent;
import com.finance.ui.main.MainContract;
import com.finance.ui.main.MainPresenter;
import com.finance.utils.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 刷新用户信息对话框
 */
public class UpdateUserInfoDialog extends Dialog {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvEdit)
    TextView tvEdit;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.rlContent)
    RelativeLayout rlContent;
    @BindView(R.id.ivTitle)
    ImageView ivTitle;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.llRootView)
    RelativeLayout llRootView;
    private Activity mActivity;
    private MainContract.Presenter presenter;

    public UpdateUserInfoDialog(@NonNull Activity activity, MainContract.Presenter presenter) {
        super(activity, R.style.noBackDialog);
        mActivity = activity;
        this.presenter = presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_exit_app);
        ButterKnife.bind(this);
        ViewUtil.setBackground(mActivity, rlContent, R.drawable.dialog_ts_bg);

        tvTitle.setText("领取体验金");
        tvEdit.setText("领取");
    }

    @OnClick({R.id.tvEdit, R.id.tvCancel, R.id.ivClose})
    public void onClick(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.tvEdit:
                presenter.receiveExperienceMoney();//获取体验金
                break;
            case R.id.tvCancel:
                break;
            case R.id.ivClose:
                break;
        }
    }


}
