package com.finance.model.http;

import java.util.HashMap;

/**
 * 数据签名接口
 */
public interface ISign {

    /**
     * 获取签名后的参数集合
     *
     * @param params 所有参数
     * @return 设置签名后的参数Json
     */
    String getSign(HashMap<String, Object> params);

}
