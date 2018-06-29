package com.finance;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.os.Looper;

import com.finance.utils.HandlerUtil;
import com.finance.widget.ToastView;

/**
 *
 */
public class App extends Application {

    private static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }

    public static App getInstance() {
        return sApp;
    }

    //toast提示框
    public void showErrorMsg(final String msg) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            ToastView.showToast(msg);
            return;
        }
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastView.showToast(msg);
            }
        });
    }

    /**
     * 判断activityName是不是最顶层Activity
     */
    public boolean isTopActivity(String activityName) {
        boolean isTop = false;
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (am != null && am.getRunningTasks(1) != null && am.getRunningTasks(1).size() > 0) {
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            if (cn != null && cn.getClassName().contains(activityName)) {
                isTop = true;
            }
        }
        return isTop;
    }

}
