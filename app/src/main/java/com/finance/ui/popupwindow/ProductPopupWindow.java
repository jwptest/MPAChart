package com.finance.ui.popupwindow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.finance.R;
import com.finance.model.ben.ItemEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.widget.commonadapter.viewholders.RecyclerViewHolder;

import java.util.ArrayList;

/**
 * 切换产品对话框
 */
public class ProductPopupWindow extends RecyclerPopupWindow<ProductEntity> {

    public ProductPopupWindow(Context context, int width, ArrayList<ItemEntity<ProductEntity>> mArrayList) {
        super(context, width, ViewGroup.LayoutParams.WRAP_CONTENT, mArrayList);
        selectIndex = 0;
    }

    @Override
    protected void onBindData(RecyclerViewHolder viewHolder, int position, ItemEntity<ProductEntity> item) {
        viewHolder.setText(R.id.tvTitle, item.getTitle());
        viewHolder.setText(R.id.tvBFB, item.getMessage());
    }

    @Override
    protected void onItemClick(int position, ItemEntity<ProductEntity> entity) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.popupwindow_recycler;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.layout_product_recy_item;
    }

    public void showBottom(View parent) {
        showAsDropDown(parent);
    }


}
