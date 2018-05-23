package com.finance.event;

/**
 * 刷新用户信息事件
 */
public class UpdateUserInfoEvent {

    private boolean isTip;//是否提示
    private boolean isTipMsg;
    private String msg = "余额已更新！";//提示内容


    public UpdateUserInfoEvent(boolean isTip) {
        this(isTip, "");
    }

    public UpdateUserInfoEvent(boolean isTip, String msg) {
        this(isTip, false, msg);
    }

    public UpdateUserInfoEvent(boolean isTip, boolean isTipMsg) {
        this(isTip, isTipMsg, "");
    }

    public UpdateUserInfoEvent(boolean isTip, boolean isTipMsg, String msg) {
        this.isTip = isTip;
        this.msg = msg;
        this.isTipMsg = isTipMsg;
    }

    public boolean isTip() {
        return isTip;
    }

    public String getMsg() {
        return (msg != null) ? msg : "";
    }

    public boolean isTipMsg() {
        return isTipMsg;
    }
}
