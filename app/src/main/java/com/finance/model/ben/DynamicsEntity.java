package com.finance.model.ben;

import java.util.ArrayList;

/**
 * 动态数据
 */
public class DynamicsEntity extends ResponseEntity {

    private ArrayList<DynamicEntity> Trends;

    public ArrayList<DynamicEntity> getTrends() {
        return Trends;
    }

    public void setTrends(ArrayList<DynamicEntity> trends) {
        Trends = trends;
    }
}
