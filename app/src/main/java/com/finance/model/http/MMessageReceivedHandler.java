package com.finance.model.http;

import android.util.Log;

import com.finance.BuildConfig;
import com.finance.common.Constants;
import com.finance.model.ben.ResponseEntity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import microsoft.aspnet.signalr.client.MessageReceivedHandler;

/**
 * 网络请求回调
 * 普通请求返回对象 {@link com.finance.model.ben.ResponseEntity}
 * 历史期号返回[字符串数组]
 * 时时期号返回指数字符串
 */
public class MMessageReceivedHandler implements MessageReceivedHandler {

    protected CallbackIssues historyIssues;//历史期号回调
//    protected BaseCallback2 alwaysIssues;//期号
    protected HashMap<Integer, BaseCallback2> callBacks;//回调接口
    protected Gson gson;

    public MMessageReceivedHandler() {
        callBacks = new HashMap<Integer, BaseCallback2>(3);
        gson = new Gson();
    }

    @Override
    public synchronized void onMessageReceived(JsonElement jsonElement) {
        String data = jsonElement.toString();
        ResponseEntity entity = null;
        if (BuildConfig.DEBUG) {
            Log.d("123", "返回参数: " + data);
        }
        try {
            entity = gson.fromJson(data, ResponseEntity.class);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
//                e.printStackTrace();
            }
        }
        if (entity == null && historyIssues != null) {
            ArrayList<String> strs = null;
            try {
                strs = gson.fromJson(data, new TypeToken<ArrayList<String>>() {
                }.getType());
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
//                e.printStackTrace();
                }
            }
            if (strs != null) {
                historyIssues.onMessageReceived(strs);
                return;
            }
        }
        if (entity == null) {//解析失败
            return;
        }
        if (entity.getStatus() == Constants.DEFAULTCODE) return;
        BaseCallback2 callback = getBaseCallback(entity.getSourceCode());
        if (callback == null) return;
        callback.onMessageReceived(data);
    }

    public void addBaseCallback(int sourceCode, BaseCallback2 c) {
        callBacks.put(sourceCode, c);
    }

    public void setHistoryIssues(CallbackIssues historyIssues) {
        this.historyIssues = historyIssues;
    }

    private BaseCallback2 getBaseCallback(int sourceCode) {
        BaseCallback2 c = callBacks.get(sourceCode);
        callBacks.remove(sourceCode);
        return c;
    }


}
