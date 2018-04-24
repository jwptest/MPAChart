package com.finance.widget.combinedchart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.renderer.LineChartRenderer;

/**
 * 走势图控件
 */
public class MLineChart2 extends BarLineChartBase<LineData> implements LineDataProvider {

    private MLineChartRenderer mcombinedchartRenderer;

    public MLineChart2(Context context) {
        super(context);
    }

    public MLineChart2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MLineChart2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        mcombinedchartRenderer = null;
        mcombinedchartRenderer = new MLineChartRenderer(this, mAnimator, mViewPortHandler);
        mRenderer = mcombinedchartRenderer;
        //替换右轴绘制类
        mAxisRendererRight = new MYAxisRightRenderer(mViewPortHandler, mAxisRight, mRightAxisTransformer);
        //提现X轴绘制类
        mXAxisRenderer = new MXAxisRenderer(mViewPortHandler, mXAxis, mLeftAxisTransformer);
    }

    @Override
    public LineData getLineData() {
        return mData;
    }

    @Override
    protected void onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if (mRenderer != null && mRenderer instanceof LineChartRenderer) {
            ((LineChartRenderer) mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //没有显示则不刷新
        if (getVisibility() != View.VISIBLE) return;
        super.onDraw(canvas);
    }

    public MLineChartRenderer getMLineChartRenderer() {
        return mcombinedchartRenderer;
    }

}
