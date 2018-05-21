package com.finance.model.https;

/**
 * 请求回调
 */
public interface BaseCallback3 {

    void noNetworkConnected(int sourceCode);//没有网络

    void onMessageReceived(String data);

    void error(int sourceCode, String mgs);

    void setTag(Object object);

}
