package com.finance.model.http;

import microsoft.aspnet.signalr.client.MessageReceivedHandler;

/**
 */
public abstract class BaseCallback implements MessageReceivedHandler {

    protected boolean isDiscarded = false;//是否废弃该回调

    public abstract void noNetworkConnected();//没有网络

    public void setDiscarded(boolean discarded) {
        isDiscarded = discarded;
    }
}
