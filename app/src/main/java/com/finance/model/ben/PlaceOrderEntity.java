package com.finance.model.ben;

/**
 * 购买请求返回实体
 */
public class PlaceOrderEntity extends ResponseEntity {
    /**
     * OrderId : OID-20180507120408-001
     * Ticks : 2018-05-07T12:05:00+08:00
     * Money : 10.0
     * OrderStatus : 20
     * Expects : 79
     * CreateTime : 2018-05-07T12:04:08.4289928+08:00
     * Result : true
     * BonusMoney : 0.0
     * HexIndexMark : 6A08D5B412A2B560C050001D35B1
     * BonusHexIndexMark : 0000000000000000000000000000
     * IsTest : true
     * IssueType : 0
     * ProductId : 106
     * Issue : 00201805071449
     * SubAccount : 999
     * TaskId : 00000000-0000-0000-0000-000000000000
     * Url : null
     * Sign : null
     * CNYRate : 6.6155
     */

    //{"OrderId":"OID-20180510213251-001","Ticks":"2018-05-10T21:34:00+08:00","Money":10.0,"OrderStatus":20,"Expects":79,
    // "CreateTime":"2018-05-10T21:32:51.4626572+08:00","Result":true,"BonusMoney":0.0,"HexIndexMark":"6A08D5B6BD943EA1C06001237751",
    // "BonusHexIndexMark":"0000000000000000000000000000","IsTest":true,"IssueType":0,"ProductId":106,"Issue":"00201805102587",
    // "SubAccount":999,"TaskId":"00000000-0000-0000-0000-000000000000","MessageId":"3fc6bf1a-a7d1-4136-9ca5-5b304799b03c",
    // "SourceCode":201,"Status":0,"Message":"购买成功","Url":null,"Sign":null,"RunTime":"0:00:00.4436998",
    // "CurrDateTime":"2018-05-10T21:32:51.5095347+08:00","CNYRate":6.3775}

    private String OrderId;
    private String Ticks;//开奖时间
    private double Money;//购买金额
    private int OrderStatus;
    private int Expects;
    private boolean Result;
    private double BonusMoney;
    private String HexIndexMark;//购买的指数
    private String BonusHexIndexMark;
    private boolean IsTest;
    private int IssueType;
    private int ProductId;
    private String Issue;//购买的期数
    private String TaskId;

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String OrderId) {
        this.OrderId = OrderId;
    }

    public String getTicks() {
        return Ticks;
    }

    public void setTicks(String Ticks) {
        this.Ticks = Ticks;
    }

    public double getMoney() {
        return Money;
    }

    public void setMoney(double Money) {
        this.Money = Money;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(int OrderStatus) {
        this.OrderStatus = OrderStatus;
    }

    public int getExpects() {
        return Expects;
    }

    public void setExpects(int Expects) {
        this.Expects = Expects;
    }


    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean Result) {
        this.Result = Result;
    }

    public double getBonusMoney() {
        return BonusMoney;
    }

    public void setBonusMoney(double BonusMoney) {
        this.BonusMoney = BonusMoney;
    }

    public String getHexIndexMark() {
        return HexIndexMark;
    }

    public void setHexIndexMark(String HexIndexMark) {
        this.HexIndexMark = HexIndexMark;
    }

    public String getBonusHexIndexMark() {
        return BonusHexIndexMark;
    }

    public void setBonusHexIndexMark(String BonusHexIndexMark) {
        this.BonusHexIndexMark = BonusHexIndexMark;
    }

    public boolean isIsTest() {
        return IsTest;
    }

    public void setIsTest(boolean IsTest) {
        this.IsTest = IsTest;
    }

    public int getIssueType() {
        return IssueType;
    }

    public void setIssueType(int IssueType) {
        this.IssueType = IssueType;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int ProductId) {
        this.ProductId = ProductId;
    }

    public String getIssue() {
        return Issue;
    }

    public void setIssue(String Issue) {
        this.Issue = Issue;
    }


    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String TaskId) {
        this.TaskId = TaskId;
    }


}
