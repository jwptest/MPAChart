package com.finance.model.https;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.finance.BuildConfig;
import com.finance.common.Constants;
import com.finance.model.ben.ResponseEntity;
import com.finance.utils.HandlerUtil;
import com.finance.utils.TimerUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Type;

/**
 * 网络回调
 */
public abstract class JsonCallback<T> implements BaseCallback3 {

    private Object tag;
    private Class<T> clazz;
    private Type type;
    private T t;
    private ResponseEntity mEntity;

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    @Override
    public void noNetworkConnected(int sourceCode) {
        failed(sourceCode, "没有网络", false);
    }

    @Override
    public void onMessageReceived(String data) {
        //运行在子线程
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onAfter();
            }
        });
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
        if (t instanceof ResponseEntity) {
            mEntity = (ResponseEntity) t;
            if (mEntity.getStatus() == Constants.DEFAULTCODE) return;
        }
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mEntity != null) {
                    if (mEntity.getStatus() != Constants.SUCCESSCODE) {
                        failed(mEntity.getSourceCode(), mEntity.getMessage(), false);
                        return;
                    }
                    Constants.SERVERCURRENTTIMER = TimerUtil.timerToLong(mEntity.getCurrDateTime());
                    //成功
                    successed(mEntity.getSourceCode(), mEntity.getMessage(), false, t);
                    return;
                }
                successed(0, "请求成功", false, t);
            }
        });
    }

    @Override
    public void error(int sourceCode, String msg) {
        failed(sourceCode, msg, false);
    }

    public void noNetworkConnected() {
        //没有网络
        failed(mEntity.getSourceCode(), "没有网络！", false);
    }

    //设置tag
    @Override
    public void setTag(Object tag) {
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
