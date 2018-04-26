package com.finance.model.http;

/**
 * 公共参数
 */

import com.finance.common.Constants;

import java.util.HashMap;

public class BaseParams {

    private HashMap<String, String> params = new HashMap<String, String>(10);

    public BaseParams() {
        //        this.SourceCode = 0;
//        this.Sign = '';
//        this.Token = '';
//        this.T = 200;//默认200 普通请求
        params.put("Device", Constants.DEVICETYPE);
        params.put("PlatformId", "0");
        params.put("MessageId", "1");//默认为1，可以根据业务更改
        params.put("T", "200");
    }

    public BaseParams addParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public BaseParams addParam(Object key, Object value) {
        params.put(key + "", value + "");
        return this;
    }

}
