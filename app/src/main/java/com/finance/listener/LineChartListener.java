package com.finance.listener;

import android.app.Activity;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.finance.R;
import com.finance.event.DataRefreshEvent;
import com.finance.event.EventBus;
import com.finance.interfaces.IChartListener;
import com.finance.linechartview.BaseAxisValueFormatter;
import com.finance.model.ben.PurchaseViewEntity;
import com.finance.ui.main.PurchaseViewAnimation;
import com.finance.utils.ViewUtil;
import com.finance.widget.combinedchart.MCombinedChart;
import com.finance.widget.combinedchart.OnDrawCompletion;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointD;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/15.
 * MLineChart 事件处理
 */
public class LineChartListener implements IChartListener, OnDrawCompletion {

    private Activity mActivity;
    private MCombinedChart mChart;

    private ImageView ivIcon;
    private View vEndLine;//截止线
    private TextView tvEndLineDes;
    private ImageView ivEndLineIcon;
    private View vTransverseContrast;//横向对比线
    private TextView tvTransverseContrastDes;
    private View vSettlementLine;//结算线
    private TextView tvSettlementDes;
    private ImageView ivSettlementIcon;
    private ViewGroup mParent;//父布局

    private BaseAxisValueFormatter mRightAxisValueFormatter, mXAxisValueFormatter;


    private RelativeLayout.LayoutParams iconParams, endParams, endDesParams, endIconParams;//截止线
    private RelativeLayout.LayoutParams tranConParams, tranConDesParams;//横向对比线
    private RelativeLayout.LayoutParams settParams, settDesParams, settIconParams;//结算线

    //当前点的坐标
    private int currentX, currentY;
    //当前点的值
    private Entry currentEntry = new Entry();
    //开奖点和购买点
    private Entry openEntry, endEntry;
    //显示数据
    private IDataSet currentDataSet;
    //是否刷新子控件坐标
    private boolean isRefresh = false;

    //MLineChart控件的起始坐标
    private int startX, startY, chartWidth, chartHeight;
    private int iconWidth, iconHight;//当前点控件的宽高
    private int endDesWidth, endDesHight;
    private int tranConDesWidth, tranConDesHight;
    private int settWidth, settHight;
    private int settDesWidth, settDesHight;
    private int endIconWidth, endIconHight;
    private int settIconWidth, settIconHight;
    //当前购买的点的实体集合
    private ArrayList<PurchaseViewEntity> mPurchaseViewEntities;

    public LineChartListener(Activity activity, MCombinedChart lineChart) {
        this.mActivity = activity;
        this.mChart = lineChart;
        mParent = (ViewGroup) mChart.getParent();
    }

    @Override
    public LineChartListener initListener() {
        EventBus.register(this);//注册事件
        mChart.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mChart.getWidth() <= 0) return;
                //移除监听，防止重复执行
                mChart.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                chartWidth = mChart.getWidth();
                chartHeight = mChart.getHeight();
                Rect r = new Rect();
                mChart.getLocalVisibleRect(r);
                startX = r.left;
                startY = r.top;
                initViewHight();
            }
        });
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
//                Log.d("123", "X:" + h.getDrawX() + ",Y:" + h.getDrawY());
//                Log.d("123", "X:" + h.getX() + ",Y:" + h.getY());
//                Log.d("123", "X:" + h.getXPx() + ",Y:" + h.getYPx());
            }

            @Override
            public void onNothingSelected() {

            }
        });
        mChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                //开始手势
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                //手势结束
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
                //长按
            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
                //双击
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                //单击
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
                //快速滑动
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                //手势放缩
            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                //手势滑动
            }
        });
        mChart.setOnDrawCompletion(this);
        return this;
    }

    //设置当前点显示的图标控件
    public LineChartListener setIvIcon(ImageView ivIcon) {
        this.ivIcon = ivIcon;
        ViewUtil.setViewVisibility(ivIcon, View.GONE);
        //获取宽高
        ivIcon.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (LineChartListener.this.ivIcon.getWidth() <= 0) return;
                //移除监听，防止重复执行
                LineChartListener.this.ivIcon.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                iconWidth = LineChartListener.this.ivIcon.getWidth() / 2;
                iconHight = LineChartListener.this.ivIcon.getHeight() / 2;
            }
        });
        iconParams = (RelativeLayout.LayoutParams) ivIcon.getLayoutParams();
        return this;
    }

    //设置截止线控件
    public LineChartListener setEndLine(View vEndLine) {
        this.vEndLine = vEndLine;
        ViewUtil.setViewVisibility(vEndLine, View.GONE);
        endParams = (RelativeLayout.LayoutParams) vEndLine.getLayoutParams();
        return this;
    }

    //设置截止线描述控件
    public LineChartListener setTvEndLineDes(TextView tvEndLineDes) {
        this.tvEndLineDes = tvEndLineDes;
        ViewUtil.setViewVisibility(tvEndLineDes, View.GONE);
        tvEndLineDes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (LineChartListener.this.tvEndLineDes.getWidth() <= 0) return;
                LineChartListener.this.tvEndLineDes.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                endDesWidth = LineChartListener.this.tvEndLineDes.getWidth();
                endDesHight = LineChartListener.this.tvEndLineDes.getHeight();
            }
        });
        endDesParams = (RelativeLayout.LayoutParams) tvEndLineDes.getLayoutParams();
        return this;
    }

    //设置横向比较线控件
    public LineChartListener setTransverseContrast(View vTransverseContrast) {
        this.vTransverseContrast = vTransverseContrast;
        ViewUtil.setViewVisibility(vTransverseContrast, View.GONE);
        tranConParams = (RelativeLayout.LayoutParams) vTransverseContrast.getLayoutParams();
        return this;
    }

    //横向比较描述控件
    public LineChartListener setTvTransverseContrastDes(TextView tvTransverseContrastDes) {
        this.tvTransverseContrastDes = tvTransverseContrastDes;
        ViewUtil.setViewVisibility(tvTransverseContrastDes, View.GONE);
        tvTransverseContrastDes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (LineChartListener.this.tvTransverseContrastDes.getWidth() <= 0) return;
                LineChartListener.this.tvTransverseContrastDes.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                tranConDesWidth = LineChartListener.this.tvTransverseContrastDes.getWidth();
                tranConDesHight = LineChartListener.this.tvTransverseContrastDes.getHeight();
            }
        });
        tranConDesParams = (RelativeLayout.LayoutParams) tvTransverseContrastDes.getLayoutParams();
        return this;
    }

    public LineChartListener setSettlementLine(View vSettlementLine) {
        this.vSettlementLine = vSettlementLine;
        ViewUtil.setViewVisibility(vSettlementLine, View.GONE);
        settParams = (RelativeLayout.LayoutParams) vSettlementLine.getLayoutParams();
        return this;
    }

    public LineChartListener setTvSettlementDes(TextView tvSettlementDes) {
        this.tvSettlementDes = tvSettlementDes;
        ViewUtil.setViewVisibility(tvSettlementDes, View.GONE);
        tvSettlementDes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (LineChartListener.this.tvSettlementDes.getWidth() <= 0) return;
                LineChartListener.this.tvSettlementDes.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                settDesWidth = LineChartListener.this.tvSettlementDes.getWidth();
                settDesHight = LineChartListener.this.tvSettlementDes.getHeight();
            }
        });
        settDesParams = (RelativeLayout.LayoutParams) tvSettlementDes.getLayoutParams();
        return this;
    }

    public LineChartListener setIvEndLineIcon(ImageView ivEndLineIcon) {
        this.ivEndLineIcon = ivEndLineIcon;
        ViewUtil.setViewVisibility(ivEndLineIcon, View.GONE);
        this.ivEndLineIcon.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (LineChartListener.this.ivEndLineIcon.getWidth() <= 0)
                    return;
                LineChartListener.this.ivEndLineIcon.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                endIconWidth = LineChartListener.this.ivEndLineIcon.getWidth();
                endIconHight = LineChartListener.this.ivEndLineIcon.getHeight();
                initViewEndHight(endIconParams.bottomMargin);
            }
        });
        endIconParams = (RelativeLayout.LayoutParams) ivEndLineIcon.getLayoutParams();
        return this;
    }

    public LineChartListener setIvSettlementIcon(ImageView ivSettlementIcon) {
        this.ivSettlementIcon = ivSettlementIcon;
        ViewUtil.setViewVisibility(ivSettlementIcon, View.GONE);
        this.ivSettlementIcon.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (LineChartListener.this.ivSettlementIcon.getWidth() <= 0)
                    return;
                LineChartListener.this.ivSettlementIcon.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                settIconWidth = LineChartListener.this.ivSettlementIcon.getWidth();
                settIconHight = LineChartListener.this.ivSettlementIcon.getHeight();
                initViewSettHight(settIconParams.bottomMargin);
            }
        });
        settIconParams = (RelativeLayout.LayoutParams) ivSettlementIcon.getLayoutParams();
        return this;
    }

    public LineChartListener setRightAxisValueFormatter(BaseAxisValueFormatter rightAxisValueFormatter) {
        mRightAxisValueFormatter = rightAxisValueFormatter;
        return this;
    }

    public LineChartListener setXAxisValueFormatter(BaseAxisValueFormatter XAxisValueFormatter) {
        mXAxisValueFormatter = XAxisValueFormatter;
        return this;
    }

    //添加需要显示的购买点
    @Override
    public void addPurchaseView(PurchaseViewEntity entity) {
        if (mPurchaseViewEntities == null)
            mPurchaseViewEntities = new ArrayList<>(4);
        entity.setDisplay(false);
        mPurchaseViewEntities.add(entity);
        //将布局添加到父控件
        addPurchaseToView(mParent, entity);
        if (currentDataSet != null) {
            //刷新购买点的位置
            refreshPurchaseView(entity, currentDataSet);
            //刷新位置
            mParent.requestLayout();
            //启动动画
            PurchaseViewAnimation.getCompleteAnimation(entity.getTvBuyingMone()).start();
        }
    }

    //清空布局
    @Override
    public void clearPurchaseView() {
        if (mPurchaseViewEntities == null || mPurchaseViewEntities.isEmpty()) return;
        ViewGroup viewGroup = mParent;
        for (PurchaseViewEntity entity : mPurchaseViewEntities) {
            viewGroup.removeView(entity.getRootView());
        }
        mPurchaseViewEntities.clear();
    }

    //将所有布局添加到控件
    private void addPurchaseViews() {
        if (mPurchaseViewEntities == null || mPurchaseViewEntities.isEmpty()) return;
        ViewGroup viewGroup = mParent;
        for (PurchaseViewEntity entity : mPurchaseViewEntities) {
            addPurchaseToView(viewGroup, entity);
        }
    }

    //将布局添加到控件
    private void addPurchaseToView(ViewGroup viewGroup, PurchaseViewEntity entity) {
        if (entity.isDisplay()) {
            return;
        }
        View rootView = entity.getRootView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, PurchaseViewEntity.viewHeight);
        entity.setDisplay(true);
        rootView.setLayoutParams(params);
        viewGroup.addView(rootView);
    }


    //获取当前点的值
    @Override
    public Entry getCurrentEntry() {
        return currentEntry;
    }

    private void initView() {
        if (ivIcon != null) {
            ViewUtil.setViewVisibility(ivIcon, View.VISIBLE);
            //加载gif
            Glide.with(mActivity)
                    .load(R.drawable.new_one_icon)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(ivIcon);
        }
        if (vEndLine != null && tvEndLineDes != null && ivEndLineIcon != null) {//截止
            ViewUtil.setViewVisibility(vEndLine, View.VISIBLE);
            ViewUtil.setViewVisibility(tvEndLineDes, View.VISIBLE);
            ViewUtil.setViewVisibility(ivEndLineIcon, View.VISIBLE);
        }
        if (vTransverseContrast != null && tvTransverseContrastDes != null) {//横向对比
            ViewUtil.setViewVisibility(vTransverseContrast, View.VISIBLE);
            ViewUtil.setViewVisibility(tvTransverseContrastDes, View.VISIBLE);
        }
        if (vSettlementLine != null && tvSettlementDes != null || ivSettlementIcon != null) {//结算
            ViewUtil.setViewVisibility(vSettlementLine, View.VISIBLE);
            ViewUtil.setViewVisibility(tvSettlementDes, View.VISIBLE);
            ViewUtil.setViewVisibility(ivSettlementIcon, View.VISIBLE);
        }
    }

    /**
     * 隐藏图标
     */
    private void hideView() {
        if (ivIcon != null) {
            ViewUtil.setViewVisibility(ivIcon, View.GONE);
            ivIcon.setImageResource(0);
        }
        if (vEndLine != null && tvEndLineDes != null && ivEndLineIcon != null) {//截止
            ViewUtil.setViewVisibility(vEndLine, View.GONE);
            ViewUtil.setViewVisibility(tvEndLineDes, View.GONE);
            ViewUtil.setViewVisibility(ivEndLineIcon, View.GONE);
        }
        if (vTransverseContrast != null && tvTransverseContrastDes != null) {//横向对比
            ViewUtil.setViewVisibility(vTransverseContrast, View.GONE);
            ViewUtil.setViewVisibility(tvTransverseContrastDes, View.GONE);
        }
        if (vSettlementLine != null && tvSettlementDes != null || ivSettlementIcon != null) {//结算
            ViewUtil.setViewVisibility(vSettlementLine, View.GONE);
            ViewUtil.setViewVisibility(tvSettlementDes, View.GONE);
            ViewUtil.setViewVisibility(ivSettlementIcon, View.GONE);
        }
    }

    private void initViewHight() {
        //设置横向对比线的宽度
        tranConParams.width = chartWidth - 10;//横线对比线
        tranConParams.leftMargin = startX;
        vTransverseContrast.setLayoutParams(tranConParams);
    }

    //截止线
    private void initViewEndHight(int bottom) {
        if (endIconWidth <= 0) return;
        endParams.height = chartHeight - bottom - endIconWidth + 10;
        vEndLine.setLayoutParams(endParams);
    }

    //结算线
    private void initViewSettHight(int bottom) {
        if (settIconWidth <= 0) return;
        settParams.height = chartHeight - bottom - settIconWidth + 10;
        vSettlementLine.setLayoutParams(settParams);
    }

    @Subscribe
    public void onEvent(DataRefreshEvent event) {
        isRefresh = event.isRefresh();
        if (isRefresh) {//走势图动画执行完成，初始化控件
            initView();
        } else {
            hideView();
        }
    }

    @Override
    public void completion(Entry lastEntry, IDataSet dataSet) {
        if (!isRefresh) return;
        currentEntry = lastEntry;
        currentDataSet = dataSet;
        MPPointD pointD = ViewUtil.getMPPointD(mChart, dataSet, currentEntry.getX(), currentEntry.getY());
        currentX = (int) pointD.x;
        currentY = (int) pointD.y;
        //开奖
        refreshOpen(openEntry, dataSet);
        //截止购买
        refreshEnd(endEntry, dataSet);
        //其他线
        refreshLocation(currentEntry, dataSet);
        //刷新购买点的位置
        refreshPurchaseViews(dataSet);
        //刷新位置
        mParent.requestLayout();
    }

    //刷新开奖点
    private void refreshOpen(Entry openEntry, IDataSet dataSet) {
        if (settParams != null && settDesParams != null && openEntry != null) {
            MPPointD pointD = ViewUtil.getMPPointD(mChart, dataSet, openEntry.getX() + 100, openEntry.getY());
            //结算线
            settParams.leftMargin = (int) (pointD.x + settDesWidth / 2);
            //结算线描述
            settDesParams.leftMargin = settParams.leftMargin - settDesHight * 2 - settDesParams.rightMargin;
            //图标
            settIconParams.leftMargin = settParams.leftMargin - settIconWidth / 2;
        }
    }

    //刷新截止点
    private void refreshEnd(Entry endEntry, IDataSet dataSet) {
        if (endParams != null && endDesParams != null && endEntry != null) {
            MPPointD pointD = ViewUtil.getMPPointD(mChart, dataSet, endEntry.getX(), endEntry.getY());
            //截止线
            endParams.leftMargin = (int) (pointD.x + startX + endDesHight);
            //截止线描述
            endDesParams.leftMargin = endParams.leftMargin - endDesHight * 2 - endDesParams.rightMargin;
            //图标
            endIconParams.leftMargin = endParams.leftMargin - endIconWidth / 2;
        }
    }

    //设置当前点Icon坐标
    private void refreshLocation(Entry last, IDataSet dataSet) {
        //当前点
        if (iconParams != null) {
            iconParams.leftMargin = currentX + startX - iconWidth;
            iconParams.topMargin = currentY - iconHight;
        }
        if (tranConDesParams != null && tranConParams != null) {
            //横向描述线
            tranConParams.topMargin = currentY;
            //横向描线描述
            tranConDesParams.topMargin = currentY - tranConDesHight / 2;
        }
        if (mRightAxisValueFormatter != null) {
            //右边轴标签显示偏移值
            MPPointD pointD1 = ViewUtil.getMPPointD(mChart, dataSet, last.getX(), mRightAxisValueFormatter.getLasOne());
            MPPointD pointD2 = ViewUtil.getMPPointD(mChart, dataSet, last.getX(), mRightAxisValueFormatter.getLasTwo());
            mChart.getAxisRight().setYOffset((int) ((pointD1.y - pointD2.y) / 2));
        }
//        if (mXAxisValueFormatter != null) {
//            MPPointD pointD1 = ViewUtil.getMPPointD(mChart, dataSet, mXAxisValueFormatter.getLasOne(), last.getY());
//            MPPointD pointD2 = ViewUtil.getMPPointD(mChart, dataSet, mXAxisValueFormatter.getLasTwo(), last.getY());
//            mChart.getXAxis().setXOffset((int) ((pointD2.x - pointD1.x) / 2));
////            mChart.getXAxis().setXOffset((int) ((pointD2.x - pointD1.x) / 6));
//        }

    }

    //刷新所有购买点的位置
    private void refreshPurchaseViews(IDataSet dataSet) {
        if (mPurchaseViewEntities == null || mPurchaseViewEntities.isEmpty()) return;
        for (PurchaseViewEntity entity : mPurchaseViewEntities) {
            refreshPurchaseView(entity, dataSet);
        }
    }

    //刷新购买点的位置
    private void refreshPurchaseView(PurchaseViewEntity entity, IDataSet dataSet) {
        if (!entity.isDisplay())
            return;
        ViewGroup.LayoutParams layoutParams;
        RelativeLayout.LayoutParams layoutParams1;
        ViewGroup.LayoutParams layoutParams2;
        RelativeLayout.LayoutParams layoutParams3;
        ViewGroup.LayoutParams layoutParams4;
        RelativeLayout.LayoutParams layoutParams5;
        View rootView;
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
    }

    @Override
    public void onDestroy() {
        isRefresh = false;
        EventBus.unregister(this);
    }

}
