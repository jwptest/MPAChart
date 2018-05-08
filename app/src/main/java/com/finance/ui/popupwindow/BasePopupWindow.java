package com.finance.ui.popupwindow;

import android.animation.Animator;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    protected long mInnerAnimDuration = 500;

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
        contentView.get
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
        BaseAnimatorSet dismissAs = getDismissAs();
        if (dismissAs != null) {
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
        } else {
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

    public void showPopupWindow(View view) {
        showAtLocation(view, Gravity.START, 0, 0);
        BaseAnimatorSet showAs = getShowAs();
        if (showAs != null) {
            showAs.duration(mInnerAnimDuration).playOn(mAminationView);
        }
    }

}
