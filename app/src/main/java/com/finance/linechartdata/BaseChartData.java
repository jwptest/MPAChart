package com.finance.linechartdata;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;

import com.finance.R;
import com.finance.common.Constants;
import com.finance.event.ChartDataUpdateEvent;
import com.finance.event.DataRefreshEvent;
import com.finance.event.EventBus;
import com.finance.interfaces.IChartData;
import com.finance.linechartview.XAxisValueFormatter;
import com.finance.listener.EventDistribution;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.model.ben.XEntity;
import com.finance.ui.main.MainContract;
import com.finance.utils.TimerUtil;
import com.finance.widget.combinedchart.MCombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 数据处理基类类
 */
public abstract class BaseChartData<T extends Entry> implements IChartData, EventDistribution.IPurchase {

    protected MCombinedChart mChart;
    protected Context mContext;
    protected MainContract.View mView;
    protected MainContract.Presenter mPresenter;
    //    protected XAxis mXAxis;
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
    protected int currentChartType;
    //    protected View animView;//执行加载动画的view
//    private Animation mAnimation;//执行动画
    protected int dataMinCount = 0;

    public BaseChartData(Context context, MainContract.View view, MCombinedChart chart, MainContract.Presenter presenter) {
        this(context, view, chart, presenter, null);
    }

    public BaseChartData(Context context, MainContract.View view, MCombinedChart chart, MainContract.Presenter presenter, View animView) {
        this.mContext = context;
        this.mView = view;
        this.mChart = chart;
        this.mPresenter = presenter;
//        this.mXAxis = mChart.getXAxis();
        this.mChartDatas = new ArrayList<>(100);
        this.dpPxRight = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_20);
//        this.animView = animView;
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
        EventDistribution.getInstance().addPurchase(this);
    }

    @Override
    public void onResume(int type) {
        currentChartType = type;
    }

    @Override
    public void onDestroy(int chartType) {
        if (chartType == Constants.CHART_LINEFILL || chartType == Constants.CHART_LINE) {
            return;
        }
        EventBus.post(new DataRefreshEvent(false));
        unSubscribeProduct();//取消订阅
    }

    @Override
    public void stopPurchase(boolean isOrder) {
        if (isOrder) {
            return;
        }
        isAnimation = true;
        mView.refreshIessue();//刷新期号
    }

    @Override
    public void openPrize(boolean isOrder) {
        isAnimation = true;
//        if (this.productEntity != null) {
//            //取消订阅产品
//            mPresenter.unSubscribeProduct(this.productEntity.getProductId());
//        }
    }

    @Override
    public void updateIssue(ProductEntity productEntity, IssueEntity issueEntity) {
        if (productEntity == null || issueEntity == null) return;
        if (productEntity == this.productEntity) {
            if (issueEntity == this.issueEntity) {
                return;
            }
            this.issueEntity = issueEntity;
            isAnimation = true;
            //去掉部分历史数据
            removeBasicData();
        } else {
            isAnimation = true;
            unSubscribeProduct();
            this.productEntity = productEntity;
            this.issueEntity = issueEntity;
            if (topProductEntity == null) {
                topProductEntity = productEntity;
            }
            updateData();
        }
    }

    //取消订阅
    private void unSubscribeProduct() {
        if (this.productEntity != null) {
            //取消订阅产品
            mPresenter.unSubscribeProduct(this.productEntity.getProductId());
        }
    }

    //刷新X轴显示的最大值
    protected void setAxisMaximum() {
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

        XAxis mXAxis = mChart.getXAxis();

        mXAxis.setAxisMaximum(xCount + addItem);

        //标签计算
        long totalTimer = startTimer + ((int) mXAxis.getAxisMaximum() * 500);
        int lableCount = (int) (totalTimer - startTimer) / 1000 / 60;
        mXAxis.setLabelCount(lableCount + 1,true);//设置x轴显示的标签个数
        //计算X轴标签显示值
        IAxisValueFormatter f = mXAxis.getValueFormatter();
        if (!(f instanceof XAxisValueFormatter)) {
            return;
        }
        int hour = (int) (openTimer - startTimer) / 1000 / 60;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(openTimer);
        ArrayList<XEntity> xentitys = new ArrayList<>();
        for (int i = 0; i <= hour; i++) {
            XEntity en = new XEntity();
            en.setMaxIndex(getXIndex(openTimer - i * 60000));
            en.setMinIndex(getXIndex(openTimer - (i + 1) * 60000));
            en.setValue(TimerUtil.getHourMin(calendar));
            calendar.add(Calendar.MINUTE, -1);
            xentitys.add(en);
        }

        hour = (int) (totalTimer - openTimer) / 1000 / 60;
        calendar.setTimeInMillis(openTimer);
        for (int i = 0; i <= hour; i++) {
            calendar.add(Calendar.MINUTE, 1);
            XEntity en = new XEntity();
            en.setMinIndex(getXIndex(openTimer + i * 60000));
            en.setMaxIndex(getXIndex(openTimer + (i + 1) * 60000));
            en.setValue(TimerUtil.getHourMin(calendar));
            xentitys.add(en);
        }
        ((XAxisValueFormatter) f).setStartTimer(xentitys);
    }

    //刷新走势图
    protected void invalidateChart() {
        combinedData.notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    //启动添加数据动画
    protected void startAddDataAnimation(ArrayList<T> entitys) {
        final int maxIndex = entitys.size() - 1;
        mChartDatas.clear();
        if (maxIndex <= 0) {
            invalidateChart();
            isAnimation = false;
            return;
        }
        mChartDatas.addAll(entitys);
        combinedData.setData(getLineData());
        mChart.setData(combinedData);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
        setAxisMaximum();//刷新X周显示条数
        stopAddAnimation();
        isAnimation = false;
    }

    private void removeBasicData() {//去掉部分基础数据
        if (mChartDatas == null || mChartDatas.isEmpty()) {
            isAnimation = false;
            return;
        }
        int size = mChartDatas.size();
        if (size == dataMinCount) {
            isAnimation = false;
            return;
        }
        ArrayList<T> entitys = getShowData();
        mChartDatas.clear();//清理了，如果走势图还在刷新就会报下标超界
        mChartDatas.addAll(entitys);
        dataMinCount = mChartDatas.size();
        setAxisMaximum();
        invalidateChart();
        //走势图数据查询事件
        EventBus.post(new ChartDataUpdateEvent());
        isAnimation = false;
    }

    //添加动画执行完成
    protected void stopAddAnimation() {
    }

    //初始化操作
    protected abstract void onInit();

    //刷新数据
    protected abstract void updateData();

    //
//    protected abstract void updateStartTime(T t);

    protected abstract ArrayList<T> getShowData();

    //
    protected LineData getLineData() {
        return null;
    }

    protected abstract int getXIndex(long timer);

}
