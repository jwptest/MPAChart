package com.finance.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.finance.App;
import com.finance.model.ben.AccountEntity;
import com.finance.model.ben.UserInfoEntity;
import com.finance.utils.NumberUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * 用户操作处理
 */
public class UserShell {

    private static class UserShellInstance {
        private static final UserShell INSTANCE = new UserShell();
    }

    public static UserShell getInstance() {
        return UserShellInstance.INSTANCE;
    }

    private SharedPreferences mPref;
    //用户权限字段
    private String userToken;
    //用户信息
    private UserInfoEntity userInfo;

    private UserShell() {
        mPref = App.getInstance().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        String userInfoJson = getUserInfoJson();
        String userToken = getUserToken();
        UserInfoEntity userInfo;
        try {
            userInfo = new Gson().fromJson(userInfoJson, UserInfoEntity.class);
        } catch (Exception e) {
            emptyUserInfo();
            return;
        }
        this.userInfo = userInfo;
        this.userToken = userToken;
    }

    /**
     * 设置用户信息和用户key
     */
    boolean setUserAndToken(UserInfoEntity userInfo, String userToken) {
        if (userInfo == null || TextUtils.isEmpty(userToken)) {
            return false;
        }
        //保存用户信息
        String userInfoStr;
        try {
            userInfoStr = new Gson().toJson(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (!TextUtils.isEmpty(userInfoStr)) {
            //将用户信息保存到本地
            setUserInfoJson(userInfoStr);
        }
        setUserToken(userToken);
        //保存用户信息
        this.userInfo = userInfo;
        return true;
    }

    /**********************SharedPreferences操作*************************/
    /**
     * 清空内存中的用户信息
     */
    private void emptyUserInfo() {
        this.userToken = "";
        userInfo = new UserInfoEntity();
    }

    private String getUserInfoJson() {
        return mPref.getString("userInfoJson", "");
    }

    private void setUserInfoJson(String userInfoJson) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("userInfoJson", userInfoJson);
        editor.apply();
    }

    private void setUserToken(String token) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("UserToken", token);
        editor.apply();
        userToken = token;
    }

    /**********************对外提供的SharedPreferences操作*************************/

    /**
     * 清空用户信息
     */
    public void clearUserInfo() {
        SharedPreferences.Editor editor = mPref.edit();
        editor.clear();
        editor.apply();
        userToken = "";
    }

    public String getUserToken() {
        if (TextUtils.isEmpty(userToken)) return mPref.getString("UserToken", "");
        else return userToken;
    }

    public UserInfoEntity getUserInfo() {
        return userInfo == null ? new UserInfoEntity() : userInfo;
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(userToken);
    }

    public String getUserMoneyStr() {
        return NumberUtil.digitDouble(getUserMoney()) + "";
    }

    public double getUserMoney() {
        if (!isLogin()) return 0;
        ArrayList<AccountEntity> arrayList = userInfo.getAccounts();
        if (arrayList == null || arrayList.isEmpty()) return 0;
        double money = 0;
        for (AccountEntity entity : arrayList) {
            money += entity.getMoney();
        }
        return money;
    }

}