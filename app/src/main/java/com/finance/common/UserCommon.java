package com.finance.common;

import android.content.Context;
import android.text.TextUtils;

import com.finance.interfaces.ICallback;
import com.finance.model.ben.TokenEntity;
import com.finance.model.ben.UserInfoEntity;
import com.finance.model.http.BaseParams;
import com.finance.model.https.JsonCallback;
import com.finance.model.imps.NetworkRequest;

/**
 * 用户操作
 */
public class UserCommon {

    public static void getUserInfo() {
        getUserInfo(null);
    }

    public static void getUserInfo(Context context) {
        getUserInfo(context, UserShell.getInstance().getUserToken());
    }

    public static void getUserInfo(Context context, String token) {
        getUserInfo(context, token, null);
    }

    public static void getUserInfo(Context context, final ICallback<UserInfoEntity> iCallback) {
        getUserInfo(context, UserShell.getInstance().getUserToken(), iCallback);
    }

    /**
     * 获取用户信息
     */
    public static void getUserInfo(final Context context, String token, final ICallback<UserInfoEntity> iCallback) {
        if (TextUtils.isEmpty(token)) {
            //调用注册
            register(context, new ICallback<TokenEntity>() {
                @Override
                public void onCallback(int code, TokenEntity tokenEntity, String message) {
                    String tokenStr = tokenEntity == null ? "" : tokenEntity.getToken();
                    if (TextUtils.isEmpty(tokenStr)) {
                        if (iCallback == null) {
                            return;
                        }
                        iCallback.onCallback(code, null, message);
                        return;
                    }
                    getUserInfo(context, tokenStr, iCallback);
                }
            });
            return;
        }
        getUserInfo1(context, token, iCallback);
    }

    /**
     * 获取用户信息
     */
    private static void getUserInfo1(Context context, final String token, final ICallback<UserInfoEntity> iCallback) {
        BaseParams baseParams = new BaseParams(105);
        baseParams.setToken(token);
        baseParams.setTag(context);
        NetworkRequest.getInstance()
                .getHttpConnection()
                .request(NetworkRequest.getRequestParamSignImp(baseParams), new JsonCallback<UserInfoEntity>(UserInfoEntity.class) {
                    @Override
                    public void onSuccessed(int code, String msg, boolean isFromCache, UserInfoEntity result) {
                        UserShell shell = UserShell.getInstance();
                        boolean islogin = shell.setUserAndToken(result, token);
                        if (iCallback == null)
                            return;
                        if (islogin) {
                            iCallback.onCallback(code, result, msg);
                            return;
                        }
                        iCallback.onCallback(code, null, "登录失败");
                    }

                    @Override
                    public void onFailed(int code, String msg, boolean isFromCache) {
                        if (iCallback != null)
                            iCallback.onCallback(code, null, msg);
                    }
                });
//                .setTag(context)
//                .setT(200)
//                .setToken(token)
//                .setParams(baseParams)
//                .execute();
    }

    /**
     * 注册用户
     *
     * @param context   上下文
     * @param iCallback 回调接口
     */
    private static void register(Context context, final ICallback<TokenEntity> iCallback) {
        BaseParams baseParams = new BaseParams(101);
        baseParams.setT(1101);
        baseParams.setToken("");
        baseParams.setTag(context);
        NetworkRequest.getInstance()
                .getHttpConnection()
                .request(NetworkRequest.getRequestParamSignImp(baseParams), new JsonCallback<TokenEntity>(TokenEntity.class) {
                    @Override
                    public void onSuccessed(int code, String msg, boolean isFromCache, TokenEntity result) {
                        if (iCallback != null)
                            iCallback.onCallback(code, result, msg);
                    }

                    @Override
                    public void onFailed(int code, String msg, boolean isFromCache) {
                        if (iCallback != null)
                            iCallback.onCallback(code, null, msg);
                    }
                });
//                .setT(1101)
//                .setTag(context)
//                .setToken("")
//                .setParams(baseParams)
//                .execute();
    }

}
