package com.finance.ui.main;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.finance.R;
import com.finance.base.BaseAminatorListener;
import com.finance.base.BaseViewHandle;
import com.finance.ui.popupwindow.OrderPopupWindow;
import com.finance.utils.BtnClickUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 左菜单处理
 */
public class LeftMenu extends BaseViewHandle {

    @BindView(R.id.vItemBg)
    View vItemBg;
    @BindView(R.id.ivTransaction)
    ImageView ivTransaction;
    @BindView(R.id.tvTransaction)
    TextView tvTransaction;
    @BindView(R.id.ivOrder)
    ImageView ivOrder;
    @BindView(R.id.tvOrder)
    TextView tvOrder;
    @BindView(R.id.ivDynamic)
    ImageView ivDynamic;
    @BindView(R.id.tvDynamic)
    TextView tvDynamic;

    private Activity mActivity;

    private RelativeLayout.LayoutParams mLayoutParams;

    private ValueAnimator anim;//背景移动动画
    private MBaseAminatorListener mListener;//动画执行监听

    private TextView currentSelect;

    public LeftMenu(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public LeftMenu onInit(View rootView) {
        super.onInit(rootView);
//        R.layout.home_layout_left_menu
        onGlobalLayoutListener(ivTransaction);
        onGlobalLayoutListener(tvTransaction);
        currentSelect = tvTransaction;
        return this;
    }

    private void initItemBg() {
        if (ivTransaction.getHeight() <= 0 || tvTransaction.getHeight() <= 0) return;
        mLayoutParams = (RelativeLayout.LayoutParams) vItemBg.getLayoutParams();
        mLayoutParams.height = ivTransaction.getHeight() + tvTransaction.getHeight();
        vItemBg.setLayoutParams(mLayoutParams);
        vItemBg.setTag("0");
        vItemBg.setVisibility(View.VISIBLE);
    }

    private void onGlobalLayoutListener(final View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (view.getHeight() <= 0) return;
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initItemBg();
            }
        });
    }

    @OnClick({R.id.ivTransaction, R.id.tvTransaction, R.id.ivOrder, R.id.tvOrder, R.id.ivDynamic, R.id.tvDynamic})
    public void onViewClicked(View view) {
        if (BtnClickUtil.isFastDoubleClick(view.getId())) {
            //防止双击
            return;
        }
        switch (view.getId()) {
            case R.id.ivTransaction://交易
            case R.id.tvTransaction:
                startAnimation(0, tvTransaction);
                break;
            case R.id.ivOrder://订单
            case R.id.tvOrder:
                startAnimation(1, tvOrder);
                break;
            case R.id.ivDynamic://动态
            case R.id.tvDynamic:
                startAnimation(2, tvDynamic);
                break;
        }
    }

    private void startAnimation(int toItem, TextView textView) {
        String tab = vItemBg.getTag() + "";
        int item = 0;
        try {
            item = Integer.valueOf(tab);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (toItem == item) return;
        int hight = mLayoutParams.height;
        int from = hight * item;
        int to = hight * toItem;
        vItemBg.setTag(toItem);
        getValueAnimator(from, to, toItem, textView).start();
    }

    private ValueAnimator getValueAnimator(int from, int to, int tag, TextView textView) {
        if (anim == null) {
            anim = ValueAnimator.ofInt();
            anim.setDuration(200);
            anim.setIntValues();
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mLayoutParams.topMargin = (Integer) animation.getAnimatedValue();
                    rootView.requestLayout();//刷新布局
                }
            });
            mListener = new MBaseAminatorListener();
            anim.addListener(mListener);
        }
        anim.setIntValues(from, to);
        mListener.setToTextView(textView);
        mListener.setTag(tag);
        return anim;
    }

    private void setSelectText(TextView textView, int tag) {
        if (currentSelect != null)
            currentSelect.setTextColor(Color.parseColor("#333333"));
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        currentSelect = textView;

        if (tag == 0) {
            //交易
        } else if (tag == 1) {
            //订单
//            OrderPopupWindow
        } else if (tag == 2) {
            //动态

        }
    }


    private class MBaseAminatorListener extends BaseAminatorListener {

        private TextView toTextView;
        private int tag;

        public void setToTextView(TextView toTextView) {
            this.toTextView = toTextView;
        }

        public void setTag(int tag) {
            this.tag = tag;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            setSelectText(toTextView, tag);
        }

    }
}
