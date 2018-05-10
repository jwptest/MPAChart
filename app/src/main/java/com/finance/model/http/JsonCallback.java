package com.finance.model.http;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.finance.BuildConfig;
import com.finance.common.Constants;
import com.finance.model.ben.ResponseEntity;
import com.finance.utils.HandlerUtil;
import com.finance.utils.TimerUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Type;

/**
 * 网络回调
 */
public abstract class JsonCallback<T> extends BaseCallback {

    private Object tag;
    private Class<T> clazz;
    private Type type;
    private T t;

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    public JsonCallback(Type type) {
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
        if (BuildConfig.DEBUG) {
            Logger.d("返回数据：" + data);
        }
        try {
            if (clazz == String.class) t = (T) data;
            else if (clazz != null) t = new Gson().fromJson(data, clazz);
            else if (type != null) t = new Gson().fromJson(data, type);
            else t = null;
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
//                e.printStackTrace();
            }
            t = null;
            return;
        }
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (t instanceof ResponseEntity) {
                    ResponseEntity entity = (ResponseEntity) t;
                    if (entity.getStatus() == Constants.DEFAULTCODE) return;
                    if (entity.getStatus() != Constants.SUCCESSCODE) {
                        failed(entity.getSourceCode(), entity.getMessage(), false);
                        return;
                    }
                    Constants.SERVERCURRENTTIMER = TimerUtil.timerToLong(entity.getCurrDateTime());
                    //成功
                    successed(entity.getSourceCode(), entity.getMessage(), false, t);
                    return;
                }
                successed(0, "请求成功", false, t);
            }
        });
    }

    //设置tag
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
