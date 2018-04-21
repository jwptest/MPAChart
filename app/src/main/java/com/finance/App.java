package com.finance;

import android.app.Application;
import android.content.Context;
import android.os.Looper;

import com.finance.utils.HandlerUtil;
import com.finance.widget.Toastor;

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
            new Toastor().showToast(msg);
            return;
        }
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Toastor().showToast(msg);
            }
        });
    }


}
