package com.finance.model.https;

public class RequestCallBack {
    private String mId;
    private long timeOut;
    private long countTime = 0;
    private boolean isNoLoading;
    private boolean isSuccess = false;
    private boolean isRate;
    private RequestParams mOptions;
    private BaseCallback3 mCallback;

    public RequestCallBack(String key, RequestParams options) {
        this.mId = key;
        this.mOptions = options;
        this.timeOut = options.getTimeOut();
        this.isNoLoading = options.isNoLoading();
        this.isRate = options.isRate();
    }

    public void done() {
//        this.countTime = this.timeOut;
//        this.isSuccess = true;
    }

    public void error(String msg) {
        mCallback.error(mOptions.getSourceCode(), msg);
    }

    public String getId() {
        return mId;
    }

    public long getCountTime() {
        return countTime;
    }

    public void addCountTime(long countTime) {
        this.countTime += countTime;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public boolean isRate() {
        return isRate;
    }

    public void setRate(boolean rate) {
        isRate = rate;
    }

    public BaseCallback3 getCallback() {
        return mCallback;
    }

    public void setCallback(BaseCallback3 callback) {
        mCallback = callback;
    }

    public long getStartTimer() {
        return mOptions.getStart();
    }

    public long getTimeOut() {
        return timeOut;
    }

}
