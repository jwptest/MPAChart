package com.finance.common.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class CDInvestmentBaseAdapter<D> extends RecyclerView.Adapter {
    private ArrayList<D> items = new ArrayList<>();

    public CDInvestmentBaseAdapter() {
        setHasStableIds(true);
    }

    public void add(D object) {
        items.add(object);
        notifyDataSetChanged();
    }

    public void add(int index, D object) {
        items.add(index, object);
        notifyDataSetChanged();
    }

    public void addAll(List<D> collection) {
        if (collection != null) {
            items.clear();
            items.addAll(collection);
            notifyDataSetChanged();
        }
    }

//    public void addAll(D... items) {
//        addAll(Arrays.asList(items));
//    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void remove(D object) {
        items.remove(object);
        notifyDataSetChanged();
    }

    public D getItem(int position) {
        return position<items.size()?items.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        D en = getItem(position);
        return en != null?getItem(position).hashCode():-1;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
