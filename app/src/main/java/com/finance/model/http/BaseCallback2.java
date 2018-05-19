package com.finance.model.http;

/**
 * 网络请求回调
 */
public abstract class BaseCallback2 {

    public abstract void noNetworkConnected(int sourceCode);//没有网络

    public abstract void onMessageReceived(String message);

    public static BaseCallback2 getBaseCallback(){
        return new BaseCallback2() {
            @Override
            public void noNetworkConnected(int sourceCode) {

            }

            @Override
            public void onMessageReceived(String message) {

            }
        };
    }

}
