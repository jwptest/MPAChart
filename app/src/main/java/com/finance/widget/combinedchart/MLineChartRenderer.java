package com.finance.widget.combinedchart;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * 绘制走势图控件
 */
public class MLineChartRenderer extends LineChartRenderer {

    public MLineChartRenderer(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
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

    private float[] mLineBuffer = new float[4];

    @Override
    protected void drawLinear(Canvas c, ILineDataSet dataSet) {

        int entryCount = dataSet.getEntryCount();

        final boolean isDrawSteppedEnabled = dataSet.isDrawSteppedEnabled();
        final int pointsPerEntryPair = isDrawSteppedEnabled ? 4 : 2;

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        float phaseY = mAnimator.getPhaseY();

        mRenderPaint.setStyle(Paint.Style.STROKE);

        Canvas canvas = null;

        // if the data-set is dashed, draw on bitmap-canvas
        if (dataSet.isDashedLineEnabled()) {
            canvas = mBitmapCanvas;
        } else {
            canvas = c;
        }

        mXBounds.set(mChart, dataSet);

        // if drawing filled is enabled
        if (dataSet.isDrawFilledEnabled() && entryCount > 0) {
            drawLinearFill(c, dataSet, trans, mXBounds);
        }

        int length = mXBounds.range + mXBounds.min;
        int step = getStep2(length);
        // more than 1 color
        if (dataSet.getColors().size() > 1) {

            if (mLineBuffer.length <= pointsPerEntryPair * 2)
                mLineBuffer = new float[pointsPerEntryPair * 4];

            boolean p = true;
            for (int j = mXBounds.min + step; j <= length; j += step) {
                if (p && j + step > length) {
                    j = length - step;
                    p = false;
                }
                Entry e = dataSet.getEntryForIndex(j);
                if (e == null) continue;
                mLineBuffer[0] = e.getX();
                mLineBuffer[1] = e.getY() * phaseY;
                if (j < mXBounds.max) {
                    e = dataSet.getEntryForIndex(j + step);
                    if (e == null) break;
                    if (isDrawSteppedEnabled) {
                        mLineBuffer[2] = e.getX();
                        mLineBuffer[3] = mLineBuffer[1];
                        mLineBuffer[4] = mLineBuffer[2];
                        mLineBuffer[5] = mLineBuffer[3];
                        mLineBuffer[6] = e.getX();
                        mLineBuffer[7] = e.getY() * phaseY;
                    } else {
                        mLineBuffer[2] = e.getX();
                        mLineBuffer[3] = e.getY() * phaseY;
                    }
                } else {
                    mLineBuffer[2] = mLineBuffer[0];
                    mLineBuffer[3] = mLineBuffer[1];
                }
                trans.pointValuesToPixel(mLineBuffer);
                if (!mViewPortHandler.isInBoundsRight(mLineBuffer[0]))
                    break;
                // make sure the lines don't do shitty things outside
                // bounds
                if (!mViewPortHandler.isInBoundsLeft(mLineBuffer[2])
                        || (!mViewPortHandler.isInBoundsTop(mLineBuffer[1]) && !mViewPortHandler
                        .isInBoundsBottom(mLineBuffer[3])))
                    continue;
                // get the color that is set for this line-segment
                mRenderPaint.setColor(dataSet.getColor(j));

                canvas.drawLines(mLineBuffer, 0, pointsPerEntryPair * 2, mRenderPaint);
            }
        } else { // only one color per dataset

            if (mLineBuffer.length < Math.max((entryCount) * pointsPerEntryPair, pointsPerEntryPair) * 2)
                mLineBuffer = new float[Math.max((entryCount) * pointsPerEntryPair, pointsPerEntryPair) * 4];

            Entry e1, e2;

            e1 = dataSet.getEntryForIndex(mXBounds.min);

            if (e1 != null) {
                boolean p = true;
                int j = 0;
                for (int x = mXBounds.min + step; x <= length; x += step) {
                    if (p && x + step > length) {
                        x = length - step;
                        p = false;
                    }
                    e1 = dataSet.getEntryForIndex(x == 0 ? 0 : (x - step));
                    e2 = dataSet.getEntryForIndex(x);

                    if (e1 == null || e2 == null) continue;

                    mLineBuffer[j++] = e1.getX();
                    mLineBuffer[j++] = e1.getY() * phaseY;

                    if (isDrawSteppedEnabled) {
                        mLineBuffer[j++] = e2.getX();
                        mLineBuffer[j++] = e1.getY() * phaseY;
                        mLineBuffer[j++] = e2.getX();
                        mLineBuffer[j++] = e1.getY() * phaseY;
                    }

                    mLineBuffer[j++] = e2.getX();
                    mLineBuffer[j++] = e2.getY() * phaseY;
                }

                if (j > 0) {
                    trans.pointValuesToPixel(mLineBuffer);

                    final int size = Math.max((mXBounds.range + 1) * pointsPerEntryPair, pointsPerEntryPair) * 2;

                    mRenderPaint.setColor(dataSet.getColor());

                    canvas.drawLines(mLineBuffer, 0, size, mRenderPaint);
                }
            }
        }
        mRenderPaint.setPathEffect(null);
    }

    //间隔步长
    private int getStep(int length) {
        if (length <= 120) return 0;
        return length / 120;
    }

    //间隔步长
    private int getStep2(int length) {
        if (length <= 120) return 1;
        return length / 120;
    }

}
