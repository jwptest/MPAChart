package com.finance.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.finance.R;
import com.finance.interfaces.IChartData;
import com.finance.model.ben.IndexMarkEntity;
import com.finance.model.ben.PurchaseViewEntity;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointD;

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
        View view = LayoutInflater.from(context).inflate(R.layout.home_layout_buyingpoint, null);
        View line = view.findViewById(R.id.line);
        TextView tvBuyingMoney = view.findViewById(R.id.tvBuyingMoney);
        ImageView ivZD = view.findViewById(R.id.ivZD);
        tvBuyingMoney.setText(text);
        if (isAdd) {
            line.setBackgroundColor(Color.parseColor("#EE4F3C"));
            tvBuyingMoney.setBackgroundResource(R.drawable.rise_bg);
            ivZD.setImageResource(R.drawable.rise_icon);
        } else {
            line.setBackgroundColor(Color.parseColor("#20CF56"));
            tvBuyingMoney.setBackgroundResource(R.drawable.fall_bg);
            ivZD.setImageResource(R.drawable.fall_icon);
        }
        PurchaseViewEntity viewEntity = new PurchaseViewEntity();
        viewEntity.setRootView(view);
        viewEntity.setLine(line);
        viewEntity.setIvZD(ivZD);
        viewEntity.setTvBuyingMone(tvBuyingMoney);
        return viewEntity;
    }

    public static void dd(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        animator.setDuration(500);
        animator.start();
    }

}
