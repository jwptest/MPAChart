package com.finance.ui.main;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

import com.finance.base.BaseAminatorListener;
import com.finance.model.ben.PurchaseViewEntity;

/**
 * 购买点显示和隐藏的动画
 */
public class PurchaseViewAnimation extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {

    private View animView;//执行动画的view
    private ViewGroup.LayoutParams mLayoutParams;

    public PurchaseViewAnimation(View animView) {
        this.animView = animView;
        mLayoutParams = animView.getLayoutParams();
        addUpdateListener(this);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        //执行动画
        mLayoutParams.width = (int) animation.getAnimatedValue();
        animView.setLayoutParams(mLayoutParams);
    }

//    getDisplayAnimation

    /**
     * 获取一个显示和隐藏动画
     */
    public static PurchaseViewAnimation getCompleteAnimation(final View animView) {
        PurchaseViewAnimation animation = getDisplayAnimation(animView);
        animation.addListener(new BaseAminatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                PurchaseViewAnimation anim = getHideAnimation(animView);
                anim.addListener(new BaseAminatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animView.setVisibility(View.GONE);
                    }
                });
                anim.start();
            }
        });
//        animView.setVisibility(View.VISIBLE);
        return animation;
    }

    //隐藏动画
    public static PurchaseViewAnimation getHideAnimation(View animView) {
        PurchaseViewAnimation animation = getAnimation(animView, 3000);
        animation.setIntValues(PurchaseViewEntity.moneyTextWidth, PurchaseViewEntity.iconWidth);
        return animation;
    }

    //显示动画
    public static PurchaseViewAnimation getDisplayAnimation(View animView) {
        PurchaseViewAnimation animation = getAnimation(animView, 0);
        animation.setIntValues(PurchaseViewEntity.iconWidth, PurchaseViewEntity.moneyTextWidth);
        return animation;
    }

    /**
     * 获取动画
     */
    public static PurchaseViewAnimation getAnimation(View animView, long startDelay) {
        PurchaseViewAnimation animation = new PurchaseViewAnimation(animView);
        animation.setDuration(300);//执行时长
        animation.setStartDelay(startDelay);
        return animation;
    }

}
