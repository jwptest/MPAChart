package com.finance.model.http;

import java.util.ArrayList;

/**
 * 获取历史期号回调
 */
public abstract class CallbackIssues {

    void onMessageReceived(ArrayList<String> issues) {
//        HandlerUtil.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                onMessages(issues);
//            }
//        });
        onMessages(issues);
    }

    protected abstract void onMessages(ArrayList<String> issues);

}
