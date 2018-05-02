package com.finance.model.ben;

import java.util.ArrayList;

/**
 * 订单数据
 */
public class OrdersEntity extends ResponseEntity {

    private ArrayList<OrderEntity> Orders;//购买记录
    private double CurrentBonusMoney;// Decimal 当前页总中奖金额
    private double CurrentMoney;// Decimal 当前页总金额
    private int Page;//Int32 页码
    private int PageSize;//Int32 每页记录条数
    private int Total;//Int32 总记录条数
    private double TotalBonusMoney;// Decimal 总中奖金额
    private double TotalMoney;// Decimal 总金额

    public ArrayList<OrderEntity> getOrders() {
        return Orders;
    }

    public void setOrders(ArrayList<OrderEntity> orders) {
        Orders = orders;
    }

    public double getCurrentBonusMoney() {
        return CurrentBonusMoney;
    }

    public void setCurrentBonusMoney(double currentBonusMoney) {
        CurrentBonusMoney = currentBonusMoney;
    }

    public double getCurrentMoney() {
        return CurrentMoney;
    }

    public void setCurrentMoney(double currentMoney) {
        CurrentMoney = currentMoney;
    }

    public int getPage() {
        return Page;
    }

    public void setPage(int page) {
        Page = page;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public double getTotalBonusMoney() {
        return TotalBonusMoney;
    }

    public void setTotalBonusMoney(double totalBonusMoney) {
        TotalBonusMoney = totalBonusMoney;
    }

    public double getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        TotalMoney = totalMoney;
    }

}
