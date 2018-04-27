package com.finance.model.http;

/**
 * 公共参数
 */

import com.finance.common.Constants;

import java.util.HashMap;

import javax.xml.transform.Source;

public class BaseParams {

    private HashMap<String, Object> params = new HashMap<String, Object>(10);

    public BaseParams() {
        //        this.SourceCode = 0;
//        this.Sign = '';
//        this.Token = '';
//        this.T = 200;//默认200 普通请求
        params.put("Device", Constants.DEVICETYPE);
        params.put("PlatformId", Constants.PLATFORMID);
//        params.put("OperationIP", "1");
//        params.put("Source", 1);
        params.put("MessageId", "3fc6bf1a-a7d1-4136-9ca5-5b304799b03c");//默认为1，可以根据业务更改
        params.put("T", 200);
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

}
