package com.finance.linechartdata;

import android.animation.ValueAnimator;

import com.github.mikephil.charting.data.Entry;

/**
 * 最后一个点移动动画
 */
public class ChartCurrentPointAnimation {

    private Entry mEntry;//当前移动点
    private int duration = 300;//动画执行时长
    private float bX, bY;//移动点的基础值
    private float cX, cY;//移动点的速率
    private Entry current;
    private IinvalidateChart mChart;

    public ChartCurrentPointAnimation() {
    }

    public ChartCurrentPointAnimation updateParam(Entry current, Entry previous) {
        this.mEntry = current;
        bX = previous.getX();
        bY = previous.getY();
        cX = (current.getX() - previous.getX()) / duration;
        cY = (current.getY() - previous.getY()) / duration;
        return this;
    }

    public void startAnimation() {
        //执行动画
        ValueAnimator mValueAnimator = ValueAnimator.ofInt(0, duration);
        mValueAnimator.setDuration(duration);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (float) animation.getAnimatedValue();
                mEntry.setX(bX + cX * f);
                mEntry.setY(bY + cY * f);
                mChart.invalidateChart();
            }
        });
    }

    public interface IinvalidateChart {
        void invalidateChart();//刷新走势图
    }

}
