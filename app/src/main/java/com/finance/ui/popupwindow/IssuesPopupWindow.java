package com.finance.ui.popupwindow;

import android.content.Context;
import android.view.View;

import com.finance.R;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.ItemEntity;
import com.finance.widget.commonadapter.viewholders.RecyclerViewHolder;

import java.util.ArrayList;

/**
 *
 */
public class IssuesPopupWindow extends RecyclerPopupWindow<IssueEntity> {

    public IssuesPopupWindow(Context context, ArrayList<ItemEntity<IssueEntity>> mArrayList) {
        super(context, mArrayList);
    }

    @Override
    protected void onItemClick(int position, ItemEntity<IssueEntity> entity) {

    }

    @Override
    protected int getLayoutId() {
        return  R.layout.popupwindow_recycler;
    }

    @Override
    protected void onBindData(RecyclerViewHolder viewHolder, int position, ItemEntity<IssueEntity> item) {
        viewHolder.setText(R.id.tvTitle, item.getTitle());
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.layout_issues_recy_item;
    }


    public void showBottom(View parent) {
        showAsDropDown(parent);
    }

}
