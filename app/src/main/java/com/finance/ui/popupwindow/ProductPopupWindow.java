package com.finance.ui.popupwindow;

import android.content.Context;

import com.finance.model.ben.ItemEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.widget.commonadapter.viewholders.RecyclerViewHolder;

import java.util.ArrayList;

/**
 * ===============================
 * 描    述：
 * 作    者：pjw
 * 创建日期：2018/5/1 15:02
 * ===============================
 */
public class ProductPopupWindow extends RecyclerPopupWindow<ProductEntity> {


    public ProductPopupWindow(Context context, ArrayList<ItemEntity<ProductEntity>> mArrayList) {
        super(context, mArrayList);
    }

    @Override
    protected void onItemClick(int position, ItemEntity<ProductEntity> entity) {

    }

    @Override
    protected void onBindData(RecyclerViewHolder viewHolder, int position, ItemEntity<ProductEntity> item) {

    }

    @Override
    protected int getItemLayoutId() {
        return 0;
    }
}
