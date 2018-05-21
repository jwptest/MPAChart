package com.finance.model.http;

/**
 * 时时期号回调接口
 */
public abstract class CallbackAlwaysIssue {

    void onMessageReceived(String indexMark) {
//        HandlerUtil.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                onMessages(issues);
//            }
//        });
        onMessages(indexMark);
    }

    protected abstract void onMessages(String indexMark);

}
