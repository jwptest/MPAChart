package com.finance.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.finance.R;
import com.finance.widget.combinedchart.MCombinedChart;
import com.finance.widget.commonadapter.RecyclerAdapter;
import com.finance.widget.commonadapter.viewholders.RecyclerViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 开奖对话框
 */
public class OpenPrizeDialog extends Dialog {

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

    private Context mContext;

    public OpenPrizeDialog(@NonNull Context context) {
        super(context, R.style.noBackDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_open_prize);
        //layout_item_open_prize
        ButterKnife.bind(this);
        ArrayList<String> arrayList = new ArrayList<>(10);
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");

        RecyclerAdapter<String> adapter = new RecyclerAdapter<String>(R.layout.layout_item_open_prize, arrayList) {
            @Override
            protected void onBindData(RecyclerViewHolder viewHolder, int position, String item) {

            }
        };
        mRvList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRvList.setAdapter(adapter);
    }


}
