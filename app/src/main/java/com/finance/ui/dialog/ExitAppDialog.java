package com.finance.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.finance.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 退出程序对话框
 */
public class ExitAppDialog extends Dialog {

    @BindView(R.id.tvEdit)
    TextView mTvEdit;
    @BindView(R.id.vLine)
    View mVLine;
    @BindView(R.id.tvCancel)
    TextView mTvCancel;
    @BindView(R.id.ivClose)
    ImageView mIvClose;

    public ExitAppDialog(@NonNull Context context) {
        super(context, R.style.noBackDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_exit_app);
        ButterKnife.bind(this);
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
