package com.finance.base;

/**
 * Presenter
 */
public interface IBasePresenter<T extends IBaseView> {

    /**
     * Presenter入口
     */
    void start();

    /**
     * 绑定View
     *
     * @param view
     */
    void attachView(T view);

    /**
     * 销毁View
     */
    void detachView();

}
