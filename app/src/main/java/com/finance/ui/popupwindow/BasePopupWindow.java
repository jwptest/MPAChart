package com.finance.ui.popupwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.finance.interfaces.IDismiss;

import butterknife.ButterKnife;

/**
 * PopupWindow基类
 */
public abstract class BasePopupWindow extends PopupWindow {

    protected LinearLayout rootView;
    private IDismiss dismiss;

    public BasePopupWindow(Context context, int width, int height) {
        this(context, width, height, 0, 0);
    }

    public BasePopupWindow(Context context, int width, int height, int x, int y) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(context, width, height, x, y);
    }

    private void setContentView(Context context, int width, int height, int x, int y) {
        View contentView = getLayoutView();
        if (contentView == null)
            contentView = LayoutInflater.from(context).inflate(getLayoutId(), null);
        rootView = new LinearLayout(context);
        rootView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.leftMargin = x;
        params.topMargin = y;
        rootView.addView(contentView, params);
        setContentView(rootView);
        if (isBindView()) {
            ButterKnife.bind(this, contentView);
        }
    }

    protected int getLayoutId() {
        return -1;
    }

    protected View getLayoutView() {
        return null;
    }

    protected abstract boolean isBindView();

    public void setDismiss(IDismiss dismiss) {
        this.dismiss = dismiss;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (dismiss != null)
            dismiss.onDismiss();
    }

    @Override
    public void setOutsideTouchable(boolean touchable) {
        super.setOutsideTouchable(touchable);
        if (!touchable) return;
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void showPopupWindow(View view) {
        showAtLocation(view, Gravity.START, 0, 0);
    }

}
