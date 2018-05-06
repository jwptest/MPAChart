package com.finance.ui.popupwindow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.finance.R;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.ItemEntity;
import com.finance.widget.commonadapter.viewholders.RecyclerViewHolder;

import java.util.ArrayList;

/**
 * 切换期号对话框
 */
public class IssuesPopupWindow extends RecyclerPopupWindow<IssueEntity> {

    public IssuesPopupWindow(Context context,int width, ArrayList<ItemEntity<IssueEntity>> mArrayList) {
        super(context,width, ViewGroup.LayoutParams.WRAP_CONTENT, mArrayList);
        selectIndex = 0;
    }

    @Override
    protected void onItemClick(int position, ItemEntity<IssueEntity> entity) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.popupwindow_recycler;
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
