package com.finance.widget.combinedchart;

import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.Log;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * 重写右边框绘制类
 */
public class MYAxisRightRenderer extends YAxisRenderer {
    //YAxisRendererRadarChart
    private float fixedPosition;
    private String measureText = "";

    public MYAxisRightRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
    }

    @Override
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                ? mYAxis.mEntryCount
                : (mYAxis.mEntryCount - 1);
        this.fixedPosition = fixedPosition;
//        Log.d("1234", "-------------------------");
//        for (int i = from; i < to; i++) {
//            String text = mYAxis.getFormattedLabel(i);
//            Log.d("1234", "drawYLabels: " + text);
//        }
        // draw
        for (int i = from + 1; i < to - 1; i++) {
            String text = mYAxis.getFormattedLabel(i);
            c.drawText(text, fixedPosition, positions[i * 2 + 1] + offset, mAxisLabelPaint);
//            c.drawText(text, fixedPosition, positions[i * 2 + 1], mAxisLabelPaint);
            if (i == from) measureText = text;
        }
    }

//    @Override
//    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
//        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
//        final int to = mYAxis.isDrawTopYLabelEntryEnabled()
//                ? mYAxis.mEntryCount
//                : (mYAxis.mEntryCount - 1);
//        this.fixedPosition = fixedPosition;
//        // draw 最后一个值不绘制
//        for (int i = from + 1; i < to; i++) {
//
//            String text = mYAxis.getFormattedLabel(i);
//
//            c.drawText(text, fixedPosition, positions[i * 2 + 1] + offset, mAxisLabelPaint);
//        }
//    }

    public float getFixedPosition() {
        return fixedPosition;
//        if (TextUtils.isEmpty(measureText)) return 0;
//        YAxis.YAxisLabelPosition position = mYAxis.getLabelPosition();
//        if (YAxis.YAxisLabelPosition.INSIDE_CHART == position) {//画在x轴内
//            return fixedPosition - mAxisLabelPaint.measureText(measureText);
//        } else {//画在x轴外
//            return fixedPosition;
//        }
    }

    public float getLabelWidth() {
        if (TextUtils.isEmpty(measureText)) {
            measureText = "1.000000";
        }
        return mAxisLabelPaint.measureText(measureText);
    }

}
