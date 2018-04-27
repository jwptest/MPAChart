package com.finance.model.http;

import java.util.HashMap;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.Connection;
import microsoft.aspnet.signalr.client.StateChangedCallback;

/**
 * 网络请求
 */
public class HttpConnection {

    private String url;
    private StateChangedCallback mChangedCallback;
    private ISign mISign;//请求参数处理类
    private BaseParams params;
    private Object tag;

    public HttpConnection(String url) {
        this.url = url;
    }

    public HttpConnection setISign(ISign ISign) {
        mISign = ISign;
        return this;
    }

    public HttpConnection setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public HttpConnection setChangedCallback(StateChangedCallback changedCallback) {
        mChangedCallback = changedCallback;
        return this;
    }

    public HttpConnection setParams(BaseParams params) {
        this.params = params;
        return this;
    }

    public void execute(BaseCallback receivedHandler) {
        Connection connection = new Connection(url);
        connection.received(receivedHandler);
        connection.stateChanged(mChangedCallback);
        receivedHandler.setTag(tag);
        receivedHandler.onBefore();//开始请求网络
        connection.start().done(new Action<Void>() {
            @Override
            public void run(Void aVoid) throws Exception {
                connection.send(mISign.getSign(params.getParams()));
            }
        });
    }

}
