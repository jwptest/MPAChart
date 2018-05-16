package com.finance;

import android.app.Application;
import android.os.Looper;

import com.finance.utils.HandlerUtil;
import com.finance.widget.ToastView;
import com.tencent.bugly.crashreport.CrashReport;

/**
 *
 */
public class App extends Application {

    private static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        //初始化bugLy,为了保证运营数据的准确性，建议不要在异步线程初始化Bugly
        CrashReport.initCrashReport(sApp.getApplicationContext(), "455847388a", BuildConfig.DEBUG);//false为正式模式，true为debug模式
//        //启动bugTag
//        HandlerUtil.runOnUiThreadDelay(new Runnable() {
//            @Override
//            public void run() {
//                //初始化bugLy,为了保证运营数据的准确性，建议不要在异步线程初始化Bugly
//                CrashReport.initCrashReport(sApp.getApplicationContext(), "455847388a", BuildConfig.DEBUG);//false为正式模式，true为debug模式
////                CrashReport.testJavaCrash();
//            }
//        }, 500);
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


}
