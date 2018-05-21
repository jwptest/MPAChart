package com.finance.model.https;


import com.finance.utils.HandlerUtil;

import java.util.ArrayList;

public class ApiCache {

    static private ArrayList<RequestCallBack> requestCallBacks = new ArrayList<RequestCallBack>(); //已经发送请求池
    static private boolean isRunning = false; //加锁
    private static Runnable mRunnable;

    //    清理掉 延迟请求缓存池，清理掉，已发送请求缓存池；
    public static void clearRequest() {
        requestCallBacks.clear();
    }

    // 增加请求缓存池
    public static void addRequestCallBack(RequestParams options, BaseCallback3 callback3) {
        String key = getKey(options);
        RequestCallBack requestCallBack = new RequestCallBack(key, options);
        callback3.setTag(options.getTag());
        requestCallBack.setCallback(callback3);
        isRunning = true;
        RequestCallBack tempCallBack = findCallBackById(requestCallBacks, key, true);
        if (tempCallBack != null) tempCallBack.done();
        requestCallBacks.add(requestCallBack);
        isRunning = false;
        if (mRunnable == null)
            managerRequest();
    }

    //        在请求缓存池中，寻找Key相等的对象
    private static RequestCallBack findCallBackById(ArrayList<RequestCallBack> requestCallBacks, String key, boolean isRemove) {
        RequestCallBack backCallBack = null;
        for (RequestCallBack requestCallback : requestCallBacks) {
            if (requestCallback.getId().equals(key)) {
                backCallBack = requestCallback;
                break;
            }
        }
        if (isRemove && backCallBack != null) {
            requestCallBacks.remove(backCallBack);
        }
        return backCallBack;
    }

    public static void removeCallBacks(BaseCallback3 callback) {
        RequestCallBack requestCallback0 = null;
        for (RequestCallBack requestCallback : requestCallBacks) {
            if (requestCallback.getCallback() == callback) {
                requestCallback0 = requestCallback;
                break;
            }
        }
        if (requestCallback0 != null) {
            requestCallBacks.remove(requestCallback0);
        }
    }


    //        在请求缓存池中，寻找Key相等的对象
    public static RequestCallBack findCallBackById(String key, boolean isRemove) {
        return findCallBackById(requestCallBacks, key, isRemove);
    }

    //返回发送池中的KEY
    public static String getKey(RequestParams options) {
        if (!options.isRate()) return options.getMessageId();
        String[] tempArr = options.getD().split(":");
        if (tempArr.length > 1) {
            return tempArr[0] + "-" + "0";
        } else {
            return tempArr[0];
        }
    }

    //    管理发送池
    public static void managerRequestCallBack() {
        // TODO: 2018/5/20每秒执行
        if (isRunning) {
            mRunnable = null;
            return;
        }
        int size = requestCallBacks == null ? 0 : requestCallBacks.size();
        if (size == 0) {
            mRunnable = null;
            return;
        }
        long timer = System.currentTimeMillis();
        ArrayList<RequestCallBack> arrayList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            RequestCallBack requestCallBack = requestCallBacks.get(i);
            if (requestCallBack.isRate()) return;
            if (timer - requestCallBack.getStartTimer() < requestCallBack.getTimeOut()) continue;
            requestCallBack.error("超时"); //看你怎么处理
            arrayList.add(requestCallBack);
        }
        for (RequestCallBack callBack : arrayList) {
            requestCallBacks.remove(callBack);
        }
//        for (RequestCallBack requestCallBack : requestCallBacks) {
//            requestCallBack.addCountTime(1000);
//            if (requestCallBack.getCountTime() < requestCallBack.getCountTime() || requestCallBack.isRate())
//                continue;
////            移除掉超时的，非指数的，requestCallback,
////            如果超时中，没有成功的，调用失败方法
//            if (!requestCallBack.isSuccess()) {
//                requestCallBack.error("超时"); //看你怎么处理
//            }
//            requestCallBacks.remove(requestCallBack);
//        }
        if (requestCallBacks == null || !requestCallBacks.isEmpty()) {
            mRunnable = null;
        } else {
            managerRequest();
        }
    }

    //10秒循环一次检查是否有超时请求
    private static void managerRequest() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                managerRequestCallBack();
            }
        };
        HandlerUtil.runOnUiThreadDelay(mRunnable, 10 * 1000);
    }

}
