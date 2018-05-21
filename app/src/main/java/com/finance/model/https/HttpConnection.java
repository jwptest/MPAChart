package com.finance.model.https;

import android.util.Log;

import com.finance.BuildConfig;
import com.finance.common.Constants;
import com.finance.utils.NetWorkUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.orhanobut.logger.Logger;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.Connection;
import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.StateChangedCallback;

/**
 * 执行请求
 */
public class HttpConnection {

    private volatile static HttpConnection sHttpConnection;

    public static HttpConnection getInstance() {
        if (sHttpConnection == null) {
            synchronized (HttpConnection.class) {
                if (sHttpConnection == null) {
                    sHttpConnection = new HttpConnection();
                }
            }
        }
        return sHttpConnection;
    }

    private Connection connection;

    private HttpConnection() {
        connection = new Connection(Constants.HTTPURL);
        connection.received(new MessageReceivedHandler() {
            @Override
            public void onMessageReceived(JsonElement json) {
                if (json instanceof JsonPrimitive) {
                    parseIndexMark(json); //获取指数
                } else if (json instanceof JsonArray) {
                    parseHistoryIndex(json);//获取历史数据
                } else if (json instanceof JsonObject) {
                    parseNoIndexMark(json);
                }
            }
        });
    }

    private long parseProductId(String indexMark) {
        if (indexMark == null) return 0;
        else {
            String _id = indexMark.substring(0, 2);
            return Integer.parseInt(_id, 16);
        }
    }

    private void parseIndexMark(JsonElement json) {
        String indexMark = json.getAsString();
        long productId = parseProductId(indexMark);
        RequestCallBack indexRequestCallBack = ApiCache.findCallBackById(String.valueOf(productId));
        if (indexRequestCallBack == null) return;
        indexRequestCallBack.getCallback().onMessageReceived(indexMark);
    }

    private void parseHistoryIndex(JsonElement jsonarray) {
        String historyHead_IndexMark = jsonarray.getAsJsonArray().get(0).getAsString();
        long productId = parseProductId(historyHead_IndexMark);
        RequestCallBack historyRequestCallBack = ApiCache.findCallBackById(String.valueOf(productId) + "-" + "0");
        if (historyRequestCallBack == null) return;
        historyRequestCallBack.getCallback().onMessageReceived(jsonarray.toString());
    }

    private void parseNoIndexMark(JsonElement json) {
        JsonObject noIndexObject = json.getAsJsonObject();
        if (noIndexObject.has("SourceCode")) {
            handleNormalRequest(noIndexObject);
        } else {
            backEndSendMessage(noIndexObject);
        }
    }

    private void handleNormalRequest(JsonObject noIndexObject) {
        String key = noIndexObject.get("MessageId").getAsString().trim();
        RequestCallBack normalRequest = ApiCache.findCallBackById(key);
        if (normalRequest == null) return;
        normalRequest.getCallback().onMessageReceived(noIndexObject.toString());
    }

    private void backEndSendMessage(JsonObject noIndexObject) {
        String backEndMessage = "backEndMessage";
        long T = noIndexObject.get("T").getAsLong();
        switch ((int) T) {
            case 300:
                Log.v(backEndMessage + "——期号推送", String.valueOf(noIndexObject));
                break;
            default:
                Log.v(backEndMessage + "——其他推送", String.valueOf(noIndexObject));
        }
    }

    public void request(final RequestParams params, final BaseCallback3 callback3) {
        if (!NetWorkUtils.isNetworkConnected()) {
            callback3.noNetworkConnected(params.getSourceCode());//没有网络
            return;
        }
        if (connection.getState() != ConnectionState.Connected) {
            //未连接
            connection.start().done(new Action<Void>() {
                @Override
                public void run(Void aVoid) throws Exception {
                    send(params, callback3);
                    //设置网络监听
                    connection.stateChanged(new StateChangedCallback() {
                        @Override
                        public void stateChanged(ConnectionState connectionState, ConnectionState connectionState1) {
                            if (connection.getState() == ConnectionState.Connected) return;
                            ApiCache.clearRequest();
                        }
                    });
                }
            });
        } else {
            //已经连接成功
            send(params, callback3);
        }
    }

    private void send(RequestParams params, BaseCallback3 callback3) {
        String json = params.sendMessage();
        if (BuildConfig.DEBUG) {
            Logger.d("提交参数：" + json);
        }
        connection.send(json);
        ApiCache.addRequestCallBack(params, callback3);
    }

}
