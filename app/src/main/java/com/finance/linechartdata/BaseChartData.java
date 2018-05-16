package com.finance.linechartdata;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.finance.R;
import com.finance.base.BaseAminatorListener;
import com.finance.common.Constants;
import com.finance.event.ChartDataUpdateEvent;
import com.finance.event.DataRefreshEvent;
import com.finance.event.EventBus;
import com.finance.interfaces.IChartData;
import com.finance.listener.EventDistribution;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.ui.main.MainContract;
import com.finance.utils.TimerUtil;
import com.finance.utils.ViewUtil;
import com.finance.widget.combinedchart.MCombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;

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
    protected ProductEntity topProductEntity;//上一次的产品信息
    protected long duration = 160;//动画执行时间
    protected int minsPacing = -1;//如果为-1折不介入绘制
    private ValueAnimator valueAnimator;//当前执行动画
    protected View animView;//执行加载动画的view
    private Animation mAnimation;//执行动画
    protected int dataMinCount = 0;

    public BaseChartData(Context context, MainContract.View view, MCombinedChart chart, MainContract.Presenter presenter) {
        this(context, view, chart, presenter, null);
    }

    public BaseChartData(Context context, MainContract.View view, MCombinedChart chart, MainContract.Presenter presenter, View animView) {
        this.mContext = context;
        this.mView = view;
        this.mChart = chart;
        this.mPresenter = presenter;
        this.mXAxis = mChart.getXAxis();
        this.mChartDatas = new ArrayList<>(100);
        this.dpPxRight = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_30);
        this.animView = animView;
        minsPacing = ViewConfiguration.get(context).getScaledTouchSlop();
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
    public void onResume(int type) {
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
        if (productEntity == this.productEntity) {
            if (issueEntity == this.issueEntity) {
                return;
            }
            this.issueEntity = issueEntity;
            //去掉部分历史数据
            removeBasicData();
        } else {
            this.productEntity = productEntity;
            this.issueEntity = issueEntity;
            if (topProductEntity == null) {
                topProductEntity = productEntity;
            }
            ViewUtil.setViewVisibility(animView, View.VISIBLE);
//            ViewUtil.setViewVisibility(animView, View.GONE);
            updateData();
        }
    }

    //刷新X轴显示的最大值
    protected void setAxisMaximum(int count) {
//        long trimL = getLengthTime() / Constants.ISSUEINTERVAL;
//        if (trimL <= 0) return;
//        //有效绘制的X轴条数
//        int chartWidth = mChartWidth - dpPxRight;
//        float itemWidth = chartWidth / trimL;
//        //不会绘制的X轴条数
//        int addItems = (int) (dpPxRight / itemWidth);
//        mXAxis.setAxisMaximum(trimL + addItems);

////        //绘制完成回调
//        float X = getEntry(issueEntity.getBonusTime()).getX();//数据总条数
//        float labelX = mChart.getFixedPosition();
//        float labelWidth = mChart.getLabelWidth();
//        float endX = labelX - labelWidth - dpPxRight;
//        float itemWidth = endX / X;
//        float addItem = (labelWidth + dpPxRight) / itemWidth;
//        mXAxis.setAxisMaximum(X + addItem);

        long startTimer = getStartTimer();
        long openTimer = TimerUtil.timerToLong(issueEntity.getBonusTime());
        long xCount = (openTimer - startTimer) / Constants.ISSUEINTERVAL;
        float labelX = mChart.getFixedPosition();//标签开始绘制坐标
        float labelWidth = mChart.getLabelWidth();//标签长度
        float endX = labelX - labelWidth - dpPxRight;
        float itemWidth = endX / xCount;
        float addItem = (labelWidth + dpPxRight) / itemWidth;
        mXAxis.setAxisMaximum(xCount + addItem);
    }

    @Override
    public void stopPurchase(boolean isOrder) {
        if (isOrder) return;
        mView.refreshIessue();//刷新期号
//        stopNetwork();
    }

    @Override
    public void openPrize(boolean isOrder) {
        mView.refreshIessue();//刷新期号
//        stopNetwork();
    }

    //刷新走势图
    protected void invalidateChart() {
        combinedData.notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

//    private int removeCount = 0;//已经删除的个数

    //启动删除数据动画
    protected void startRemoveDataAnimation() {
        final int maxIndex = mChartDatas.size() - 1;
        if (maxIndex < 0) return;
//        mChart.isStopDraw(false);
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        isAnimation = true;
//        removeCount = 0;
        //清除数据
        mChartDatas.clear();
//        mChart.isStopDraw(true, 0);
        invalidateChart();
        isAnimation = false;
        stopRemoveAnimation();

//        valueAnimator = staetValueAnimator(new ValueAnimator.AnimatorUpdateListener() {
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
        mChartDatas.clear();
//        mChart.isStopDraw(false, 0);
        if (maxIndex > 0) {
            mChartDatas.addAll(entitys);
            setAxisMaximum(maxIndex + 1);//刷新X周显示条数
        }
        dataMinCount = entitys.size();//保存基础数据条数
//        mChart.isStopDraw(true, dataMinCount);
        if (combinedData == null) {
            combinedData = new CombinedData();
            combinedData.setData(getLineData());
            mChart.setData(combinedData);
        } else {
            invalidateChart();
        }
        stopAddAnimation();
        isAnimation = false;

//        dataMinCount = entitys.size();//保存基础数据条数
//        mChart.isStopDraw(false);
//        if (valueAnimator != null) {
//            valueAnimator.cancel();
//        }
//        final int maxIndex = entitys.size() - 1;
//        if (maxIndex < 0) return;
//        isAnimation = true;
//        addCount = 1;
//        mChartDatas.clear();
//        mChartDatas.add(entitys.get(0));
//        setAxisMaximum(entitys.size());//刷新X轴显示条数
//        mChart.isStopDraw(true);
//        valueAnimator = staetValueAnimator(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int endIndex = (int) animation.getAnimatedValue();
//                for (int i = addCount; i <= endIndex; i++) {
//                    mChartDatas.add(entitys.get(i));
//                    addCount++;
//                }
//                invalidateChart();
//            }
//        }, true, 0, maxIndex);
    }

    private ValueAnimator staetValueAnimator(ValueAnimator.AnimatorUpdateListener listener, final boolean isAddAnimation, int... values) {
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
                if (isAddAnimation) {
                    stopAddAnimation();
                } else stopRemoveAnimation();
            }
        });
        valueAnimator.start();
        return valueAnimator;
    }

    private void removeBasicData() {//去掉部分基础数据
        if (mChartDatas == null || mChartDatas.isEmpty()) return;
        int size = mChartDatas.size();
        if (size == dataMinCount) return;
//        mChart.isStopDraw(false);
        isAnimation = true;
//        ArrayList<T> entitys;
//        if (dataMinCount >= size) {
//            entitys = new ArrayList<>(size);
//        } else {
//            int step = getDrawSetp();
//            if (step != -1) {
//                dataMinCount += dataMinCount % step;//保障步长一致
//            }
//            entitys = new ArrayList<>(mChartDatas.subList(size - dataMinCount, size - 1));
//        }
//        int index = 0;
//        for (T t : entitys) {
//            t.setX(index);
//            index++;
//        }
        ArrayList<T> entitys = getShowData();
        mChartDatas.clear();//清理了，如果走势图还在刷新就会报下标超界
        mChartDatas.addAll(entitys);
        dataMinCount = mChartDatas.size();
        setAxisMaximum(0);
//        mChart.isStopDraw(true, dataMinCount);
        isAnimation = false;
        invalidateChart();
        //走势图数据查询事件
        EventBus.post(new ChartDataUpdateEvent());
    }

    //添加动画执行完成
    protected void stopAddAnimation() {
        if (animView == null) return;
        if (mAnimation == null) {
            mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.animation_chart_complete);
            mAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animView.clearAnimation();
                    ViewUtil.setViewVisibility(animView, View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        animView.startAnimation(mAnimation);
    }

    //删除动画执行完成
    protected void stopRemoveAnimation() {

    }

    //初始化操作
    protected abstract void onInit();

    //刷新数据
    protected abstract void updateData();

    //
    protected abstract void updateStartTime(T t);

    protected abstract ArrayList<T> getShowData();

    //
    protected LineData getLineData() {
        return null;
    }

    protected abstract int getDrawSetp();

}
