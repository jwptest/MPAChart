package com.finance.ui.popupwindow;

import android.content.Context;

import com.finance.widget.animation.BaseAnimatorSet;

/**
 *
 */
public abstract class LeftToRightPopupWindow extends BasePopupWindow {

    public LeftToRightPopupWindow(Context context, int width, int height) {
        super(context, width, height);
    }

    public LeftToRightPopupWindow(Context context, int width, int height, int x, int y) {
        super(context, width, height, x, y);
    }

    @Override
    protected BaseAnimatorSet getShowAs() {
        return null;
    }

    @Override
    protected BaseAnimatorSet getDismissAs() {
        return null;
    }


}
