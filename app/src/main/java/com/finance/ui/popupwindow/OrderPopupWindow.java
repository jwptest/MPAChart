package com.finance.ui.popupwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.finance.R;
import com.finance.ui.adapters.OrderAdapter;

/**
 * 订单PopupWindow
 */
public class OrderPopupWindow extends PopupWindow {

    private Activity mActivity;

    private OrderAdapter mAdapter;

    public OrderPopupWindow(Activity activity) {
        mActivity = activity;
        View rootView = LayoutInflater.from(activity).inflate(R.layout.popupwindow_order, null);
        setContentView(rootView);
    }

}
