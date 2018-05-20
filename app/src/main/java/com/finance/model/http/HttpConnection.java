package com.finance.model.http;

import com.finance.BuildConfig;
import com.finance.utils.NetWorkUtils;
import com.orhanobut.logger.Logger;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.Connection;
import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.SignalRFuture;
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
    private int T;
    private String Token;
    private BaseCallback receivedHandler;

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

    public HttpConnection setT(int t) {
        T = t;
        return this;
    }

    public HttpConnection setToken(String token) {
        Token = token;
        return this;
    }

    //断开链接
    public void stop() {
        if (receivedHandler != null) {
            receivedHandler.setDiscarded(true);//设置为废弃
            receivedHandler = null;
        }
        if (connection != null) {
            try {
                connection.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            connection = null;
        }
    }

    private Connection connection;

    public HttpConnection execute(BaseCallback receivedHandler) {
        if (!NetWorkUtils.isNetworkConnected()) {
            receivedHandler.noNetworkConnected();
            return this;
        }
        this.receivedHandler = receivedHandler;
        connection = new Connection(url);
        connection.received(receivedHandler);
        connection.stateChanged(mChangedCallback);
        if (receivedHandler instanceof JsonCallback) {
            JsonCallback jsonCallback = (JsonCallback) receivedHandler;
            jsonCallback.setTag(tag);
            jsonCallback.setConnection(this);
            jsonCallback.onBefore();//开始请求网络
        }

        if (connection.getState() != ConnectionState.Connected) {
            //未连接
            connection.start().done(new Action<Void>() {
                @Override
                public void run(Void aVoid) throws Exception {
                    send();
                }
            });
        } else {
            //已经连接成功
            send();
        }
        return this;
    }

    private void send() {
        String json = mISign.getSign(params.getParams(), T, Token);
        if (BuildConfig.DEBUG) {
            Logger.d("提交参数：" + json);
        }
        connection.send(json);
    }


}
