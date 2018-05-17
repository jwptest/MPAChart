package com.finance.linechartdata;

import android.animation.Animator;
import android.animation.ValueAnimator;

import com.finance.base.BaseAminatorListener;
import com.github.mikephil.charting.data.Entry;

/**
 * 最后一个点移动动画
 */
public class ChartCurrentPointAnimation {

    private int duration = 300;//动画执行时长
    private float bX, bY;//移动点的基础值
    private float cX, cY;//移动点的速率
    private Entry current;//当前移动点
    private IinvalidateChart mChart;
    private ValueAnimator mValueAnimator;

    public ChartCurrentPointAnimation(IinvalidateChart chart) {
        this.mChart = chart;
    }

    public ChartCurrentPointAnimation updateParam(Entry current, Entry previous) {
        this.current = current;
        bX = previous.getX();
        bY = previous.getY();
        cX = (current.getX() - previous.getX()) / duration;
        cY = (current.getY() - previous.getY()) / duration;
        return this;
    }

    public void startAnimation() {
        //执行动画
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofInt(0, duration);
            mValueAnimator.setDuration(duration);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int f = (int) animation.getAnimatedValue();
                    current.setX(bX + cX * f);
                    current.setY(bY + cY * f);
                    mChart.invalidateChart();
                }
            });
        }
        mValueAnimator.start();

//        mValueAnimator.addListener(new BaseAminatorListener() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//        });
    }

    public void stopAnimation() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
    }

    public interface IinvalidateChart {
        void invalidateChart();//刷新走势图
    }

}
