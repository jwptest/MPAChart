package com.finance.model.ben;

import java.util.ArrayList;

/**
 * 订单数据
 */
public class OrderEntity {

    private ArrayList<ItemEntity> mArrayList;

    public ArrayList<ItemEntity> getArrayList() {
        return mArrayList;
    }

    public void setArrayList(ArrayList<ItemEntity> arrayList) {
        mArrayList = arrayList;
    }

    public static class ItemEntity {
    }

}
