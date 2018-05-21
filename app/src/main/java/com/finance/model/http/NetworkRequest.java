package com.finance.model.http;

import com.finance.utils.NetWorkUtils;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.Connection;
import microsoft.aspnet.signalr.client.ConnectionState;

/**
 * 网络请求
 */
public class NetworkRequest {

    private ISign mISign;//请求参数处理类
    private BaseParams params;
    private Object tag;
    private int T;
    private String Token;
    private Connection connection;
    private MMessageReceivedHandler mHandler;

    public NetworkRequest(Connection connection) {
        this.connection = connection;
    }

    public NetworkRequest setISign(ISign ISign) {
        mISign = ISign;
        return this;
    }

    public NetworkRequest setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public NetworkRequest setParams(BaseParams params) {
        this.params = params;
        return this;
    }

    public NetworkRequest setT(int t) {
        T = t;
        return this;
    }

    public NetworkRequest setToken(String token) {
        Token = token;
        return this;
    }

    public NetworkRequest setHandler(MMessageReceivedHandler handler) {
        mHandler = handler;
        return this;
    }

    public void execute(BaseCallback2 callback2) {
        if (callback2 == null) return;
        if (!NetWorkUtils.isNetworkConnected()) {
            callback2.noNetworkConnected(params.getSourceCode());
            return;
        }
        if (callback2 instanceof JsonCallback2) {
            JsonCallback2 jsonCallback = (JsonCallback2) callback2;
            jsonCallback.setTag(tag);
            jsonCallback.onBefore();//开始请求网络
        }
        mHandler.addBaseCallback(params.getSourceCode(), callback2);//将回调添加到集合
        request();
    }

    public void execute(CallbackIssues callback2) {
        mHandler.setHistoryIssues(callback2);//设置回调
        request();
    }

    private void request() {
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
    }

    private void send() {
//        String json = mISign.getSign(params.getParams(), T, Token);
//        if (BuildConfig.DEBUG) {
//            Logger.d("提交参数：" + json);
//        }
//        connection.send(json);
    }


}
