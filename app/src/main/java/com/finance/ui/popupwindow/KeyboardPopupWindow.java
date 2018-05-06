package com.finance.ui.popupwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.finance.R;
import com.finance.utils.BtnClickUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.key;

/**
 * 键盘对话框
 */
public class KeyboardPopupWindow extends PopupWindow {

    private IKeyListener mKeyListener;

    public KeyboardPopupWindow(Activity activity, int width, int height) {
        super(width, height);
        View rootView = LayoutInflater.from(activity).inflate(R.layout.popupwindow_keyboard, null);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        setTouchable(true);
        setOutsideTouchable(true);   //设置外部点击关闭ppw窗口
    }

    @OnClick({R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5, R.id.tv6, R.id.tv7, R.id.tv8, R.id.tv9, R.id.tv11, R.id.tv0, R.id.tvn1})
    public void onClick(View view) {
        if (mKeyListener == null) return;
        key(view.getTag() + "");
    }

    private void key(String tag) {
        int key;
        try {
            key = Integer.valueOf(tag);
        } catch (Exception e) {
            return;
        }
        if (key >= 0 && key <= 9) {//数字键
            mKeyListener.onKey(key);
        } else if (key == 11) {//OK键
            mKeyListener.onOk();
        } else if (key == -1) {//删除
            mKeyListener.onDelete();
        }
    }

    public interface IKeyListener {
        void onKey(int key);//输入数字

        void onOk();//OK键

        void onDelete();//删除
    }

    public void setOnKeyListener(IKeyListener keyListener) {
        mKeyListener = keyListener;
    }
}