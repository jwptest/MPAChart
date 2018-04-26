package com.finance.model.http;

import com.finance.model.ben.ResponseEntity;

/**
 * 实例化数据
 */
public interface IJsonToEn {

    <R extends ResponseEntity> R jsonToEn(String json);

}
