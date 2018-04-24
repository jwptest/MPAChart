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

import com.finance.R;
import com.finance.base.StickyBaseAdapter;
import com.finance.model.ben.OrderEntity;
import com.finance.widget.commonadapter.viewholders.RecyclerViewHolder;
import com.finance.widget.indexrecyclerview.expandRecyclerviewadapter.StickyRecyclerHeadersAdapter;

import java.util.List;

/**
 * 订单适配器
 */
public class OrderAdapter extends StickyBaseAdapter<OrderEntity.ItemEntity> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    //当前显示数据实体
    private OrderEntity entity;

    public OrderAdapter(List<OrderEntity.ItemEntity> mlist) {
        addAll(mlist);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_recy, parent, false);
        return new ItemHolder(view);
    }

    //"investProductReservationId": 0,
//        09-05 14:20:29.648 10441-20910/com.zimadai D/PRETTYLOGGER: ║           "reservationIncreaseInterestRate": 0,
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderEntity.ItemEntity entity = getItem(position);
        ItemHolder itemHolder = (ItemHolder) holder;
    }

    @Override
    public long getHeaderId(int position) {
//        CDInvestRecordsBean.InvestRecordsListEntity entity = getItem(position);
//        return entity.getCreateIndex();
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_recy_head, parent, false);
        return new RecyclerView.ViewHolder(null) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public class ItemHolder extends RecyclerViewHolder {
//        TextView rtv_interest;
//        TextView tv_title;
//        TextView tv_describe;
//        TextView tv_investment_amount;
//        TextView tv_investment_amount_desc;
//        TextView tv_timer;
//        TextView tv_state;
//        TextView tv_increase_interest;
//        RoundTextView rtv_confirm;
//        View iv_state_icon;
//        TextView tv_income_amount;
//        TextView tv_income_amount_desc;
//        View ll_icon;
//        TextView tv_days_remaining;
//        TextView tv_days_remaining_desc;
//        View vDev;

        /**
         * 子项RecyclerView.ViewHolderNew
         *
         * @param itemView
         */
        public ItemHolder(View itemView) {
            super(itemView);
//            rtv_interest = findViewById(R.id.cd_rtv_interest);
//            tv_title = findViewById(R.id.cd_tv_title);
//            tv_describe = findViewById(R.id.cd_tv_describe);
//            tv_investment_amount = findViewById(R.id.cd_tv_investment_amount);
//            tv_investment_amount_desc = findViewById(R.id.cd_tv_investment_amount_desc);
//            tv_timer = findViewById(R.id.cd_tv_timer);
//            tv_state = findViewById(R.id.cd_tv_state);
//            tv_increase_interest = findViewById(R.id.cd_tv_increase_interest);
//            rtv_confirm = findViewById(R.id.cd_rtv_confirm);
//            iv_state_icon = findViewById(R.id.cd_iv_state_icon);
//            tv_income_amount = findViewById(R.id.cd_tv_income_amount);
//            tv_income_amount_desc = findViewById(R.id.cd_tv_income_amount_desc);
//            ll_icon = findViewById(R.id.cd_ll_icon);
//            tv_days_remaining = findViewById(R.id.cd_tv_days_remaining);
//            tv_days_remaining_desc = findViewById(R.id.cd_tv_days_remaining_desc);
//            vDev = findViewById(R.id.cd_v_dev);
        }

    }


}
