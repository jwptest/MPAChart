package com.finance.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.finance.R;
import com.finance.utils.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 退出程序对话框
 */
public class ExitAppDialog extends Dialog {

    @BindView(R.id.rlContent)
    View rlContent;
    @BindView(R.id.tvEdit)
    TextView mTvEdit;
    @BindView(R.id.vLine)
    View mVLine;
    @BindView(R.id.tvCancel)
    TextView mTvCancel;
    @BindView(R.id.ivClose)
    ImageView mIvClose;

    private Activity mActivity;

    public ExitAppDialog(@NonNull Activity activity) {
        super(activity, R.style.noBackDialog);
        mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_exit_app);
        ButterKnife.bind(this);
        ViewUtil.setBackground(mActivity, rlContent, R.drawable.dialog_ts_bg);
    }

    @OnClick({R.id.tvEdit, R.id.tvCancel, R.id.ivClose})
    public void onClick(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.tvEdit:
                System.exit(0);
                break;
            case R.id.tvCancel:
                break;
            case R.id.ivClose:
                break;
        }
    }
}
