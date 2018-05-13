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
import com.finance.common.Constants;
import com.finance.model.ben.IndexMarkEntity;
import com.finance.model.ben.OrderEntity;
import com.finance.utils.IndexUtil;
import com.finance.widget.CompletedView;
import com.finance.widget.commonadapter.viewholders.RecyclerViewHolder;
import com.finance.widget.indexrecyclerview.expandRecyclerviewadapter.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;

/**
 * 订单适配器
 */
public class OrderAdapter extends StickyBaseAdapter<OrderEntity> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private ICallback mICallback;//回调接口必须设置
    private long progressBar;//进度，计算方式不明
    private IndexUtil mIndexUtil;

    public OrderAdapter(ArrayList<OrderEntity> mlist) {
        mIndexUtil = new IndexUtil();
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

        //购买指数
        if (entity.getHexIndex() == 0) {
            IndexMarkEntity markEntity = mIndexUtil.parseExponentially(0, entity.getHexIndexMark(), Constants.INDEXDIGIT);
            if (markEntity == null) {
                entity.setHexIndex(1);
            } else {
                entity.setHexIndex(markEntity.getY());
            }
        }
        itemHolder.tvPurchase.setText(entity.getHexIndex() + "");//购买指数
        itemHolder.tvMoney2.setText(entity.getMoney() + "");//购买金额

        if (isOpen(entity)) {//开奖完成
            itemHolder.cvProgressBar.setVisibility(View.GONE);
            itemHolder.tvTimer.setText(entity.getTimer());
            itemHolder.tvDate.setText(entity.getData());
            itemHolder.llTimer.setVisibility(View.VISIBLE);
//            OrderEntity.BonusIndexMarkEntity bonusEntity = entity.getBonusIndexMark();
//            if (bonusEntity != null) {
//                itemHolder.tvPurchase2.setText(bonusEntity.getSellPrice() + "");//卖出价格
//            } else {
//                itemHolder.tvPurchase2.setText("--");//卖出价格
//            }
            //开奖指数
            if (entity.getBonusHexIndex() == 0) {
                IndexMarkEntity markEntity = mIndexUtil.parseExponentially(0, entity.getBonusHexIndexMark(), Constants.INDEXDIGIT);
                if (markEntity == null) {
                    entity.setBonusHexIndex(1);
                } else {
                    entity.setBonusHexIndex(markEntity.getY());
                }
            }

            if (entity.isResult() && entity.getHexIndex()< entity.getBonusHexIndex()) {
                itemHolder.tvMoney.setText(entity.getExpectsStr());//收益金额
            } else if (!entity.isResult() && entity.getHexIndex() > entity.getBonusHexIndex()) {
                itemHolder.tvMoney.setText(entity.getExpectsStr());//收益金额
            } else {//买输了
                itemHolder.tvMoney.setText("￥0");//收益金额
            }

            itemHolder.tvPurchase2.setText(entity.getBonusHexIndex() + "");//卖出价格

        } else {//交易中
            itemHolder.cvProgressBar.setVisibility(View.VISIBLE);
            itemHolder.llTimer.setVisibility(View.GONE);

            //计算开奖时间和这期的总时长
            if (entity.getOpenTimer() == 0 || entity.getItemTimer() == 0) {
                long[] openTotalTime = mICallback.getIssueOpenTotalTime(entity.getProductId(), entity.getIssue());
                entity.setOpenTimer(openTotalTime[0]);
                entity.setItemTimer(openTotalTime[1] / 100f);
            }
            //设置进度
            progressBar = (entity.getOpenTimer() - mICallback.getServerTimer());
            if (progressBar < 0) {
                progressBar = 0;
                if (mICallback != null) mICallback.openPrize();
            }
            progressBar = (int) (progressBar * entity.getItemTimer());
            //计算进度
            itemHolder.cvProgressBar.setProgress((int) progressBar);

            //时时指数
            OrderEntity.BonusIndexMarkEntity bonusEntity = entity.getIndexMark();
            //指数
            if (bonusEntity != null) {
                itemHolder.tvPurchase2.setText(bonusEntity.getSellPrice() + "");//卖出价格
            } else {
                itemHolder.tvPurchase2.setText("--");//卖出价格
            }
            itemHolder.tvMoney.setText(entity.getExpectsStr() + "");//预计收益金额
        }

        itemHolder.tvName.setText(entity.getProductTxt());
        if (entity.isResult()) {//涨
            itemHolder.ivRiseFall.setImageResource(R.drawable.add_icon_item);
            itemHolder.tvExplain.setText("看涨");
        } else {//跌
            itemHolder.ivRiseFall.setImageResource(R.drawable.fall_icon_item);
            itemHolder.tvExplain.setText("看跌");
        }
    }

    @Override
    public long getHeaderId(int position) {
        OrderEntity entity = getItem(position);
        if (isOpen(entity)) {
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
        if (isOpen(entity)) {
            icon.setImageResource(R.drawable.complete_icon);
            textView.setText("交易完成订单");
        } else {
            textView.setText("交易中订单");
            icon.setImageResource(R.drawable.inthetransaction_icon);
        }
    }

    private boolean isOpen(OrderEntity entity) {
        if (mICallback.getServerTimer() > entity.getOpenTimer()) {
            return true;
        } else {
            return false;
        }
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

    public void setICallback(ICallback ICallback) {
        mICallback = ICallback;
    }

    public interface ICallback {
        //有到开奖时间的订单
        void openPrize();

        //获取开奖时间和总时长0=开奖时间，1=总时长
        long[] getIssueOpenTotalTime(int productId, String issueName);

        //服务器当前时间
        long getServerTimer();
    }

}
