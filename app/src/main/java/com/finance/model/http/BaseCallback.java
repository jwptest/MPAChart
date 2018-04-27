package com.finance.model.http;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.finance.common.Constants;
import com.finance.model.ben.ResponseEntity;
import com.finance.utils.HandlerUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

import microsoft.aspnet.signalr.client.MessageReceivedHandler;

/**
 * 网络回调
 */
public abstract class BaseCallback<T extends ResponseEntity> implements MessageReceivedHandler {

    private Object tag;
    private Class<T> clazz;
    private Type type;
    private T t;

    public BaseCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    public BaseCallback(Type type) {
        this.type = type;
    }
    //{"Token":"6352945298af460fa9a595e3b57eeaa4#xhuMZ3LOg9q59IB5kk1bnv8D1s0sINBQCxoQpL0CdiVDYwxTRX0HoJjnM0ljCvlmxUv/ZIGKXnPX6S6ojepkfg==",
    // "ReceiveNotify":true,"PayPass":false,"MessageId":"3fc6bf1a-a7d1-4136-9ca5-5b304799b03c",
    // "SourceCode":102,"Status":0,"Message":null,"Url":null,"Sign":"d190d31b8462af8deec7c307e11be198",
    // "RunTime":"0:00:00.0190053","CurrDateTime":"2018-04-27T16:56:47.0206174+08:00","CNYRate":6.6155}

    @Override
    public void onMessageReceived(JsonElement jsonElement) {
        //运行在子线程
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onAfter();
            }
        });
        String data = jsonElement.toString();
//        if (clazz == String.class) t = (T) data;
        if (clazz != null) t = new Gson().fromJson(data, clazz);
        else if (type != null) new Gson().fromJson(data, type);
        else t = null;
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (t == null) {
                    failed(0, "数据解析错误", false);
                    return;
                }
                if (t.getStatus() != Constants.SUCCESSCODE) {
                    failed(t.getSourceCode(), t.getMessage(), false);
                    return;
                }
                //成功
                successed(t.getSourceCode(), t.getMessage(), false, t);
            }
        });
    }

    void setTag(Object tag) {
        this.tag = tag;
    }

    //开始网络请求
    public void onBefore() {

    }

    //请求结束
    public void onAfter() {

    }

    private void successed(int code, String msg, boolean isFromCache, T result) {
        if (tag == null) {
            onSuccessed(code, msg, isFromCache, result);
            return;
        }
        Activity activity = null;
        if (tag instanceof Activity)
            activity = (Activity) tag;
        else if (tag instanceof Fragment)
            activity = ((Fragment) tag).getActivity();
        else if (tag instanceof android.app.Fragment)
            activity = ((android.app.Fragment) tag).getActivity();
        if (activity != null && activity.isFinishing()) {
            return;
        }
        onSuccessed(code, msg, isFromCache, result);
    }

    private void failed(int code, String msg, boolean isFromCache) {
        if (tag == null) {
            onFailed(code, msg, isFromCache);
            return;
        }
        Activity activity = null;
        if (tag instanceof Activity)
            activity = (Activity) tag;
        else if (tag instanceof Fragment)
            activity = ((Fragment) tag).getActivity();
        else if (tag instanceof android.app.Fragment)
            activity = ((android.app.Fragment) tag).getActivity();
        if (activity != null && activity.isFinishing()) {
            return;
        }
        onFailed(code, msg, isFromCache);
    }

    public abstract void onSuccessed(int code, String msg, boolean isFromCache, T result);

    public abstract void onFailed(int code, String msg, boolean isFromCache);

}
