package com.finance.event;

/**
 * 网络状态变化事件
 */
public class NetWorkStateEvent {

    private boolean isNetWork = false;//是否有网络
    private int state;//网络类型

    public NetWorkStateEvent(boolean isNetWork, int state) {
        this.isNetWork = isNetWork;
        this.state = state;
    }

    public NetWorkStateEvent(boolean isNetWork) {
        this.isNetWork = isNetWork;
    }

    public boolean isNetWork() {
        return isNetWork;
    }

    public int getState() {
        return state;
    }

}
