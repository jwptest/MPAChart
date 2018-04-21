package com.finance.event;

/**
 * 数据刷新事件
 */
public class DataRefreshEvent {

    private boolean isRefresh;

    public DataRefreshEvent(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isRefresh() {
        return isRefresh;
    }
}
