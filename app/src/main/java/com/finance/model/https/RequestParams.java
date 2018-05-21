package com.finance.model.https;

import com.finance.model.http.BaseParams;

/**
 * 请求的配置
 */
public class RequestParams {

    private long start = 0;//请求开始，获取本地时间
    private long timeOut = 10 * 1000;//超时
    private boolean isRate = false; //是否为指数
    private boolean isNoLoading = false; //加载进度条
    private boolean isExpire = false; //是否过期
    private BaseParams data; //真正传递给服务器的数据
//    private ISign mISign;//请求参数处理类

    public RequestParams(BaseParams data) {
        this.data = data;
        start = System.currentTimeMillis();
    }

    public long getStart() {
        return start;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public RequestParams setTimeOut(long timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public boolean isRate() {
        return isRate;
    }

    public RequestParams setRate(boolean rate) {
        isRate = rate;
        return this;
    }

    public boolean isNoLoading() {
        return isNoLoading;
    }

    public RequestParams setNoLoading(boolean noLoading) {
        isNoLoading = noLoading;
        return this;
    }

    public boolean isExpire() {
        return isExpire;
    }

    public RequestParams setExpire(boolean expire) {
        isExpire = expire;
        return this;
    }

    public String getMessageId() {
        return data.getMessageId();
    }

//    public RequestParams setISign(ISign ISign) {
//        mISign = ISign;
//        return this;
//    }

    public Object getTag() {
        return data.getTag();
    }

    public int getSourceCode() {
        return data.getSourceCode();
    }

    public String getD() {
        return data.getD();
    }

    public String sendMessage() {
        return data.sendMessage();
    }

}
