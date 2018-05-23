package com.finance.listener;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.finance.R;
import com.finance.common.Constants;
import com.finance.event.ChartDataUpdateEvent;
import com.finance.event.DataRefreshEvent;
import com.finance.event.EventBus;
import com.finance.event.IndexEvent;
import com.finance.event.OpenPrizeDialogEvent;
import com.finance.event.UpdateIssueEvent;
import com.finance.interfaces.IChartData;
import com.finance.interfaces.IChartListener;
import com.finance.model.ben.IndexMarkEntity;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.model.ben.PurchaseViewEntity;
import com.finance.ui.main.MainContract;
import com.finance.ui.main.PurchaseViewAnimation;
import com.finance.utils.HandlerUtil;
import com.finance.utils.IndexFormatUtil;
import com.finance.utils.TimerUtil;
import com.finance.utils.ViewUtil;
import com.finance.widget.combinedchart.MCombinedChart;
import com.finance.widget.combinedchart.OnDrawCompletion;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointD;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/15.
 * MLineChart 事件处理
 */
public class LineChartListener implements IChartListener, OnDrawCompletion {

    private Activity mActivity;
    private MainContract.View mView;
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

    //    private BaseAxisValueFormatter mRightAxisValueFormatter;
//    private XAxisValueFormatter mXAxisValueFormatter;
    private RelativeLayout.LayoutParams iconParams, endParams, endDesParams, endIconParams;//截止线
    private RelativeLayout.LayoutParams tranConParams, tranConDesParams;//横向对比线
    private RelativeLayout.LayoutParams settParams, settDesParams, settIconParams;//结算线

    //当前点的坐标
    private int currentX, currentY;
    private float currentXAxis;
    //当前点的值
    private IndexMarkEntity currentEntry;
    //当前点的值
    private Entry endEntry, openEntry;
    private long endTimer, openTimer, oneEndTimer, oneOpenTimer;//截止时间和开奖时间
    private float endX, openX, oneEndX, oneOpenX;
    private ProductEntity mProductEntity;//当前产品
    //当前期号
    private IssueEntity oneIssueEntity, mCurrentIssueEntity;
    private int productId;//需要显示的产品id
    private String issueName;//需要显示的期号名称
    private boolean isOtherIssue;//是否判断其他条件
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
    private Animation mAnimator;//当前点的动画
    //当前购买的点的实体集合
    private ArrayList<PurchaseViewEntity> mPurchaseViewEntities;
    //临时用于存放购买的点的数组
    private ArrayList<PurchaseViewEntity> temporaryList;
    //需要移除的购买点
    private ArrayList<PurchaseViewEntity> removePurchaseViews;
    //获取结束点和开奖点
    private IChartData mIChartData;
    //格式化指数工具
    protected IndexFormatUtil format;
    //    //画布的X轴
//    private XAxis mXAxis;
    //刷新购买点坐标工具类
    private ViewUtil mViewUtil;
    private int childCount;
    //    private int dpPx10;
    private boolean isOrder = false;//是否有订单
    private long currentTimer;//当前点转换为时间
    private boolean isEnd = true;//是否判断截止
    private long endTimerIs, openTimerIs;//用于判断的开奖时间和结束时间
    private float endXIs, openXIs;//用于判断的开奖时间和结束时间


    public LineChartListener(Activity activity, ViewGroup rlPurchaseView, MainContract.View view, MCombinedChart lineChart) {
        this.mActivity = activity;
        this.mView = view;
        this.mChart = lineChart;
//        this.mXAxis = lineChart.getXAxis();
        mAnimator = AnimationUtils.loadAnimation(activity, R.anim.animation_chart_current_point);
        mParent = rlPurchaseView;
//        dpPx10 = activity.getResources().getDimensionPixelOffset(R.dimen.dp_15);
        mViewUtil = new ViewUtil();
        childCount = mParent.getChildCount() - 3;
        mPurchaseViewEntities = new ArrayList<>(4);
        temporaryList = new ArrayList<>(4);
        removePurchaseViews = new ArrayList<>(4);
        format = new IndexFormatUtil(Constants.INDEXDIGIT);
    }

    @Override
    public void onResume(String type) {
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
//        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
////                Log.d("123", "X:" + h.getDrawX() + ",Y:" + h.getDrawY());
////                Log.d("123", "X:" + h.getX() + ",Y:" + h.getY());
////                Log.d("123", "X:" + h.getXPx() + ",Y:" + h.getYPx());
//            }
//
//            @Override
//            public void onNothingSelected() {
//
//            }
//        });
//        mChart.setOnChartGestureListener(new OnChartGestureListener() {
//            @Override
//            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
//                //开始手势
//            }
//
//            @Override
//            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
//                //手势结束
//            }
//
//            @Override
//            public void onChartLongPressed(MotionEvent me) {
//                //长按
//            }
//
//            @Override
//            public void onChartDoubleTapped(MotionEvent me) {
//                //双击
//            }
//
//            @Override
//            public void onChartSingleTapped(MotionEvent me) {
//                //单击
//            }
//
//            @Override
//            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
//                //快速滑动
//            }
//
//            @Override
//            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
//                //手势放缩
//            }
//
//            @Override
//            public void onChartTranslate(MotionEvent me, float dX, float dY) {
//                //手势滑动
//            }
//        });
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

//        mAnimator = ValueAnimator.ofFloat(1f, 0.3f, 1f);
//        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                iconParams.
//            }
//        });
//        mAnimator.setDuration(600);
//        mAnimator.setRepeatCount(-1);
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
//                initViewEndHight(endIconParams.bottomMargin);
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
//                initViewSettHight(settIconParams.bottomMargin);
            }
        });
        settIconParams = (RelativeLayout.LayoutParams) ivSettlementIcon.getLayoutParams();
        return this;
    }

//    public LineChartListener setRightAxisValueFormatter(BaseAxisValueFormatter rightAxisValueFormatter) {
//        mRightAxisValueFormatter = rightAxisValueFormatter;
//        return this;
//    }
//
//    public LineChartListener setXAxisValueFormatter(XAxisValueFormatter XAxisValueFormatter) {
//        mXAxisValueFormatter = XAxisValueFormatter;
//        return this;
//    }

    @Override
    public void setIChartData(IChartData iChartData) {
        this.mIChartData = iChartData;
    }

    @Override
    public void setShowOrder(int productId, String issueName) {
        this.productId = productId;
        this.issueName = issueName;
        this.isOtherIssue = true;
    }

    @Override
    public void setOtherIssue(boolean isOtherIssue) {
        this.isOtherIssue = isOtherIssue;
    }

    @Override
    public ArrayList<PurchaseViewEntity> getPurchase(int productId, String issue) {
        ArrayList<PurchaseViewEntity> entities = new ArrayList<>(4);
        if (temporaryList == null || temporaryList.isEmpty())
            return entities;
        entities.addAll(temporaryList);
        temporaryList.clear();
        return entities;
    }

    //添加需要显示的购买点
    @Override
    public void addPurchaseView(PurchaseViewEntity entity) {
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

//    //清空布局
//    @Override
//    public void clearPurchaseView() {
//        if (mPurchaseViewEntities == null || mPurchaseViewEntities.isEmpty()) return;
//        ViewGroup viewGroup = mParent;
//        for (PurchaseViewEntity entity : mPurchaseViewEntities) {
//            viewGroup.removeView(entity.getRootView());
//        }
//        mPurchaseViewEntities.clear();
//    }

    @Override
    public void updateProductIssue(ProductEntity productEntity, IssueEntity mCurrentIssueEntity, IssueEntity oneIssueEntity) {
        mProductEntity = productEntity;
        this.mCurrentIssueEntity = mCurrentIssueEntity;
        this.oneIssueEntity = oneIssueEntity;
        format = new IndexFormatUtil(mProductEntity.getDigit());
    }

//    //将所有布局添加到控件
//    private void addPurchaseViews() {
//        if (mPurchaseViewEntities == null || mPurchaseViewEntities.isEmpty()) return;
//        ViewGroup viewGroup = mParent;
//        for (PurchaseViewEntity entity : mPurchaseViewEntities) {
//            addPurchaseToView(viewGroup, entity);
//        }
//    }

    //将布局添加到控件
    private void addPurchaseToView(ViewGroup viewGroup, PurchaseViewEntity entity) {
        if (entity.isDisplay()) {
            return;
        }
        mViewUtil.addPurchaseToView(viewGroup, entity, childCount + mPurchaseViewEntities.size() - 1);
    }

    @Override
    public void initView() {
        if (ivIcon != null) {
            ViewUtil.setViewVisibility(ivIcon, View.VISIBLE);
//            //加载gif
//            Glide.with(mActivity)
//                    .load(R.drawable.new_one_icon)
//                    .asGif()
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .into(ivIcon);
//            Glide.with(mActivity)
//                    .load(R.drawable.current_icon)
//                    .dontAnimate()
//                    .into(ivIcon);
            //启动动画
            ivIcon.startAnimation(mAnimator);
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
        if (vSettlementLine != null && tvSettlementDes != null && ivSettlementIcon != null) {//结算
            ViewUtil.setViewVisibility(vSettlementLine, View.VISIBLE);
            ViewUtil.setViewVisibility(tvSettlementDes, View.VISIBLE);
            ViewUtil.setViewVisibility(ivSettlementIcon, View.VISIBLE);
        }
    }

    /**
     * 隐藏图标
     */
    @Override
    public void hideView() {
        if (ivIcon != null) {
            ivIcon.clearAnimation();
            ViewUtil.setViewVisibility(ivIcon, View.GONE);
//            ivIcon.setImageResource(0);
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
        if (vSettlementLine != null && tvSettlementDes != null && ivSettlementIcon != null) {//结算
            ViewUtil.setViewVisibility(vSettlementLine, View.GONE);
            ViewUtil.setViewVisibility(tvSettlementDes, View.GONE);
            ViewUtil.setViewVisibility(ivSettlementIcon, View.GONE);
        }
    }

    //截止点撞线效果
    private void endLine() {
        vEndLine.setBackgroundColor(Color.parseColor("#F9F023"));
        HandlerUtil.runOnUiThreadDelay(new Runnable() {
            @Override
            public void run() {
                vEndLine.setBackgroundColor(Color.parseColor("#696458"));
            }
        }, 200);
    }

    //开奖点撞线效果
    private void openLine() {
        vSettlementLine.setBackgroundColor(Color.parseColor("#FF0000"));
        HandlerUtil.runOnUiThreadDelay(new Runnable() {
            @Override
            public void run() {
                vSettlementLine.setBackgroundColor(Color.parseColor("#D43625"));
            }
        }, 200);
    }

    private void initViewHight() {
        //设置横向对比线的宽度
        tranConParams.width = chartWidth - 10;//横线对比线
        tranConParams.leftMargin = startX;
        vTransverseContrast.setLayoutParams(tranConParams);
    }

//    //截止线
//    private void initViewEndHight(int bottom) {
//        if (endIconWidth <= 0) return;
//        endParams.height = chartHeight - bottom - endIconWidth + 10;
//        vEndLine.setLayoutParams(endParams);
//    }
//
//    //结算线
//    private void initViewSettHight(int bottom) {
//        if (settIconWidth <= 0) return;
//        settParams.height = chartHeight - bottom - settIconWidth + 10;
//        vSettlementLine.setLayoutParams(settParams);
//    }

    //隐藏购买点
    private void hidePurchaseView() {
        if (mPurchaseViewEntities == null || mPurchaseViewEntities.isEmpty()) return;
        for (PurchaseViewEntity entity : mPurchaseViewEntities) {
            //设置隐藏
            ViewUtil.setViewVisibility(entity.getRootView(), View.GONE);
        }
    }

    //控制购买点隐藏于显示
    private void updatePurchaseView() {
        if (mPurchaseViewEntities == null || mPurchaseViewEntities.isEmpty()) return;
        boolean isShow;
        for (PurchaseViewEntity entity : mPurchaseViewEntities) {
            if (mProductEntity.getProductId() != entity.getProductId()) {//不是当前产品的订单
                //设置不需要显示
                ViewUtil.setViewVisibility(entity.getRootView(), View.GONE);
                continue;
            }
            IndexMarkEntity indexMarkEntity = mView.getIndexMarkEntity(entity.getIndexMark());
            if (indexMarkEntity == null) {
                //设置不需要显示
                ViewUtil.setViewVisibility(entity.getRootView(), View.GONE);
                continue;
            }
            //判断是否显示订单
            isShow = (TextUtils.equals(mCurrentIssueEntity.getIssueName(), entity.getIssue())) ||
                    (isOtherIssue && productId == entity.getProductId() && TextUtils.equals(issueName, entity.getIssue()));
            if (isShow) {//是否是当前产品下购买的
                ViewUtil.setViewVisibility(entity.getRootView(), View.VISIBLE);//设置显示
                entity.setxValue(indexMarkEntity.getX());
                entity.setyValue(indexMarkEntity.getY());
                indexMarkEntity.setData(entity.getId());
                //启动动画
                PurchaseViewAnimation.getCompleteAnimation(entity.getTvBuyingMone()).start();
            } else {
                indexMarkEntity.setData(null);
                //设置不需要显示
                ViewUtil.setViewVisibility(entity.getRootView(), View.GONE);
            }
        }
    }

    @Subscribe
    public void onEvent(DataRefreshEvent event) {
        if (mActivity == null || mActivity.isFinishing()) return;
        isRefresh = event.isRefresh();
        if (isRefresh) {//走势图动画执行完成，初始化控件
            initView();
            updatePurchaseView();//刷新购买点的坐标点
        } else {
//            hideView();
            hidePurchaseView();//隐藏购买点
        }
    }

    @Subscribe
    public void onEvent(ChartDataUpdateEvent event) {
        if (mIChartData != null && mCurrentIssueEntity != null && oneIssueEntity != null) {
            endEntry = mIChartData.getEntry(mCurrentIssueEntity.getStopTime());
            openEntry = mIChartData.getEntry(mCurrentIssueEntity.getBonusTime());

            endTimer = TimerUtil.timerToLong(mCurrentIssueEntity.getStopTime());
            openTimer = TimerUtil.timerToLong(mCurrentIssueEntity.getBonusTime());

            oneEndTimer = TimerUtil.timerToLong(oneIssueEntity.getStopTime());
            oneOpenTimer = TimerUtil.timerToLong(oneIssueEntity.getBonusTime());
            if (endEntry != null) {
                endX = endEntry.getX();
            } else {
                endX = Integer.MAX_VALUE;
            }
            if (openEntry != null) {
                openX = openEntry.getX();
            } else {
                openX = Integer.MAX_VALUE;
            }
            Entry entry = mIChartData.getEntry(oneIssueEntity.getStopTime());
            if (entry != null) {
                oneEndX = entry.getX();
            } else {
                oneEndX = Integer.MAX_VALUE;
            }
            entry = mIChartData.getEntry(oneIssueEntity.getBonusTime());
            if (entry != null) {
                oneOpenX = entry.getX();
            } else {
                oneOpenX = Integer.MAX_VALUE;
            }
//            long startTimer = mIChartData.getStartTimer();
//            int timerLength = (int) ((openTimer - startTimer) / 60000);
//            mChart.getXAxis().setLabelCount(timerLength, true);
//            mXAxisValueFormatter.setStartTimer(startTimer);
            isEnd = true;
        }
        updatePurchaseView();//刷新购买点的坐标点
    }

    @Override
    public void completion(IndexMarkEntity lastEntry, IDataSet dataSet) {
        currentEntry = lastEntry;
        currentTimer = currentEntry.getTimeLong();//TimerUtil.timerToLong(lastEntry.getTime());
        currentXAxis = currentEntry.getX();
        Constants.SERVERCURRENTTIMER = currentTimer;//服务器当前时间
        if (!isRefresh) return;
        currentDataSet = dataSet;
        if (mView.isRefrshChartData()) return;
        MPPointD pointD = ViewUtil.getMPPointD(mChart, dataSet, currentXAxis, currentEntry.getY());
//        Log.d("123", "completion: " + (long) currentEntry.getX());
//        Log.d("123", "open: " + (long) openEntry.getX());
//        Log.d("123", "Xmax: " + mXAxis.getAxisMaximum());
        currentX = (int) pointD.x;
        currentY = (int) pointD.y;
        //刷新购买点的位置
        refreshPurchaseViews(dataSet);
        //开奖
        refreshOpen(openEntry, dataSet);
        //截止购买
        refreshEnd(endEntry, dataSet);
        //其他线
        refreshLocation(mIChartData.getCurrentEntry(), dataSet);
        //刷新位置
        mParent.requestLayout();
        onDraws();//调用绘制完成事件
    }

    //刷新开奖点
    private void refreshOpen(Entry openEntry, IDataSet dataSet) {
        if (settParams != null && settDesParams != null && openEntry != null) {
//            Log.d("123", "openX: " + openEntry.getX() + ",lastX:" + currentEntry.getX());
            MPPointD pointD = ViewUtil.getMPPointD(mChart, dataSet, openEntry.getX(), openEntry.getY());
            //结算线
            settParams.leftMargin = (int) (pointD.x + startX);//(int) (mChart.getFixedPosition() - mChart.getLabelWidth() - dpPx10);//(int) (pointD.x + settDesWidth / 2);
            //结算线描述
            settDesParams.leftMargin = settParams.leftMargin - settDesHight * 2 - settDesParams.rightMargin;
            //图标
            settIconParams.leftMargin = settParams.leftMargin - settIconWidth / 2;
//            if (endEntry != null && currentEntry.getX() > endEntry.getX() && mXAxis != null) {
//                MPPointD pointD = ViewUtil.getMPPointD(mChart, dataSet, currentEntry.getX(), currentEntry.getY());
//                if (pointD.x < settDesParams.leftMargin - pointD.x / currentEntry.getX()) {
//                    return;
//                }
//                if (openEntry.getX() - currentEntry.getX() <= 1) {
//                    return;
//                }
//                mChart.moveViewToX(openEntry.getX() - currentEntry.getX());
//            }
        }
    }

    //刷新截止点
    private void refreshEnd(Entry endEntry, IDataSet dataSet) {
        if (endParams != null && endDesParams != null && endEntry != null) {
            MPPointD pointD = ViewUtil.getMPPointD(mChart, dataSet, endEntry.getX(), endEntry.getY());
            //截止线
//            endParams.leftMargin = (int) (pointD.x + startX + endDesHight);
            endParams.leftMargin = (int) (pointD.x + startX);
            //截止线描述
            endDesParams.leftMargin = endParams.leftMargin - endDesHight * 2 - endDesParams.rightMargin;
//            //图标
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
            tvTransverseContrastDes.setText(format.format(last.getY()));
        }
//        if (mRightAxisValueFormatter != null) {
//            //右边轴标签显示偏移值
//            MPPointD pointD1 = ViewUtil.getMPPointD(mChart, dataSet, last.getX(), mRightAxisValueFormatter.getLasOne());
//            MPPointD pointD2 = ViewUtil.getMPPointD(mChart, dataSet, last.getX(), mRightAxisValueFormatter.getLasTwo());
//            mChart.getAxisRight().setYOffset((int) ((pointD1.y - pointD2.y) / 2));
//        }
//        if (mXAxisValueFormatter != null) {
//            MPPointD pointD1 = ViewUtil.getMPPointD(mChart, dataSet, mXAxisValueFormatter.getLasOne(), last.getY());
//            MPPointD pointD2 = ViewUtil.getMPPointD(mChart, dataSet, mXAxisValueFormatter.getLasTwo(), last.getY());
//            mChart.getXAxis().setXOffset((int) ((pointD2.x - pointD1.x) / 2));
////            mChart.getXAxis().setXOffset((int) ((pointD2.x - pointD1.x) / 6));
//        }
    }

    //刷新所有购买点的位置
    private void refreshPurchaseViews(IDataSet dataSet) {
        isOrder = false;
        if (mPurchaseViewEntities == null || mPurchaseViewEntities.isEmpty()) return;
//        boolean isIndex = false;
        boolean isOpenPrizeDialogEvent = false;
        if (!removePurchaseViews.isEmpty())
            removePurchaseViews.clear();
        PurchaseViewEntity entity0 = null;
        //移除到达开奖点的购买点
        for (PurchaseViewEntity entity : mPurchaseViewEntities) {
            if (entity.getOpenTimer() <= currentTimer) {
                entity.getRootView().clearAnimation();
                mParent.removeView(entity.getRootView());
                if (entity.getProductId() == mProductEntity.getProductId()) {
//                    isIndex = true;
                    temporaryList.add(entity.copy());
                    entity0 = entity;
                }
                removePurchaseViews.add(entity);
                isOpenPrizeDialogEvent = true;
            }
        }
        //移除到期的购买点
        if (!removePurchaseViews.isEmpty()) {
            for (PurchaseViewEntity entity : removePurchaseViews) {
                mPurchaseViewEntities.remove(entity);
            }
        }
        //刷新购买点的位置
        for (PurchaseViewEntity entity : mPurchaseViewEntities) {
            refreshPurchaseView(entity, dataSet);
        }
        if (entity0 != null) {//判断当前产品是否到有到达开奖点的
            EventBus.post(new IndexEvent(entity0.getProductId(), mProductEntity.getProductName(), entity0.getIssue(), TimerUtil.timerFormatStr(entity0.getOpenTimer()), mProductEntity.getDigit()));
        } else if (isOpenPrizeDialogEvent) {
            //有达到开奖时间的订单，但不是当前产品
            EventBus.post(new OpenPrizeDialogEvent(false));
        }
    }

    //刷新购买点的位置
    private void refreshPurchaseView(PurchaseViewEntity entity, IDataSet dataSet) {
        if (entity.getRootView().getVisibility() != View.VISIBLE) return;//不需要显示在布局上
        if (!entity.isDisplay()) return;//已经显示在布局上
        isOrder = true;
        mViewUtil.refreshPurchaseView(mChart, entity, dataSet);
    }

    //绘制完成执行
    private void onDraws() {
        //绘制完成事件
        EventDistribution.getInstance().onDraw(currentEntry, isOrder);
        if (isOrder) {
            endTimerIs = endTimer;
            openTimerIs = openTimer;
            endXIs = endX;
            openXIs = openX;
        } else {
            endTimerIs = oneEndTimer;
            openTimerIs = oneOpenTimer;
            endXIs = oneEndX;
            openXIs = oneOpenX;
        }
        //截止购买和开奖
        if (openEntry != null && endEntry != null) {
            if (isEnd && (endXIs == Integer.MAX_VALUE ? currentTimer - endTimerIs >= 0 : currentXAxis >= endXIs)) {
                isEnd = false;
                EventDistribution.getInstance().purchase(false, isOrder);//截止购买
                if (currentTimer - endTimer >= 0) {
                    EventBus.post(new UpdateIssueEvent(true));
                    endLine();
                }
                if (isOrder) {
//                    long end = TimerUtil.timerToLong(currentEntry.getTime());
                    long open = TimerUtil.timerToLong(mCurrentIssueEntity.getBonusTime());
                    if (open - currentTimer > 0)//开始倒计时
                        OpenCountDown.getInstance().startCountDown(open - currentTimer);
                }
            } else if (openXIs == Integer.MAX_VALUE ? currentTimer - openTimerIs >= 0 : currentXAxis >= openXIs) {
                isEnd = true;
                EventDistribution.getInstance().purchase(true, isOrder);//开奖
                openLine();
                EventBus.post(new UpdateIssueEvent(false));
                if (isOrder) {
                    OpenCountDown.getInstance().stopCountDown();//停止倒计时
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        isRefresh = false;
        EventBus.unregister(this);
    }

}
