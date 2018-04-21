package com.finance.interfaces;

import android.view.View;

/**
 * 菜单处理接口
 */
public interface IViewHandler extends IDestroy {
    IViewHandler onInit(View rootView);
}
