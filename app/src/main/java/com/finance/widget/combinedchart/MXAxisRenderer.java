package com.finance.widget.combinedchart;

import android.graphics.Canvas;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * X轴绘制标签
 */
public class MXAxisRenderer extends XAxisRenderer {

    public MXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }

    //重写绘制标签的方法
    @Override
    protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
        Utils.drawXAxisValue(c, formattedLabel, x + mXAxis.getXOffset(), y, mAxisLabelPaint, anchor, angleDegrees);
    }

//    /**
//     * draws the x-labels on the specified y-position
//     *
//     * @param pos
//     */
//    protected void drawLabels(Canvas c, float pos, MPPointF anchor) {
//
//        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
//        boolean centeringEnabled = mXAxis.isCenterAxisLabelsEnabled();
//
//        float[] positions = new float[mXAxis.mEntryCount * 2];
//
//        for (int i = 0; i < positions.length; i += 2) {
//
//            // only fill x values
//            if (centeringEnabled) {
//                positions[i] = mXAxis.mCenteredEntries[i / 2];
//            } else {
//                positions[i] = mXAxis.mEntries[i / 2];
//            }
//        }
//
//        mTrans.pointValuesToPixel(positions);
//
//        for (int i = 0; i < positions.length; i += 2) {
//
//            float x = positions[i];
//
//            if (mViewPortHandler.isInBoundsX(x)) {
//
//                String label = mXAxis.getValueFormatter().getFormattedValue(mXAxis.mEntries[i / 2], mXAxis);
//
//                if (mXAxis.isAvoidFirstLastClippingEnabled()) {
//
//                    // avoid clipping of the last
//                    if (i == mXAxis.mEntryCount - 1 && mXAxis.mEntryCount > 1) {
//                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);
//
//                        if (width > mViewPortHandler.offsetRight() * 2
//                                && x + width > mViewPortHandler.getChartWidth())
//                            x -= width / 2;
//
//                        // avoid clipping of the first
//                    } else if (i == 0) {
//
//                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);
//                        x += width / 2;
//                    }
//                }
//
//                drawLabel(c, label, x, pos, anchor, labelRotationAngleDegrees);
//            }
//        }
//    }

}
