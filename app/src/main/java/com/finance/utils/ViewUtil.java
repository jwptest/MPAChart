package com.finance.utils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.finance.R;
import com.finance.model.ben.PurchaseViewEntity;
import com.finance.widget.roundview.RoundTextView;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

/**
 * view工具类
 */
public class ViewUtil {

    //设置控件状态
    public static void setViewVisibility(View view, int visibility) {
        if (view.getVisibility() != visibility)
            view.setVisibility(visibility);
    }

    //将点转换为实际坐标
    public static MPPointD getMPPointD(BarLineChartBase chart, IDataSet dataSet, float x, float y) {
        return chart.getTransformer(
                dataSet.getAxisDependency()).getPixelForValues(x, y);
    }

    /**
     * 获取购买成功后显示的布局
     *
     * @param context 上下文
     * @param text    购买的金额
     * @param isAdd   是否是买涨
     * @return 返回PurchaseViewEntity
     */
    public static PurchaseViewEntity getPurchase(Context context, String text, boolean isAdd) {
        PurchaseViewEntity viewEntity = new PurchaseViewEntity();
        getPurchase(context, viewEntity, text, isAdd);
        return viewEntity;
    }

    /**
     * 获取购买成功后显示的布局
     *
     * @param context 上下文
     * @param text    购买的金额
     * @param isAdd   是否是买涨
     * @return 返回PurchaseViewEntity
     */
    public static void getPurchase(Context context, PurchaseViewEntity viewEntity, String text, boolean isAdd) {
        if (viewEntity == null || context == null) return;
        View view = LayoutInflater.from(context).inflate(R.layout.home_layout_buyingpoint, null);
        View line = view.findViewById(R.id.line);//#20CF56跌，涨#EE4F3C
        RoundTextView tvBuyingMoney = view.findViewById(R.id.tvBuyingMoney);
        ImageView ivZD = view.findViewById(R.id.ivZD);
        tvBuyingMoney.setText(text);
        if (isAdd) {
            line.setBackgroundColor(Color.parseColor("#EE4F3C"));
            tvBuyingMoney.getDelegate().setBackgroundColor(Color.parseColor("#EE4F3C"));
            ivZD.setImageResource(R.drawable.rise_icon);
        } else {
            line.setBackgroundColor(Color.parseColor("#20CF56"));
//            tvBuyingMoney.setBackgroundResource(R.drawable.fall_bg);
            tvBuyingMoney.getDelegate().setBackgroundColor(Color.parseColor("#20CF56"));
            ivZD.setImageResource(R.drawable.fall_icon);
        }
        viewEntity.setRootView(view);
        viewEntity.setLine(line);
        viewEntity.setIvZD(ivZD);
        viewEntity.setTvBuyingMone(tvBuyingMoney);
    }


    public static void setBackground(Activity activity, final View view, int drawableId) {
        setBackground(Glide.with(activity), activity, view, drawableId);
    }

    public static void setBackground(Context context, final View view, int drawableId) {
        setBackground(Glide.with(context), context, view, drawableId);
    }

    private static void setBackground(RequestManager manager, Context context, final View view, int drawableId) {
        manager.load(drawableId)
                .asBitmap()
                .skipMemoryCache(true)
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                        view.setBackground(drawable);
                    }
                });
    }

    public static void dd(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        animator.setDuration(500);
        animator.start();
    }

    public static void setTitleLineLocation(final View line, final View referenceView) {
        referenceView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (referenceView.getHeight() <= 0) return;
                referenceView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int[] location = new int[2];
                referenceView.getLocationInWindow(location);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) line.getLayoutParams();
                params.leftMargin = location[0] + referenceView.getWidth();
                line.setLayoutParams(params);
            }
        });
    }

    public static LineDataSet createLineDataSet(Context context, ArrayList<Entry> entries) {
        LineDataSet set = new LineDataSet(entries, "");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.parseColor("#FFFFED79"));//走势图线条颜色
//        int color = Color.parseColor("#6CAEDC");
//        set.setCircleColor(Color.parseColor("#90ffed79"));
        set.setLineWidth(1f);
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);//不画点
        set.setDrawValues(false);//不画点的值
        set.setDrawFilled(true);
        set.setDrawIcons(false);//显示图标
        //set.setCircleRadius(4f);
//        set.setFillAlpha(80);
//        set.setHighLightColor(color);//高亮颜色
//        set.setValueTextColor(color);
        set.setHighlightEnabled(false);//不显示高亮
//        set.setDrawHighlightIndicators(true);
//        set.setDrawVerticalHighlightIndicator(true);
//        set.setFillAlpha(50);
        set.setValueTextSize(9f);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        set.setFormLineWidth(1f);
//        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//        set.enableDashedLine(10f, 5f, 0f);
//        set.setFormSize(15.f);
//        set.enableDashedHighlightLine(10f, 5f, 0f);
//        set.setCircleRadius(1f);
//        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        if (Utils.getSDKInt() >= 18) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_red);
            set.setFillDrawable(drawable);
        } else {
            //如果不支持渐变设置的颜色
            set.setFillColor(Color.parseColor("#50FFED79"));
        }
        return set;
    }

    //将布局添加到控件
    public void addPurchaseToView(ViewGroup viewGroup, PurchaseViewEntity entity, int index) {
        View rootView = entity.getRootView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, PurchaseViewEntity.viewHeight);
        entity.setDisplay(true);
        rootView.setLayoutParams(params);
        viewGroup.addView(rootView, index);
    }


    private ViewGroup.LayoutParams layoutParams;
    private RelativeLayout.LayoutParams layoutParams1;
    private ViewGroup.LayoutParams layoutParams2;
    private RelativeLayout.LayoutParams layoutParams3;
    private ViewGroup.LayoutParams layoutParams4;
    private RelativeLayout.LayoutParams layoutParams5;
    private View rootView;

    //刷新购买点的位置
    public void refreshPurchaseView(BarLineChartBase mChart, PurchaseViewEntity entity, IDataSet dataSet) {
        //获取布局的LayoutParams
        layoutParams1 = entity.getRootParams();
        rootView = entity.getRootView();
        if (layoutParams1 == null) {
            layoutParams = rootView.getLayoutParams();
            if (layoutParams == null) return;
            layoutParams1 = (RelativeLayout.LayoutParams) layoutParams;
            entity.setRootParams(layoutParams1);
        }
        //获取点的坐标
        MPPointD pointD = ViewUtil.getMPPointD(mChart, dataSet, entity.getxValue(), entity.getyValue());
        //设置布局Y的坐标
        layoutParams1.topMargin = (int) (pointD.y - PurchaseViewEntity.viewHeight / 2);

        //获取icon的LayoutParams
        layoutParams5 = entity.getIconParams();
        if (layoutParams5 == null) {
            layoutParams4 = entity.getIvZD().getLayoutParams();
            if (layoutParams4 == null) return;
            layoutParams5 = (RelativeLayout.LayoutParams) layoutParams4;
            entity.setIconParams(layoutParams5);
        }

        //设置金额显示控件的X坐标
        layoutParams5.leftMargin = (int) (pointD.x - PurchaseViewEntity.iconWidth / 2);

        //获取购买金额控件的LayoutParams
        layoutParams3 = entity.getMoneyParams();
        if (layoutParams3 == null) {
            layoutParams2 = entity.getTvBuyingMone().getLayoutParams();
            if (layoutParams2 == null) return;
            layoutParams3 = (RelativeLayout.LayoutParams) layoutParams2;
            entity.setMoneyParams(layoutParams3);
        }
        //设置金额显示控件的X坐标
        layoutParams3.leftMargin = layoutParams5.leftMargin - PurchaseViewEntity.leftWidth;

        layoutParams = null;
        layoutParams1 = null;
        layoutParams2 = null;
        layoutParams3 = null;
        layoutParams4 = null;
        layoutParams5 = null;
        rootView = null;
    }

}
