package com.finance.model.ben;

/**
 * 开奖订单
 */
public class OpenOrderEntity {

    private String dateStr;//日期
    private String timerStr;//时间
    private String purchaseIndex;//购买指数
    private String openIndex;//开奖指数
    private String profit;//收益
    private String orderMoney;//订单金额
    private int icon;//图标

    public String getDateStr() {
        return (dateStr != null) ? dateStr : "";
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getTimerStr() {
        return (timerStr != null) ? timerStr : "";
    }

    public void setTimerStr(String timerStr) {
        this.timerStr = timerStr;
    }

    public String getPurchaseIndex() {
        return (purchaseIndex != null) ? purchaseIndex : "";
    }

    public void setPurchaseIndex(String purchaseIndex) {
        this.purchaseIndex = purchaseIndex;
    }

    public String getOpenIndex() {
        return (openIndex != null) ? openIndex : "";
    }

    public void setOpenIndex(String openIndex) {
        this.openIndex = openIndex;
    }

    public String getProfit() {
        return (profit != null) ? profit : "";
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getOrderMoney() {
        return (orderMoney != null) ? orderMoney : "";
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
