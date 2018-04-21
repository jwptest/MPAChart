package com.finance.base;

/**
 * ===============================
 * 描    述：
 * 作    者：pjw
 * 创建日期：2018/4/19 10:50
 * ===============================
 */
public class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    protected V mView;

    @Override
    public void start() {

    }

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

}
