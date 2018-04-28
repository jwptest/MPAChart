package com.finance.model.http;

import com.finance.BuildConfig;
import com.orhanobut.logger.Logger;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.Connection;
import microsoft.aspnet.signalr.client.StateChangedCallback;

import static android.R.attr.data;

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

    public void execute(BaseCallback receivedHandler) {
        Connection connection = new Connection(url);
        connection.received(receivedHandler);
        connection.stateChanged(mChangedCallback);
        receivedHandler.setTag(tag);
        receivedHandler.onBefore();//开始请求网络
        connection.start().done(new Action<Void>() {
            @Override
            public void run(Void aVoid) throws Exception {
                String json = mISign.getSign(params.getParams(), T, Token);
                if (BuildConfig.DEBUG) {
                    Logger.d("提交参数：" + json);
                }
//                json = "{D\n" +
//                        ":\n" +
//                        "\"{\"sourcecode\":101,\"messageid\":\"19781581-5133-edb6-add2-43ba24157366\",\"device\":1,\"platformid\":\"2\",\"sign\":\"e89ef921e5d195dbd8740324b5d37a00\",\"t\":1101}\"\n" +
//                        "T\n" +
//                        ":\n" +
//                        "1101\n" +
//                        "Token\n" +
//                        ":\n" +
//                        "\"\"}";
                connection.send(json);
            }
        });
    }

}
