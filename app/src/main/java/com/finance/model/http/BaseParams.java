package com.finance.model.http;

/**
 * 公共参数
 */

import com.finance.common.Constants;
import com.finance.common.UserShell;

import java.util.HashMap;

import javax.xml.transform.Source;

import static android.R.attr.key;
import static java.nio.file.Paths.get;

public class BaseParams {

    private HashMap<String, Object> params = new HashMap<String, Object>(10);

    public BaseParams() {
        this(true, 0);
    }

    public BaseParams(int SourceCode) {
        this(false, SourceCode);
    }

    public BaseParams(boolean isEmpty, int SourceCode) {
        if (isEmpty) return;
        //        this.SourceCode = 0;
//        this.Sign = '';
//        this.Token = '';
//        this.T = 200;//默认200 普通请求
        params.put("Device", Constants.DEVICETYPE);
        params.put("PlatformId", Constants.PLATFORMID);
//        params.put("OperationIP", "1");
        params.put("Token", UserShell.getInstance().getUserToken());
        params.put("MessageId", "3fc6bf1a-a7d1-4136-9ca5-5b304799b03c");//默认为1，可以根据业务更改
        params.put("T", 200);
        params.put("SourceCode", SourceCode);//请求接口
    }

    public int getSourceCode() {
        return (int) params.get("SourceCode");
//        params.containsKey("SourceCode");
    }

    public String getToken() {
        return params.get("Token") + "";
    }

    public BaseParams addParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public BaseParams addParam(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public void clearParams() {
        params.clear();
    }

    //获取参数
    public HashMap<String, Object> getParams() {
        return params;
    }

}
