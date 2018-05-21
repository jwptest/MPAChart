package com.finance.model.http;

/**
 * 公共参数
 */

import com.finance.common.Constants;
import com.finance.common.UserShell;
import com.finance.utils.MD5Util;

import java.util.HashMap;

public class BaseParams {

    private String D;
    private int T;
    private String Token;
    private HashMap<String, Object> params = new HashMap<String, Object>(10);
    private boolean isEmpty;
    private String messageId;
    private Object tag;
    private ISign mISign;

    public BaseParams() {
        this(true, 0);
    }

    public BaseParams(int SourceCode) {
        this(false, SourceCode);
    }

    public BaseParams(boolean isEmpty, int SourceCode) {
        this.isEmpty = isEmpty;
        T = 200;
        Token = UserShell.getInstance().getUserToken();
        messageId = getMessageId(SourceCode);
        if (isEmpty) return;
        //        this.SourceCode = 0;
//        this.Sign = '';
//        this.Token = '';
//        this.T = 200;//默认200 普通请求
        params.put("Device", Constants.DEVICETYPE);
        params.put("PlatformId", Constants.PLATFORMID);
//        params.put("OperationIP", "1");
        params.put("Token", Token);
        params.put("MessageId", messageId);//默认为1，可以根据业务更改
        params.put("T", T);
        params.put("SourceCode", SourceCode);//请求接口
    }

    public int getSourceCode() {
        if (params.containsKey("SourceCode")) {
            return (int) params.get("SourceCode");
        }
        return 0;
    }

    public String getMessageId() {
        return messageId;
    }

    private String getMessageId(int SourceCode) {
        return MD5Util.getInstance().getMD5String(System.currentTimeMillis() + "SourceCode:" + SourceCode);
    }

    public String getToken() {
        return Token;
    }

    public BaseParams addParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public BaseParams addParam(String key, Object value) {
        params.put(key, value);
        return this;
    }

    //获取参数
    public HashMap<String, Object> getParams() {
        return params;
    }

    public int getT() {
        return T;
    }

    public BaseParams setT(int t) {
        T = t;
        params.put("T", t);
        return this;
    }

    public BaseParams setToken(String token) {
        Token = token;
        params.put("Token", token);
        return this;
    }

    public Object getTag() {
        return tag;
    }

    public BaseParams setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    //设置格式参数的对象
    public BaseParams setISign(ISign ISign) {
        mISign = ISign;
        return this;
    }

    public BaseParams setD(String d) {
        D = d;
        params.put("D", d);
        return this;
    }

    public String getD() {
        if (D == null) {
            D = mISign.getDJson(params);
        }
        return D;
    }

    public String sendMessage() {
        return mISign.getSendJson(params, getD(), T, Token);
//        if (isEmpty) {
//            return com.finance.model.imps.NetworkRequest.getInstance().getSignBasic().getSendJson(params, getD(), T, Token);
//        } else {
//            return com.finance.model.imps.NetworkRequest.getInstance().getsSignImp().getSendJson(params, getD(), T, Token);
//        }
    }

}
