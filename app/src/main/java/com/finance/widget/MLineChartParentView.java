package com.finance.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 画图控件的父控件
 */
public class MLineChartParentView extends RelativeLayout {

    public MLineChartParentView(Context context) {
        super(context);
    }

    public MLineChartParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MLineChartParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MLineChartParentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void removeView(View view) {

    }

    public void removeView(View... views) {

    }

}
