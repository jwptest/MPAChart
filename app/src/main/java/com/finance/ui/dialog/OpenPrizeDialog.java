package com.finance.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
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
import com.finance.utils.ViewUtil;
import com.finance.widget.combinedchart.MCombinedChart;
import com.finance.widget.combinedchart.OnDrawCompletion;
import com.finance.widget.commonadapter.RecyclerAdapter;
import com.finance.widget.commonadapter.viewholders.RecyclerViewHolder;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 开奖对话框
 */
public class OpenPrizeDialog extends Dialog implements OnDrawCompletion {

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
    MCombinedChart mLineChart;
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

    private boolean isBack = false;//是否可以返回关闭对话框

    /**
     * 开奖对话框
     *
     * @param view      调用接口
     * @param entity    历史期数
     * @param openIndex 开奖指数
     */
    public OpenPrizeDialog(@NonNull Activity context, MainContract.View view, IChartSetting mChartSetting,
                           HistoryIssueEntity entity, IndexMarkEntity openIndex,
                           int productId, String issue, String productName) {
        super(context, R.style.noBackDialog);
        this.mContext = context;
        this.mChartSetting = mChartSetting;
        this.mView = view;
        this.mHistoryIssueEntity = entity;
        this.openIndex = openIndex;
        this.productId = productId;
        this.issue = issue;
        this.productName = productName;
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
                        orderEntity.setTimerStr(mView.issueNameFormat(createTime));//时间
                        orderEntity.setDateStr(getOrderDate(createTime));
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
                if (!OpenPrizeDialog.this.isShowing()) return;
                OpenPrizeDialog.this.orderTMoney = "￥" + orderTMoney;
                OpenPrizeDialog.this.profitTMoney = "￥" + profitTMoney;
                OpenPrizeDialog.this.mOrderEntities = mOrderEntities;
                OpenPrizeDialog.this.mMarkEntities = entities;
                OpenPrizeDialog.this.viewEntities = viewEntities;
                HandlerUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initView();
                    }
                });
            }
        }.start();
    }

    private String getOrderDate(String createTime) {//2018-05-07T12:04:08.4289928+08:00
        if (TextUtils.isEmpty(createTime) || createTime.length() < 10) return "-";
        createTime = createTime.substring(5, 10);
        return createTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_open_prize);
        ButterKnife.bind(this);
        ViewUtil.setBackground(mContext, llRootView, R.drawable.dialog_open_prize_bg);
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
        CombinedData combinedData = new CombinedData();
        LineDataSet set = ViewUtil.createLineDataSet(mContext, new ArrayList<>(mMarkEntities));
        LineData lineData = new LineData(set);
        combinedData.setData(lineData);
        mLineChart.setData(combinedData);

        //mLineChart.invalidate();
    }

    @Override
    public void completion(IndexMarkEntity lastEntry, IDataSet dataSet) {
        isBack = true;
        //绘制购买点
        ViewUtil mViewUtil = new ViewUtil();
        int index = 0;
        //刷新购买点的位置
        for (PurchaseViewEntity entity : viewEntities) {
            ViewUtil.getPurchase(mContext, entity, entity.getMoney() + "", entity.isResult());
            //添加到走势图
            mViewUtil.addPurchaseToView(rlChart, entity, index);
            //刷新位置
            mViewUtil.refreshPurchaseView(mLineChart, entity, dataSet);
            //启动动画
            PurchaseViewAnimation.getCompleteAnimation(entity.getTvBuyingMone()).start();
            index++;
        }
        //刷新位置
        rlChart.requestLayout();
    }

    @Override
    public void onBackPressed() {
        if (!isBack) return;
        super.onBackPressed();
    }
}
