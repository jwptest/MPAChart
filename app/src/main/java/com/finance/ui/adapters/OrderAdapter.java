/**
 * *                #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG              #
 * #                                                   #
 */

package com.finance.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.finance.R;
import com.finance.base.StickyBaseAdapter;
import com.finance.model.ben.OrderEntity;
import com.finance.widget.CompletedView;
import com.finance.widget.commonadapter.viewholders.RecyclerViewHolder;
import com.finance.widget.indexrecyclerview.expandRecyclerviewadapter.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;

/**
 * 订单适配器
 */
public class OrderAdapter extends StickyBaseAdapter<OrderEntity> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private long serviceTimer;//服务器时间

    public OrderAdapter(ArrayList<OrderEntity> mlist) {
        addAll(mlist);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_recy, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        OrderEntity entity = getItem(position);
        if (isOpen(entity, position)) {//开奖完成
            itemHolder.cvProgressBar.setVisibility(View.GONE);
//            itemHolder.tvTimer.setText(entity.getCreateTime());
//            itemHolder.tvDate.setText(entity.getCreateTime());
            itemHolder.llTimer.setVisibility(View.VISIBLE);
        } else {//交易中
            itemHolder.cvProgressBar.setVisibility(View.VISIBLE);
            itemHolder.llTimer.setVisibility(View.GONE);
//            计算进度
            itemHolder.cvProgressBar.setProgress(30);
        }
//
//        if (entity.isResult()) {//涨
//            itemHolder.ivRiseFall.setImageResource(R.drawable.rise_icon);
//            itemHolder.tvExplain.setText("看涨");
//        } else {//跌
//            itemHolder.ivRiseFall.setImageResource(R.drawable.fall_icon);
//            itemHolder.tvExplain.setText("看跌");
//        }
//
//        itemHolder.tvName.setText(entity.getProductTxt());
//        itemHolder.tvPurchase.setText(entity.getBonusHexIndexMark());
//        itemHolder.tvMoney.setText(entity.getBonusMoney() + "");
//        itemHolder.tvPurchase2.setText("指数");
//        itemHolder.tvName.setText(entity.getMoney() + "");
    }

    @Override
    public long getHeaderId(int position) {
        OrderEntity entity = getItem(position);
        if (isOpen(entity, position)) {
            return 1;
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_recy_head, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageView icon = holder.itemView.findViewById(R.id.ivStateIcon);//.setimage
        TextView textView = holder.itemView.findViewById(R.id.tvTransaction);
        OrderEntity entity = getItem(position);
        if (isOpen(entity, position)) {
            icon.setImageResource(R.drawable.rise_icon);
            textView.setText("交易完成订单");
        } else {
            textView.setText("交易中订单");
            icon.setImageResource(R.drawable.fall_icon);
        }
    }

    private boolean isOpen(OrderEntity entity, int i) {
        return i > 5;
//        if (serviceTimer > entity.getOpenTimer()) {
//            return true;
//        } else {
//            return false;
//        }
    }

    public void setServiceTimer(long serviceTimer) {
        this.serviceTimer = serviceTimer;
    }

    public class ItemHolder extends RecyclerViewHolder {
        CompletedView cvProgressBar;
        View llTimer;
        TextView tvTimer;
        TextView tvName;
        TextView tvPurchase;
        TextView tvMoney;
        TextView tvDate;
        ImageView ivRiseFall;
        TextView tvExplain;
        TextView tvPurchase2;
        TextView tvMoney2;

        /**
         * 子项RecyclerView.ViewHolderNew
         *
         * @param itemView
         */
        public ItemHolder(View itemView) {
            super(itemView);
            cvProgressBar = findViewById(R.id.cvProgressBar);
            llTimer = findViewById(R.id.llTimer);
            tvTimer = findViewById(R.id.tvTimer);
            tvName = findViewById(R.id.tvName);
            tvPurchase = findViewById(R.id.tvPurchase);
            tvMoney = findViewById(R.id.tvMoney);
            tvDate = findViewById(R.id.tvDate);
            ivRiseFall = findViewById(R.id.ivRiseFall);
            tvExplain = findViewById(R.id.tvExplain);
            tvPurchase2 = findViewById(R.id.tvPurchase2);
            tvMoney2 = findViewById(R.id.tvMoney2);
        }
    }


}
