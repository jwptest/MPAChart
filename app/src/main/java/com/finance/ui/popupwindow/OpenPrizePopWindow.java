package com.finance.ui.popupwindow;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.finance.R;
import com.finance.event.EventBus;
import com.finance.event.OpenPrizeDialogEvent;
import com.finance.linechartview.BaseAxisValueFormatter;
import com.finance.linechartview.LineChartSetting2;
import com.finance.model.ben.HistoryIssueEntity;
import com.finance.model.ben.IndexMarkEntity;
import com.finance.model.ben.OpenOrderEntity;
import com.finance.model.ben.PurchaseViewEntity;
import com.finance.ui.main.MainContract;
import com.finance.ui.main.PurchaseViewAnimation;
import com.finance.utils.HandlerUtil;
import com.finance.utils.IndexFormatUtil;
import com.finance.utils.IndexUtil;
import com.finance.utils.TimerUtil;
import com.finance.utils.ViewUtil;
import com.finance.widget.animation.BaseAnimatorSet;
import com.finance.widget.combinedchart.MLineChart2;
import com.finance.widget.combinedchart.OnDrawCompletion;
import com.finance.widget.commonadapter.RecyclerAdapter;
import com.finance.widget.commonadapter.viewholders.RecyclerViewHolder;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointD;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开奖对话框
 */
public class OpenPrizePopWindow extends BasePopupWindow implements OnDrawCompletion {

    //
    @BindView(R.id.llContent)
    View llContent;
    @BindView(R.id.tvTitleType)
    TextView mTvTitleType;
    @BindView(R.id.tvTitleMoney)
    TextView mTvTitleMoney;
    @BindView(R.id.tvTitleProfit)
    TextView mTvTitleProfit;
    @BindView(R.id.tvTitleTypeValue)
    TextView mTvTitleTypeValue;
    @BindView(R.id.tvTitleMoneyValue)
    TextView mTvTitleMoneyValue;
    @BindView(R.id.tvTitleProfitValue)
    TextView mTvTitleProfitValue;
    @BindView(R.id.lineChart)
    MLineChart2 mLineChart;
    @BindView(R.id.rvList)
    RecyclerView mRvList;
    @BindView(R.id.rlChart)
    ViewGroup rlChart;
    @BindView(R.id.vTransverseContrast)
    View vTransverseContrast;
    @BindView(R.id.tvTransverseContrastDes)
    TextView tvTransverseContrastDes;
    @BindView(R.id.vSettlementLine)
    View vSettlementLine;
    @BindView(R.id.ivSettlementIcon)
    ImageView ivSettlementIcon;


    private Activity mContext;
    private MainContract.View mView;
    //    private IChartSetting mChartSetting;
    private HistoryIssueEntity mHistoryIssueEntity;
    private IndexMarkEntity openIndex;
    private ArrayList<IndexMarkEntity> mMarkEntities;//显示指数
    private ArrayList<OpenOrderEntity> mOrderEntities;//开奖订单列表
    private ArrayList<PurchaseViewEntity> viewEntities;//购买点
    private String productName;//产品名称
    private String orderTMoney;//订单总金额
    private String profitTMoney;//收益总金额
    private int productId;
    private String issue;
    private ViewUtil mViewUtil;
    private int digit;

    private RelativeLayout.LayoutParams tranConParams, tranConDesParams;//横向对比线
    private RelativeLayout.LayoutParams settParams, settIconParams;//结算线
    private int tranConDesHight;
    private int settIconHight;
    private boolean isAddPurchase = false;//是否可以返回关闭对话框
    private IndexFormatUtil mIndexFormatUtil;

    /**
     * 开奖对话框
     *
     * @param view      调用接口
     * @param entity    历史期数
     * @param openIndex 开奖指数
     */
    public OpenPrizePopWindow(@NonNull Activity context, MainContract.View view,
                              HistoryIssueEntity entity, IndexMarkEntity openIndex,
                              int productId, String issue, String productName, int digit, int screenWidth, int screenHeight) {
        super(context, (int) (screenWidth * 0.6f), (int) (screenHeight * 0.6f), (int) (screenWidth * 0.2f), (int) (screenHeight * 0.2));
        this.mContext = context;
//        this.mChartSetting = mChartSetting;
        this.mView = view;
        this.mHistoryIssueEntity = entity;
        this.openIndex = openIndex;
        this.productId = productId;
        this.issue = issue;
        this.productName = productName;
        this.digit = digit;
        this.mViewUtil = new ViewUtil();
        setTouchable(true);
        setOutsideTouchable(false);   //设置外部点击关闭ppw窗口
        ViewUtil.setBackground(mContext, llContent, R.drawable.dialog_open_prize_bg);
        mIndexFormatUtil = new IndexFormatUtil(digit);
        tvTransverseContrastDes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tranConDesHight = tvTransverseContrastDes.getHeight();
                if (tranConDesHight <= 0) return;
                tvTransverseContrastDes.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        ivSettlementIcon.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                settIconHight = ivSettlementIcon.getHeight();
                if (settIconHight <= 0) return;
                ivSettlementIcon.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        //界面显示出来再处理数据
//        mLineChart.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if (mLineChart.getWidth() <= 0) return;
//                mLineChart.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                initData(mHistoryIssueEntity, openIndex);
//            }
//        });
        tranConParams = (RelativeLayout.LayoutParams) vTransverseContrast.getLayoutParams();
        tranConDesParams = (RelativeLayout.LayoutParams) tvTransverseContrastDes.getLayoutParams();

        settParams = (RelativeLayout.LayoutParams) vSettlementLine.getLayoutParams();
        settIconParams = (RelativeLayout.LayoutParams) ivSettlementIcon.getLayoutParams();

        EventBus.post(new OpenPrizeDialogEvent(true));//打开开奖对话框事件
        initData(mHistoryIssueEntity, openIndex.getY(), mIndexFormatUtil.format(openIndex.getY()));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_open_prize;
    }

    @Override
    protected int getBackColor() {
        return Color.parseColor("#A0000000");
    }

    @Override
    protected boolean isEvent() {
        return false;
    }

    private void initData(final HistoryIssueEntity entity, final float openIndex, final String openIndexStr) {
        new Thread() {
            @Override
            public void run() {
                IndexUtil indexUtil = new IndexUtil();
                ArrayList<IndexMarkEntity> entities = indexUtil.parseExponentially2(0, entity.getIndexMarks(), digit);
                if (entities == null) return;
                //设置下标
                for (int i = 0, size = entities.size(); i < size; i++) {
                    entities.get(i).setX(i);
                }
                ArrayList<PurchaseViewEntity> viewEntities = mView.getPurchase(productId, issue);
                ArrayList<OpenOrderEntity> mOrderEntities = new ArrayList<OpenOrderEntity>(4);
                int size = entities.size() - 1;
                int orderTMoney = 0, profitTMoney = 0;

                for (PurchaseViewEntity viewEntity : viewEntities) {
                    IndexMarkEntity indexMarkEntity = null;
                    IndexMarkEntity indexMarkEntity1 = indexUtil.parseExponentially(0, viewEntity.getIndexMark(), digit);
                    for (int i = size; i >= 0; i--) {
                        indexMarkEntity = entities.get(i);
                        if (TextUtils.equals(viewEntity.getIndexMark(), indexMarkEntity.getId())) {
                            indexMarkEntity.setData(indexMarkEntity.getId());
                            break;
                        } else if (indexMarkEntity1 != null && indexMarkEntity1.getTimeLong() >= indexMarkEntity.getTimeLong()) {
                            indexMarkEntity.setData(indexMarkEntity.getId());
                            break;
                        }
                    }
                    if (indexMarkEntity != null && indexMarkEntity.getData() != null) {
                        //列表数据
                        OpenOrderEntity orderEntity = new OpenOrderEntity();
                        String createTime = viewEntity.getCreateTime();
                        orderEntity.setTimerStr(TimerUtil.getTimer(createTime));//时间
                        orderEntity.setDateStr(TimerUtil.getDate(createTime));
                        orderEntity.setPurchaseIndex(mIndexFormatUtil.format(indexMarkEntity.getY()));//购买指数
                        orderEntity.setOpenIndex(openIndexStr);//开奖指数
                        orderEntity.setIcon(viewEntity.isResult() ? R.drawable.add_icon_item : R.drawable.fall_icon_item);
                        orderTMoney += viewEntity.getMoney();//订单金额
                        orderEntity.setOrderMoney("¥" + viewEntity.getMoney());//购买金额
                        int profit = 0;
                        if (viewEntity.isResult() && openIndex > indexMarkEntity.getY()) {//猜涨
                            profit = viewEntity.getMoney() * (viewEntity.getExpects() + 100) / 100;
                        } else if (!viewEntity.isResult() && openIndex < indexMarkEntity.getY()) {//猜跌
                            profit = viewEntity.getMoney() * (viewEntity.getExpects() + 100) / 100;
                        }
                        orderEntity.setProfit("¥" + profit);//收益金额
                        profitTMoney += profit;
                        mOrderEntities.add(orderEntity);
                        //购买点数据
                        viewEntity.setxValue(indexMarkEntity.getX());
                        viewEntity.setyValue(indexMarkEntity.getY());
                    }
                }
                if (!OpenPrizePopWindow.this.isShowing()) return;
                OpenPrizePopWindow.this.orderTMoney = "¥" + orderTMoney;
                OpenPrizePopWindow.this.profitTMoney = "¥" + profitTMoney;
                OpenPrizePopWindow.this.mOrderEntities = mOrderEntities;
                OpenPrizePopWindow.this.mMarkEntities = entities;
                OpenPrizePopWindow.this.viewEntities = viewEntities;
                HandlerUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initView();
                    }
                });
            }
        }.start();
    }

//    @OnClick(R.id.tvTitleType)
//    public void onClickListener() {
//        initData(mHistoryIssueEntity, openIndex);
//    }

    @OnClick(R.id.ivClose)
    public void onClickListenerClose() {
        dismiss();
    }

    private void initView() {
        mTvTitleTypeValue.setText(productName);
        mTvTitleMoneyValue.setText(orderTMoney);
        mTvTitleProfitValue.setText(profitTMoney);
        RecyclerAdapter<OpenOrderEntity> adapter = new RecyclerAdapter<OpenOrderEntity>(R.layout.layout_item_open_prize, mOrderEntities) {
            @Override
            protected void onBindData(RecyclerViewHolder viewHolder, int position, OpenOrderEntity item) {
                viewHolder.setText(R.id.tvTimer, item.getTimerStr())
                        .setText(R.id.tvIndex, item.getOpenIndex())
                        .setText(R.id.tvProfit, item.getProfit())
                        .setText(R.id.tvDate, item.getDateStr())
                        .setText(R.id.tvIndexValue, item.getPurchaseIndex())
                        .setImageResource(R.id.ivIcon, item.getIcon())
                        .setText(R.id.tvMoney, item.getOrderMoney());
            }
        };
        mRvList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRvList.setAdapter(adapter);

        mLineChart.setOnDrawCompletion(this);//设置绘制完成回调时间
        //右边轴value显示格式类
        BaseAxisValueFormatter mRightAxisValue = new BaseAxisValueFormatter();
        mRightAxisValue.setDigit(digit);
        new LineChartSetting2(mContext).setRightIAxisValueFormatter(mRightAxisValue)
                .initLineChart(mLineChart, false);//配置走势图参数
        //设置数据
//        CombinedData combinedData = new CombinedData();
        LineDataSet set = ViewUtil.createLineDataSet(mContext, new ArrayList<>(mMarkEntities));
        LineData lineData = new LineData(set);
//        combinedData.setData(lineData);
        mLineChart.setData(lineData);
        mLineChart.invalidate();
    }

    @Override
    public void completion(IndexMarkEntity lastEntry, IDataSet dataSet) {
//        long startTimer = mMarkEntities.get(0).getTimeLong();
//        long openTimer = openIndex.getTimeLong();
        int xCount = mMarkEntities.size();
        float labelX = mLineChart.getFixedPosition();//标签开始绘制坐标
        float labelWidth = mLineChart.getLabelWidth();//标签长度
        int dpPxRight = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_15);
        float endX = labelX - labelWidth - dpPxRight;
        float itemWidth = endX / xCount;
        float addItem = (labelWidth + dpPxRight) / itemWidth;
        mLineChart.getXAxis().setAxisMaximum(xCount + addItem);
        //绘制购买点
//        int index = 1;
        //刷新购买点的位置
        for (PurchaseViewEntity entity : viewEntities) {
            ViewUtil.getPurchase(mContext, entity, entity.getMoney() + "", entity.isResult());
            if (!isAddPurchase) {//已经添加就不重复添加
                //添加到走势图
                mViewUtil.addPurchaseToView(rlChart, entity, -1);
//                index++;
            }
            //刷新位置
            mViewUtil.refreshPurchaseView(mLineChart, entity, dataSet);
            //启动动画
            PurchaseViewAnimation.getCompleteAnimation(entity.getTvBuyingMone()).start();
        }
        isAddPurchase = true;
        IndexMarkEntity entity = mMarkEntities.get(xCount - 1);
        MPPointD pointD = ViewUtil.getMPPointD(mLineChart, dataSet, entity.getX(), entity.getY());
        //开奖点
        if (tranConDesParams != null && tranConParams != null) {
            //横向描述线
            tranConParams.topMargin = (int) pointD.y;
            //横向描线描述
            tranConDesParams.topMargin = (int) (pointD.y - tranConDesHight / 2);
            tvTransverseContrastDes.setText(mIndexFormatUtil.format(entity.getY()));
        }
        if (settParams != null && settIconParams != null) {
            //结算线
            settParams.leftMargin = (int) pointD.x;
            //图标
            settIconParams.leftMargin = settParams.leftMargin - settIconHight / 2;
        }
        //刷新位置
        rlChart.requestLayout();
    }

    @Override
    protected boolean isAnimation() {
        return false;
    }

    @Override
    protected boolean isBindView() {
        return true;
    }

    @Override
    protected BaseAnimatorSet getShowAs() {
        return null;
    }

    @Override
    protected BaseAnimatorSet getDismissAs() {
        return null;
    }


}
