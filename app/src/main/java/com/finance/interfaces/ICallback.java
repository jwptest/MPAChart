package com.finance.interfaces;

/**
 * 网络请求回调
 */
public interface ICallback<T> {

    /**
     * 请求成功返回T，反之返回null
     */
    void onCallback(int code,T t, String message);

}
