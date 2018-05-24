package com.finance.model.https;

import android.util.Log;

import com.finance.BuildConfig;
import com.finance.common.Constants;
import com.finance.utils.HandlerUtil;
import com.finance.utils.NetWorkUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.Connection;
import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.StateChangedCallback;

/**
 * 执行请求
 */
public class HttpConnection {

    private volatile static HttpConnection sHttpConnection;
    private static ArrayList<RequestEntity> mArrayList;
    private boolean isConnectionIn = false;//是否是在连接中

    private HttpConnection() {
    }

    public static HttpConnection getInstance() {
        if (sHttpConnection == null) {
            synchronized (HttpConnection.class) {
                if (sHttpConnection == null) {
                    mArrayList = new ArrayList<>(2);
                    sHttpConnection = new HttpConnection();
                }
            }
        }
        return sHttpConnection;
    }

    private Connection connection;

    private void stopConnection() {
        Log.d("123", "request: 断开连接");
        if (connection != null) {
            try {
                connection.stop();
            } catch (Exception e) {
            }
            connection = null;//置空下次请求重新连接
        }
        isConnectionIn = false;
    }

    //启动连接
    private void startConnection() {
        isConnectionIn = true;
        connection = new Connection(Constants.HTTPURL);
        connection.received(new MessageReceivedHandler() {
            @Override
            public void onMessageReceived(JsonElement json) {
                if (json instanceof JsonPrimitive) {
                    parseIndexMark(json); //获取指数
                } else if (json instanceof JsonObject) {//普通请求或者推送
                    parseNoIndexMark(json);
                } else if (json instanceof JsonArray) {
                    parseHistoryIndex(json);//获取历史数据
                }
            }
        });
        //未连接
        connection.start().done(new Action<Void>() {
            @Override
            public void run(Void aVoid) throws Exception {
                //设置网络监听
                connection.stateChanged(new StateChangedCallback() {
                    @Override
                    public void stateChanged(ConnectionState connectionState, ConnectionState connectionState1) {
                        if (connection.getState() == ConnectionState.Connected) {
                            return;
                        }
                        Log.d("123", "stateChanged: 断开连接");
                        ApiCache.clearRequest();
                        stopConnection();
                    }
                });
                //将缓存的请求发送
                HandlerUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final int size = mArrayList == null || mArrayList.isEmpty() ? 0 : mArrayList.size();
                        if (size == 0) return;
                        RequestEntity request;
                        for (int i = 0; i < size; i++) {
                            request = mArrayList.get(i);
                            send(request.params, request.callback3);
                        }
                        mArrayList.clear();
                        isConnectionIn = false;
                    }
                });
            }
        }).onError(new ErrorCallback() {
            @Override
            public void onError(Throwable throwable) {
                Log.d("123", "onError: 断开连接");
                HandlerUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final int size = mArrayList == null || mArrayList.isEmpty() ? 0 : mArrayList.size();
                        if (size == 0) return;
                        RequestEntity request;
                        for (int i = 0; i < size; i++) {
                            request = mArrayList.get(i);
                            if (request.callback3 == null) continue;
                            request.callback3.error(request.params.getSourceCode(), "网络连接失败");
                        }
                        mArrayList.clear();
                        stopConnection();
                    }
                });
            }
        });
    }

    //指数解析
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
        RequestCallBack indexRequestCallBack = ApiCache.findCallBackById(String.valueOf(productId), false);
        if (indexRequestCallBack == null) return;
        indexRequestCallBack.getCallback().onMessageReceived(indexMark);
    }

    private void parseHistoryIndex(JsonElement jsonarray) {
        String historyHead_IndexMark = jsonarray.getAsJsonArray().get(0).getAsString();
        long productId = parseProductId(historyHead_IndexMark);
        RequestCallBack historyRequestCallBack = ApiCache.findCallBackById(String.valueOf(productId) + "-" + "0", true);
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
        RequestCallBack normalRequest = ApiCache.findCallBackById(key, true);
        if (normalRequest == null) return;
        normalRequest.getCallback().onMessageReceived(noIndexObject.toString());
    }

    private void backEndSendMessage(JsonObject noIndexObject) {
        if (!BuildConfig.DEBUG) return;
        String backEndMessage = "backEndMessage";
        long T = noIndexObject.get("T").getAsLong();
        switch ((int) T) {
            case 300:
//                String D = noIndexObject.get("D").getAsString();
//                IssuesEntity entity;
//                try {
//                    entity = new Gson().fromJson(D, IssuesEntity.class);
//                } catch (Exception e) {
//                    entity = null;
//                }
//                ArrayList<IssueEntity> issues = entity == null ? null : entity.getIssueInfo();
//                EventBus.post(new PushIssuesEvent(issues));
                Log.v(backEndMessage + "——期号推送", String.valueOf(noIndexObject));
                break;
            default:
                Log.v(backEndMessage + "——其他推送", String.valueOf(noIndexObject));
        }
    }

    public void request(RequestParams params, BaseCallback3 callback3) {
        if (!NetWorkUtils.isNetworkConnected()) {
            if (callback3 != null)
                callback3.noNetworkConnected(params.getSourceCode());//没有网络
            return;
        }
        if (connection == null) {
            Log.d("123", "request: 第一次请求网络");
            startConnection();//连接网络
            mArrayList.add(new RequestEntity(params, callback3));
        } else if (connection.getState() != ConnectionState.Connected) {
            Log.d("123", "request: 网络未连接" + isConnectionIn);
            if (!isConnectionIn) {//已经断开
                stopConnection();
                startConnection();
            }
            mArrayList.add(new RequestEntity(params, callback3));
        } else {
            Log.d("123", "request: 已经连接成功");
            //已经连接成功
            send(params, callback3);
        }
    }

    private void send(RequestParams params, BaseCallback3 callback3) {
        String json = params.sendMessage();
        if (BuildConfig.DEBUG) {
            Logger.d("提交参数：" + json);
        }
        try {
            connection.send(json);
        } catch (Exception e) {
            if (callback3 != null)
                callback3.error(params.getSourceCode(), "请求失败");
            return;
        }
        if (callback3 != null)
            ApiCache.addRequestCallBack(params, callback3);
    }

    private static class RequestEntity {
        private RequestParams params;
        private BaseCallback3 callback3;

        private RequestEntity(RequestParams params, BaseCallback3 callback3) {
            this.params = params;
            this.callback3 = callback3;
        }
    }

}
