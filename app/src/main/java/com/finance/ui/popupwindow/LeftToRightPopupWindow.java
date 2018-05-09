package com.finance.ui.popupwindow;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.finance.widget.animation.BaseAnimatorSet;

/**
 * 从左到右显示的动画
 */
public abstract class LeftToRightPopupWindow extends BasePopupWindow {

    public LeftToRightPopupWindow(Context context, int width, int height) {
        super(context, width, height);
    }

    public LeftToRightPopupWindow(Context context, int width, int height, int x, int y) {
        super(context, width, height, x, y);
    }

    @Override
    protected boolean isAnimation() {
        return true;
    }

    @Override
    protected BaseAnimatorSet getShowAs() {
        if (mAminationView == null) return null;
        final ViewGroup.LayoutParams params = mAminationView.getLayoutParams();
        return new BaseAnimatorSet() {
            @Override
            public void setAnimation(View view) {
                ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mAnimViewWidth);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        params.width = (int) animation.getAnimatedValue();
                        mAminationView.setLayoutParams(params);
                    }
                });
                animatorSet.playTogether(valueAnimator);
            }
        };
    }

    @Override
    protected BaseAnimatorSet getDismissAs() {
        if (mAminationView == null) return null;
        final ViewGroup.LayoutParams params = mAminationView.getLayoutParams();
        return new BaseAnimatorSet() {
            @Override
            public void setAnimation(View view) {
                ValueAnimator valueAnimator = ValueAnimator.ofInt(mAnimViewWidth, 0);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        params.width = (int) animation.getAnimatedValue();
                        mAminationView.setLayoutParams(params);
                    }
                });
                animatorSet.playTogether(valueAnimator);
            }
        };
    }


}
