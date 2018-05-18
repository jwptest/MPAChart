package com.finance.model.http;

import com.google.gson.JsonElement;

import microsoft.aspnet.signalr.client.MessageReceivedHandler;

/**
 */
public abstract class BaseCallback implements MessageReceivedHandler {

    protected boolean isDiscarded = false;//是否废弃该回调

    public abstract void noNetworkConnected();//没有网络

    public void setDiscarded(boolean discarded) {
        isDiscarded = discarded;
    }

    //获取一个空处理的回调
    public static BaseCallback getBaseCallback() {
        return new BaseCallback() {
            @Override
            public void noNetworkConnected() {

            }

            @Override
            public void onMessageReceived(JsonElement jsonElement) {

            }
        };
    }

}
