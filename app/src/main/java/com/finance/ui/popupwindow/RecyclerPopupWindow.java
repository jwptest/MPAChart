package com.finance.ui.popupwindow;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.finance.R;
import com.finance.model.ben.ItemEntity;
import com.finance.widget.commonadapter.RecyclerAdapter;
import com.finance.widget.commonadapter.viewholders.RecyclerViewHolder;

import java.util.ArrayList;

/**
 * 线上列表的PopupWindow
 */
public abstract class RecyclerPopupWindow<D> extends PopupWindow implements RecyclerAdapter.OnItemClickListener {

    protected ArrayList<ItemEntity<D>> mArrayList;
    protected RecyclerView rvList;//线上数据列表
    protected int selectIndex = -1;//当前选中下标
    protected int selColorBG, unSelColorBg;//选中未选中背景颜色
    protected RecyclerAdapter<ItemEntity<D>> mAdapter;
    protected OnItemClicklistener<D> mOnItemClicklistener;

    public RecyclerPopupWindow(Context context, ArrayList<ItemEntity<D>> mArrayList) {
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, mArrayList);
    }

    public RecyclerPopupWindow(Context context, int width, int height, ArrayList<ItemEntity<D>> mArrayList) {
        super(width, height);
        View rootView = LayoutInflater.from(context).inflate(getLayoutId(), null);
        setContentView(rootView);
        rvList = rootView.findViewById(R.id.rvList);
        this.mArrayList = mArrayList;
        rvList.setLayoutManager(getLayoutManager(context));
        mAdapter = getRecyclerAdapter(mArrayList);
        rvList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        setTouchable(true);
        setOutsideTouchable(true);   //设置外部点击关闭ppw窗口
        selColorBG = Color.parseColor("#50141312");
        unSelColorBg = Color.parseColor("#00000000");
    }

    public void onItemClick(int position) {
        selectIndex = position;
        ItemEntity<D> entity = mArrayList.get(position);
        onItemClick(position, entity);
        mAdapter.notifyDataSetChanged();
        if (mOnItemClicklistener != null)
            mOnItemClicklistener.onClickListener(position, entity);
    }

    protected RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    protected RecyclerAdapter<ItemEntity<D>> getRecyclerAdapter(ArrayList<ItemEntity<D>> mArrayList) {
        return new RecyclerAdapter<ItemEntity<D>>(getItemLayoutId(), mArrayList) {
            @Override
            protected void onBindData(RecyclerViewHolder viewHolder, int position, ItemEntity<D> item) {
                RecyclerPopupWindow.this.onBindData(viewHolder, position, item);
                viewHolder.itemView.setBackgroundColor(position == selectIndex ? selColorBG : unSelColorBg);
            }
        };
    }

    protected abstract void onBindData(RecyclerViewHolder viewHolder, int position, ItemEntity<D> item);

    protected abstract void onItemClick(int position, ItemEntity<D> entity);

    protected abstract int getLayoutId();

    protected abstract int getItemLayoutId();

    public void setOnItemClicklistener(OnItemClicklistener<D> onItemClicklistener) {
        mOnItemClicklistener = onItemClicklistener;
    }

    public void setData(ArrayList<ItemEntity<D>> entities) {
        if (mAdapter != null) {
            mAdapter.clear();
            mAdapter.addItems(entities);
        }
        mArrayList = entities;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public interface OnItemClicklistener<D> {
        void onClickListener(int privation, ItemEntity<D> dItemEntity);
    }


}
