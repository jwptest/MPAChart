package com.finance.ui.popupwindow;

import android.animation.Animator;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.finance.interfaces.IDismiss;
import com.finance.widget.animation.BaseAnimatorSet;

import butterknife.ButterKnife;

/**
 * PopupWindow基类
 */
public abstract class BasePopupWindow extends PopupWindow {

    protected LinearLayout rootView;
    protected View mAminationView;
    private IDismiss dismiss;
    //动画执行时间
    protected long mInnerAnimDuration = 1000;
    protected int mAnimViewWidth, mAnimViewHeight;

    public BasePopupWindow(Context context, int width, int height) {
        this(context, width, height, 0, 0);
    }

    public BasePopupWindow(Context context, int width, int height, int x, int y) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(context, width, height, x, y);
    }

    private void setContentView(Context context, int width, int height, int x, int y) {
        View contentView = getLayoutView();
        if (contentView == null)
            contentView = LayoutInflater.from(context).inflate(getLayoutId(), null);
        rootView = new LinearLayout(context);
        rootView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.leftMargin = x;
        params.topMargin = y;
        rootView.addView(contentView, params);
        setContentView(rootView);
        if (isBindView()) {
            ButterKnife.bind(this, contentView);
        }
        mAminationView = contentView;
    }

    protected int getLayoutId() {
        return -1;
    }

    protected View getLayoutView() {
        return null;
    }

    protected abstract boolean isBindView();

    protected abstract BaseAnimatorSet getShowAs();

    protected abstract BaseAnimatorSet getDismissAs();

    public void setDismiss(IDismiss dismiss) {
        this.dismiss = dismiss;
    }

    @Override
    public void dismiss() {
        if (!startDismissAnimation()) {
            superDismiss();
        }
    }

    public void superDismiss() {
        if (isShowing()) {
            try {
                super.dismiss();
            } catch (IllegalArgumentException e) {
            } catch (Exception e) {
            }
        }
        if (dismiss != null)
            dismiss.onDismiss();
    }

    @Override
    public void setOutsideTouchable(boolean touchable) {
        super.setOutsideTouchable(touchable);
        if (!touchable) return;
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void monitoringShow() {
        mAminationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mAnimViewWidth = mAminationView.getWidth();
                if (mAnimViewWidth <= 0) return;
                mAnimViewHeight = mAminationView.getHeight();
                mAminationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                startShowAnimation();
            }
        });
    }

    //执行显示动画
    protected boolean startShowAnimation() {
        BaseAnimatorSet showAs = getShowAs();
        if (showAs != null) {
            showAs.duration(mInnerAnimDuration).playOn(mAminationView);
            return true;
        }
        return false;
    }

    protected boolean startDismissAnimation() {
        BaseAnimatorSet dismissAs = getDismissAs();
        if (dismissAs != null) {//执行关闭动画
            dismissAs.listener(new BaseAnimatorSet.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    superDismiss();
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    superDismiss();
                }
            });
            dismissAs.duration(mInnerAnimDuration).playOn(mAminationView);
            return true;
        }
        return false;
    }

    public void showPopupWindow(View view) {
        monitoringShow();
        showAtLocation(view, Gravity.START, 0, 0);
    }

}
