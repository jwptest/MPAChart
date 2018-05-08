package com.finance.event;

/**
 * 用户登录事件
 */
public class UserLoginEvent {

    private boolean isLogin = false;

    public UserLoginEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
