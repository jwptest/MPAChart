package com.finance.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.finance.R;
import com.finance.event.EventBus;
import com.finance.event.OpenPrizeDialogEvent;
import com.finance.event.ToastCloseEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * 开奖指数对话框
 */
public class ToastDialog extends Dialog {

    private Context mContext;
    private String tip;

    public ToastDialog(@NonNull Context context, String tip) {
        super(context, R.style.noBackDialog);
        mContext = context;
        this.tip = tip;
        EventBus.register(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tvMessage = (TextView) LayoutInflater.from(mContext).inflate(R.layout.toast_view, null);
        setContentView(tvMessage);
        tvMessage.setText(tip);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        EventBus.unregister(this);
    }

    @Subscribe
    public void onEvent(ToastCloseEvent event) {
        dismiss();
    }

}
