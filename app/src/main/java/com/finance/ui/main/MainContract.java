package com.finance.ui.main;


import com.finance.base.IBasePresenter;
import com.finance.base.IBaseView;

/**
 * 首页接口
 */
public interface MainContract {

    interface View extends IBaseView {

    }

    interface Presenter extends IBasePresenter<View> {

    }

}
