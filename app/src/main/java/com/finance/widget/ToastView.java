package com.finance.widget;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.finance.App;
import com.finance.R;

/**
 * 自定义view的toast
 */
public class ToastView {

    public static void showToast(String msg) {
        Toast toast = new Toast(App.getInstance());
        //设置Toast显示位置，居中，向 X、Y轴偏移量均为0
        toast.setGravity(Gravity.CENTER, 0, 0);
        //获取自定义视图
        TextView tvMessage = (TextView) LayoutInflater.from(App.getInstance()).inflate(R.layout.toast_view, null);
        //设置文本
        tvMessage.setText(msg);
        //设置视图
        toast.setView(tvMessage);
        //设置显示时长
        toast.setDuration(Toast.LENGTH_SHORT);
        //显示
        toast.show();
    }

}
