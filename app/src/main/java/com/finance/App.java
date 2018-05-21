package com.finance;

import android.app.Application;
import android.os.Looper;

import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsOptions;
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
        //初始化bugLy,为了保证运营数据的准确性，建议不要在异步线程初始化Bugly
//        CrashReport.initCrashReport(sApp.getApplicationContext(), "455847388a", BuildConfig.DEBUG);//false为正式模式，true为debug模式
//        //启动bugTag
//        HandlerUtil.runOnUiThreadDelay(new Runnable() {
//            @Override
//            public void run() {
//                //初始化bugLy,为了保证运营数据的准确性，建议不要在异步线程初始化Bugly
//                CrashReport.initCrashReport(sApp.getApplicationContext(), "455847388a", BuildConfig.DEBUG);//false为正式模式，true为debug模式
////                CrashReport.testJavaCrash();
//            }
//        }, 500);

        BugtagsOptions options = new BugtagsOptions.Builder().
                trackingLocation(false).//是否获取位置，默认 true
                trackingCrashLog(true).//是否收集crash，默认 true
                trackingConsoleLog(false).//是否收集console log，默认 true
                trackingUserSteps(false).//是否收集用户操作步骤，默认 true
                trackingNetworkURLFilter("(.*)").//自定义网络请求跟踪的 url 规则，默认 null
                build();
        Bugtags.start(BuildConfig.DEBUG ? "8fa3ec82825f6cea1cc7dac777f9da19" : "3431fd00d8f4cb0d477bac0e866b2c8c", this, Bugtags.BTGInvocationEventBubble, options);
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
