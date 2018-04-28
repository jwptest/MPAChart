package com.finance.model.ben;

/**
 * 期号
 */
public class IssueEntity {

    private double BonusDecIndexMark;// Decimal
    private String BonusHexIndexMark;// String
    //    BonusIndexMark IndexMark 开奖指数未开奖时为0
//    BonusSourceIndexMark IndexMark 开奖原始指数未开奖时为0
    private String BonusTime;//DateTime 开奖时间
    private double CurrIndexMark;//Decimal 当前指数
    private int Expects;//Int32 预计收益
    private int Id;//Int64 ID
    private String IssueName;//String 期号
    private int IssueType;//Int32 期号类型 30秒=1/60秒=0/90秒=2...
    private double KeepMoney;//Decimal 留存金额(提走金)
    private int LossCount;//Int32 用户亏损数量
    private double LossMoney;//Decimal 用户亏损金额
    private double PaymentMoney;//Decimal 总付款金额
    private int ProductId;//Int32 产品ID
    private String ProductTxt;//String 产品名称
    private int ProfitCount;//Int32 用户盈利数量
    private double ProfitMoney;//Decimal 用户盈利金额
    private String StartTime;//DateTime 开始时间
    private int Status;//Int32 状态
    private int StatusText;//Int32 状态名称
    private String StopTime;//DateTime 结束时间
    private double TradeMoney;//Decimal 总交易金额

    public double getBonusDecIndexMark() {
        return BonusDecIndexMark;
    }

    public void setBonusDecIndexMark(double bonusDecIndexMark) {
        BonusDecIndexMark = bonusDecIndexMark;
    }

    public String getBonusHexIndexMark() {
        return (BonusHexIndexMark != null) ? BonusHexIndexMark : "";
    }

    public void setBonusHexIndexMark(String bonusHexIndexMark) {
        BonusHexIndexMark = bonusHexIndexMark;
    }

    public String getBonusTime() {
        return (BonusTime != null) ? BonusTime : "";
    }

    public void setBonusTime(String bonusTime) {
        BonusTime = bonusTime;
    }

    public double getCurrIndexMark() {
        return CurrIndexMark;
    }

    public void setCurrIndexMark(double currIndexMark) {
        CurrIndexMark = currIndexMark;
    }

    public int getExpects() {
        return Expects;
    }

    public void setExpects(int expects) {
        Expects = expects;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getIssueName() {
        return (IssueName != null) ? IssueName : "";
    }

    public void setIssueName(String issueName) {
        IssueName = issueName;
    }

    public int getIssueType() {
        return IssueType;
    }

    public void setIssueType(int issueType) {
        IssueType = issueType;
    }

    public double getKeepMoney() {
        return KeepMoney;
    }

    public void setKeepMoney(double keepMoney) {
        KeepMoney = keepMoney;
    }

    public int getLossCount() {
        return LossCount;
    }

    public void setLossCount(int lossCount) {
        LossCount = lossCount;
    }

    public double getLossMoney() {
        return LossMoney;
    }

    public void setLossMoney(double lossMoney) {
        LossMoney = lossMoney;
    }

    public double getPaymentMoney() {
        return PaymentMoney;
    }

    public void setPaymentMoney(double paymentMoney) {
        PaymentMoney = paymentMoney;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductTxt() {
        return (ProductTxt != null) ? ProductTxt : "";
    }

    public void setProductTxt(String productTxt) {
        ProductTxt = productTxt;
    }

    public int getProfitCount() {
        return ProfitCount;
    }

    public void setProfitCount(int profitCount) {
        ProfitCount = profitCount;
    }

    public double getProfitMoney() {
        return ProfitMoney;
    }

    public void setProfitMoney(double profitMoney) {
        ProfitMoney = profitMoney;
    }

    public String getStartTime() {
        return (StartTime != null) ? StartTime : "";
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getStatusText() {
        return StatusText;
    }

    public void setStatusText(int statusText) {
        StatusText = statusText;
    }

    public String getStopTime() {
        return (StopTime != null) ? StopTime : "";
    }

    public void setStopTime(String stopTime) {
        StopTime = stopTime;
    }

    public double getTradeMoney() {
        return TradeMoney;
    }

    public void setTradeMoney(double tradeMoney) {
        TradeMoney = tradeMoney;
    }
}
