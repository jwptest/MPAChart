package com.finance.ui.popupwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.finance.R;
import com.finance.widget.animation.BaseAnimatorSet;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 键盘对话框
 */
public class KeyboardPopupWindow extends BasePopupWindow {

    private IKeyListener mKeyListener;

    public KeyboardPopupWindow(Activity activity, int width, int height, int x, int y) {
        super(activity, width, height, x, y);
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

    @Override
    protected boolean isBindView() {
        return false;
    }

    @Override
    protected BaseAnimatorSet getShowAs() {
        return null;
    }

    @Override
    protected BaseAnimatorSet getDismissAs() {
        return null;
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
