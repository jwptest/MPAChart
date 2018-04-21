package com.finance.widget.linechart;

import android.graphics.Paint;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * 绘制走势图控件
 */
public class MLineChartRenderer extends LineChartRenderer {

    private OnDrawCompletion mCompletion;

    public MLineChartRenderer(com.finance.widget.linechart.MLineChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    @Override
    protected void drawCubicBezier(ILineDataSet dataSet) {
        float phaseY = mAnimator.getPhaseY();
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());
        mXBounds.set(mChart, dataSet);
        float intensity = dataSet.getCubicIntensity();
        cubicPath.reset();
        if (mXBounds.range >= 1) {
            float prevDx;
            float prevDy;
            float curDx;
            float curDy;
            // Take an extra point from the left, and an extra from the right.
            // That's because we need 4 points for a cubic bezier (cubic=4), otherwise we get lines moving and doing weird stuff on the edges of the chart.
            // So in the starting `prev` and `cur`, go -2, -1
            // And in the `lastIndex`, add +1
            final int firstIndex = mXBounds.min + 1;
            Entry prevPrev;
            Entry prev = dataSet.getEntryForIndex(Math.max(firstIndex - 2, 0));
            Entry cur = dataSet.getEntryForIndex(Math.max(firstIndex - 1, 0));
            Entry next = cur;
            int nextIndex = -1;

            if (cur == null) return;

            // let the spline start
            cubicPath.moveTo(cur.getX(), cur.getY() * phaseY);
            int size = mXBounds.range + mXBounds.min;
            int step = getStep(size);
            if (step > 0) {
                boolean p = true;
                for (int j = mXBounds.min + 1; j <= size; j += step) {
                    if (p && j + step > size) {
                        j = size - step;
                        p = false;
                    }
                    prevPrev = prev;
                    prev = cur;
                    cur = nextIndex == j ? next : dataSet.getEntryForIndex(j);

                    nextIndex = j + 1 < dataSet.getEntryCount() ? j + 1 : j;
                    next = dataSet.getEntryForIndex(nextIndex);

                    prevDx = (cur.getX() - prevPrev.getX()) * intensity;
                    prevDy = (cur.getY() - prevPrev.getY()) * intensity;
                    curDx = (next.getX() - prev.getX()) * intensity;
                    curDy = (next.getY() - prev.getY()) * intensity;

                    cubicPath.cubicTo(prev.getX() + prevDx, (prev.getY() + prevDy) * phaseY,
                            cur.getX() - curDx,
                            (cur.getY() - curDy) * phaseY, cur.getX(), cur.getY() * phaseY);
                }
            } else {
                for (int j = mXBounds.min + 1; j <= size; j++) {
                    prevPrev = prev;
                    prev = cur;
                    cur = nextIndex == j ? next : dataSet.getEntryForIndex(j);

                    nextIndex = j + 1 < dataSet.getEntryCount() ? j + 1 : j;
                    next = dataSet.getEntryForIndex(nextIndex);

                    prevDx = (cur.getX() - prevPrev.getX()) * intensity;
                    prevDy = (cur.getY() - prevPrev.getY()) * intensity;
                    curDx = (next.getX() - prev.getX()) * intensity;
                    curDy = (next.getY() - prev.getY()) * intensity;

                    cubicPath.cubicTo(prev.getX() + prevDx, (prev.getY() + prevDy) * phaseY,
                            cur.getX() - curDx,
                            (cur.getY() - curDy) * phaseY, cur.getX(), cur.getY() * phaseY);
                }
            }
            if (size > 0) {
                drawCompletion(dataSet.getEntryForIndex(size), dataSet);
            }
        }
        // if filled is enabled, close the path
        if (dataSet.isDrawFilledEnabled()) {
            cubicFillPath.reset();
            cubicFillPath.addPath(cubicPath);
            drawCubicFill(mBitmapCanvas, dataSet, cubicFillPath, trans, mXBounds);
        }

        mRenderPaint.setColor(dataSet.getColor());

        mRenderPaint.setStyle(Paint.Style.STROKE);

        trans.pathValueToPixel(cubicPath);

        mBitmapCanvas.drawPath(cubicPath, mRenderPaint);
        mRenderPaint.setPathEffect(null);
    }

    //间隔步长
    private int getStep(int length) {
        if (length <= 100) return 0;
        return length / 100;
    }

    public MLineChartRenderer setOnCompletion(OnDrawCompletion completion) {
        mCompletion = completion;
        return this;
    }

    //绘制完成回调
    private void drawCompletion(Entry entry, ILineDataSet dataSet) {
        if (mCompletion == null) return;
        //将数据转换为坐标
//        mCompletion.completion(entry, (int) (mViewPortHandler.contentLeft() + pixels.x), (int) (mViewPortHandler.contentTop() + pixels.y));
        mCompletion.completion(entry, dataSet);
    }

    public interface OnDrawCompletion {
        /**
         * 图表绘制完成回调
         */
        void completion(Entry last, ILineDataSet dataSet);
    }

}
