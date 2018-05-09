package com.finance.linechartdata;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.ViewTreeObserver;

import com.finance.R;
import com.finance.base.BaseAminatorListener;
import com.finance.common.Constants;
import com.finance.event.DataRefreshEvent;
import com.finance.event.EventBus;
import com.finance.interfaces.IChartData;
import com.finance.listener.EventDistribution;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.ui.main.MainContract;
import com.finance.widget.combinedchart.MCombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * 数据处理基类类
 */
public abstract class BaseChartData<T extends Entry> implements IChartData, EventDistribution.IPurchase {

    protected MCombinedChart mChart;
    protected Context mContext;
    protected MainContract.View mView;
    protected MainContract.Presenter mPresenter;
    protected XAxis mXAxis;
    protected CombinedData combinedData;
    protected ArrayList<T> mChartDatas;//推送的指数数据
    protected int dpPxRight;//开奖线距离右边标签间距
    protected int mChartWidth;
    protected boolean isAnimation = false;//是否在执行动画
    protected ProductEntity productEntity;//当前产品
    protected IssueEntity issueEntity;//当前期号
    protected long duration = 2000;//动画执行时间

    public BaseChartData(Context context, MainContract.View view, MCombinedChart chart, MainContract.Presenter presenter) {
        this.mContext = context;
        this.mView = view;
        this.mChart = chart;
        this.mPresenter = presenter;
        this.mXAxis = mChart.getXAxis();
        this.mChartDatas = new ArrayList<>(100);
        this.dpPxRight = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_20);
        this.mChart.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mChartWidth = mChart.getWidth();
                if (mChartWidth <= 0) return;
                mChart.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        combinedData = new CombinedData();
        onInit();
    }

    @Override
    public void onResume(String type) {
        EventDistribution.getInstance().addPurchase(this);
    }

    @Override
    public void onDestroy() {
        EventBus.post(new DataRefreshEvent(false));
        EventDistribution.getInstance().removePurchase(this);
    }

    @Override
    public void updateIssue(ProductEntity productEntity, IssueEntity issueEntity) {
        if (productEntity == null || issueEntity == null) return;
        if (this.issueEntity != null && this.issueEntity == issueEntity) {
            return;
        }
        this.productEntity = productEntity;
        this.issueEntity = issueEntity;
        updateData();
    }

    //刷新X轴显示的最大值
    protected void setAxisMaximum() {
        long trimL = getLengthTime() / Constants.ISSUEINTERVAL;
        if (trimL <= 0) return;
        //有效绘制的X轴条数
        int chartWidth = mChartWidth - dpPxRight;
        float itemWidth = chartWidth / trimL;
        //不会绘制的X轴条数
        int addItems = (int) (dpPxRight / itemWidth);
        mXAxis.setAxisMaximum(trimL + addItems);
    }

    @Override
    public void stopPurchase(boolean isOrder) {
        if (isOrder) return;
        mView.refreshIessue();//刷新期号
    }

    @Override
    public void openPrize(boolean isOrder) {
        mView.refreshIessue();//刷新期号
    }

    //刷新走势图
    protected void invalidateChart() {
        combinedData.notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    private int removeCount = 0;//已经删除的个数

    //启动删除数据动画
    protected void startRemoveDataAnimation() {
        final int maxIndex = mChartDatas.size() - 1;
        if (maxIndex < 0) return;
        isAnimation = true;
        removeCount = 0;

        //清除数据
        mChartDatas.clear();
        invalidateChart();
        isAnimation = false;
        stopRemoveAnimation();

//        staetValueAnimator(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int endIndex = (int) animation.getAnimatedValue();
//                int size = 0;
//                for (int i = 0; i <= endIndex - removeCount; i++) {
//                    size = mChartDatas.size() - 1;
//                    if (size >= 0)
//                        mChartDatas.remove(size);
//                    else break;
//                    removeCount++;
//                }
//                invalidateChart();
//            }
//        }, false, 0, maxIndex);
    }

    private int addCount = 0;//已经添加的数据条数

    //启动添加数据动画
    protected void startAddDataAnimation(ArrayList<T> entitys) {
        final int maxIndex = entitys.size() - 1;
        if (maxIndex < 0) return;
        setAxisMaximum();//刷新X周显示条数
        isAnimation = true;
        addCount = 0;
        mChartDatas.clear();
        staetValueAnimator(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int endIndex = (int) animation.getAnimatedValue();
                for (int i = addCount; i <= endIndex; i++) {
                    mChartDatas.add(entitys.get(i));
                    addCount++;
                }
                invalidateChart();
            }
        }, true, 0, maxIndex);
    }

    private void staetValueAnimator(ValueAnimator.AnimatorUpdateListener listener, final boolean isAddAnimation, int... values) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(values);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(listener);
        valueAnimator.addListener(new BaseAminatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = false;
                if (isAddAnimation) stopAddAnimation();
                else stopRemoveAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnimation = false;
                if (isAddAnimation) stopAddAnimation();
                else stopRemoveAnimation();
            }
        });
        valueAnimator.start();
    }

    //初始化操作
    protected abstract void onInit();

    //刷新数据
    protected abstract void updateData();

    //添加动画执行完成
    protected abstract void stopAddAnimation();

    //删除动画执行完成
    protected abstract void stopRemoveAnimation();

    //获取当前数据的总时长
    protected abstract long getLengthTime();

}
