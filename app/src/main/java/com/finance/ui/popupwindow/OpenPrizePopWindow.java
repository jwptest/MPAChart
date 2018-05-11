package com.finance.ui.popupwindow;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.finance.R;
import com.finance.common.Constants;
import com.finance.interfaces.IChartSetting;
import com.finance.model.ben.HistoryIssueEntity;
import com.finance.model.ben.IndexMarkEntity;
import com.finance.model.ben.OpenOrderEntity;
import com.finance.model.ben.PurchaseViewEntity;
import com.finance.ui.main.MainContract;
import com.finance.ui.main.PurchaseViewAnimation;
import com.finance.utils.HandlerUtil;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开奖对话框
 */
public class OpenPrizePopWindow extends BasePopupWindow implements OnDrawCompletion {

    //
    @BindView(R.id.llRootView)
    View llRootView;
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

    private Activity mContext;
    private MainContract.View mView;
    private IChartSetting mChartSetting;
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

    private boolean isAddPurchase = false;//是否可以返回关闭对话框

    /**
     * 开奖对话框
     *
     * @param view      调用接口
     * @param entity    历史期数
     * @param openIndex 开奖指数
     */
    public OpenPrizePopWindow(@NonNull Activity context, MainContract.View view, IChartSetting mChartSetting,
                              HistoryIssueEntity entity, IndexMarkEntity openIndex,
                              int productId, String issue, String productName, int screenWidth, int screenHeight) {

        super(context, (int) (screenWidth * 0.8f), (int) (screenHeight * 0.8f), (int) (screenWidth * 0.1f), (int) (screenHeight * 0.1));
        this.mContext = context;
        this.mChartSetting = mChartSetting;
        this.mView = view;
        this.mHistoryIssueEntity = entity;
        this.openIndex = openIndex;
        this.productId = productId;
        this.issue = issue;
        this.productName = productName;
        this.mViewUtil = new ViewUtil();
        ViewUtil.setBackground(mContext, llRootView, R.drawable.dialog_open_prize_bg);

        //界面显示出来再处理数据
//        mLineChart.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if (mLineChart.getWidth() <= 0) return;
//                mLineChart.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            }
//        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_open_prize;
    }

    @Override
    protected int getBackColor() {
        return Color.parseColor("#80000000");
    }

    private void initData(final HistoryIssueEntity entity, final IndexMarkEntity openIndex) {
        new Thread() {
            @Override
            public void run() {
                ArrayList<IndexMarkEntity> entities = new IndexUtil().parseExponentially(0, entity.getIndexMarks(), Constants.INDEXDIGIT);
                if (entities == null) return;
//                HistoryIssueEntity.IssueInfoEntity issueEntity = entity.getIssueInfo();
                ArrayList<PurchaseViewEntity> viewEntities = mView.getPurchase(productId, issue);
                ArrayList<OpenOrderEntity> mOrderEntities = new ArrayList<OpenOrderEntity>(4);
                int size = entities.size() - 1;
                int orderTMoney = 0, profitTMoney = 0;

                for (PurchaseViewEntity viewEntity : viewEntities) {
                    IndexMarkEntity indexMarkEntity = null;
                    for (int i = size; i >= 0; i--) {
                        indexMarkEntity = entities.get(i);
                        if (TextUtils.equals(viewEntity.getIndexMark(), indexMarkEntity.getId())) {
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
                        orderEntity.setPurchaseIndex(indexMarkEntity.getY() + "");//指数
                        orderEntity.setOpenIndex(openIndex.getY() + "");
                        orderEntity.setIcon(viewEntity.isResult() ? R.drawable.add_icon_item : R.drawable.fall_icon_item);
                        orderTMoney += viewEntity.getMoney();//订单金额
                        orderEntity.setOrderMoney("￥" + viewEntity.getMoney());
                        int profit = 0;
                        if (viewEntity.isResult() && openIndex.getY() > indexMarkEntity.getY()) {//猜涨
                            profit = viewEntity.getMoney() * (viewEntity.getExpects() + 100) / 100;
                        } else if (!viewEntity.isResult() && openIndex.getY() < indexMarkEntity.getY()) {//猜跌
                            profit = viewEntity.getMoney() * (viewEntity.getExpects() + 100) / 100;
                        }
                        orderEntity.setProfit("￥" + profit);
                        profitTMoney += profit;
                        mOrderEntities.add(orderEntity);
                        //购买点数据
                        viewEntity.setxValue(indexMarkEntity.getX());
                        viewEntity.setyValue(indexMarkEntity.getY());
                    }
                }
                if (!OpenPrizePopWindow.this.isShowing()) return;
                OpenPrizePopWindow.this.orderTMoney = "￥" + orderTMoney;
                OpenPrizePopWindow.this.profitTMoney = "￥" + profitTMoney;
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

    @OnClick(R.id.tvTitleType)
    public void onClickListener() {
        initData(mHistoryIssueEntity, openIndex);
    }

    private void initView() {
        mTvTitleTypeValue.setText(productName);
        mTvTitleMoneyValue.setText(orderTMoney);
        mTvTitleProfitValue.setText(profitTMoney);
        RecyclerAdapter<OpenOrderEntity> adapter = new RecyclerAdapter<OpenOrderEntity>(R.layout.layout_item_open_prize, mOrderEntities) {
            @Override
            protected void onBindData(RecyclerViewHolder viewHolder, int position, OpenOrderEntity item) {
                viewHolder.setText(R.id.tvTimer, item.getTimerStr())
                        .setText(R.id.tvIndex, item.getPurchaseIndex())
                        .setText(R.id.tvProfit, item.getProfit())
                        .setText(R.id.tvDate, item.getDateStr())
                        .setText(R.id.tvIndexValue, item.getOpenIndex())
                        .setImageResource(R.id.ivIcon, item.getIcon())
                        .setText(R.id.tvMoney, item.getOrderMoney());
            }
        };
        mRvList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRvList.setAdapter(adapter);

        mLineChart.setOnDrawCompletion(this);//设置绘制完成回调时间
        mChartSetting.initLineChart(mLineChart, false);//配置走势图参数
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
        //绘制购买点
        int index = 1;
        //刷新购买点的位置
        for (PurchaseViewEntity entity : viewEntities) {
            ViewUtil.getPurchase(mContext, entity, entity.getMoney() + "", entity.isResult());
            if (!isAddPurchase) {//已经添加就不重复添加
                //添加到走势图
                mViewUtil.addPurchaseToView(rlChart, entity, index);
                index++;
            }
            //刷新位置
            mViewUtil.refreshPurchaseView(mLineChart, entity, dataSet);
            //启动动画
            PurchaseViewAnimation.getCompleteAnimation(entity.getTvBuyingMone()).start();
        }
        //刷新位置
        rlChart.requestLayout();
        isAddPurchase = true;
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
