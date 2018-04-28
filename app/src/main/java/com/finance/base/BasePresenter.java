package com.finance.base;

/**
 *
 */
public class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    protected V mView;

    public BasePresenter(V view) {
        mView = view;
    }

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
