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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.finance.R;
import com.finance.base.StickyBaseAdapter;
import com.finance.model.ben.DynamicEntity;
import com.finance.widget.commonadapter.viewholders.RecyclerViewHolder;
import com.finance.widget.indexrecyclerview.expandRecyclerviewadapter.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;

/**
 * 订单适配器
 */
public class DynamicAdapter extends StickyBaseAdapter<DynamicEntity> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    public DynamicAdapter(ArrayList<DynamicEntity> mlist) {
        addAll(mlist);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dynamic_recy, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        DynamicEntity entity = getItem(position);
    }

    @Override
    public long getHeaderId(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dynamic_recy_head, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    private static class ItemHolder extends RecyclerViewHolder {
        TextView tvName;
        TextView tvProduct;
        ProgressBar vProportion;
        TextView tvMoney;

        /**
         * 子项RecyclerView.ViewHolderNew
         *
         * @param itemView
         */
        public ItemHolder(View itemView) {
            super(itemView);
            tvName = findViewById(R.id.tvName);
            tvProduct = findViewById(R.id.tvProduct);
            vProportion = findViewById(R.id.vProportion);
            tvMoney = findViewById(R.id.tvMoney);
        }
    }


}
