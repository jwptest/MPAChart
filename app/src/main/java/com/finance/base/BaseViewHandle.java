package com.finance.base;

import android.view.View;

import com.finance.interfaces.IViewHandler;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseViewHandle implements IViewHandler {

    protected Unbinder unbinder;
    protected View rootView;

    @Override
    public BaseViewHandle onInit(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);
        this.rootView = rootView;
        return this;
    }

    @Override
    public void onDestroy() {
        if (unbinder != null && unbinder != Unbinder.EMPTY)
            unbinder.unbind();
        rootView = null;
    }

}
