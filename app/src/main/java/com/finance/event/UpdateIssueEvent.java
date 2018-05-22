package com.finance.event;

/**
 * 切换产品刷新期号事件
 */
public class UpdateIssueEvent {

    boolean isUpdate = false;

    public UpdateIssueEvent(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public boolean isUpdate() {
        return isUpdate;
    }
}
