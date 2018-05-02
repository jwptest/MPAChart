package com.finance.model.ben;

/**
 * ===============================
 * 描    述：
 * 作    者：pjw
 * 创建日期：2018/5/2 16:18
 * ===============================
 */
public class OrderEntity {

    private String BonusHexIndexMark;//String 中奖指数
    //    BonusIndexMark IndexMark ;//中奖指数
    private double BonusMoney;//Decimal 中奖金额
    private String BonusTime;// DateTime 开奖时间
    transient private long openTimer;//开奖时间
    private String CreateTime;//DateTime 创建时间
    private double DecIndexMark;// Decimal 数值型指数
    private int Expects;//Int32 预计收益
    private String HexIndexMark;// String 购买指数
    //    IndexMark ;//IndexMark 指数
    private boolean IsEmptyUser;//Boolean 是否为未登录用户订单
    private String Issue;//String 期号
    private int IssueType;//Int32 期号类型
    private boolean IsTest;//Boolean 是否为测试产品
    private double Money;// Decimal 订单金额
    private int Platform;//Int32
    private int ProductId;//Int32 产品
    private String ProductTxt;//String 产品名称
    private boolean Result;// Boolean 买(涨/跌)
    //  private int  RiskLevel ;//Int32
    private String SchemeId;//String 订单号
    private String UserName;//String 用户名
    private int Status;//状态

    public long getOpenTimer() {
        return openTimer;
    }

    public void setOpenTimer(long openTimer) {
        this.openTimer = openTimer;
    }

    public String getBonusHexIndexMark() {
        return (BonusHexIndexMark != null) ? BonusHexIndexMark : "";
    }

    public void setBonusHexIndexMark(String bonusHexIndexMark) {
        BonusHexIndexMark = bonusHexIndexMark;
    }

    public double getBonusMoney() {
        return BonusMoney;
    }

    public void setBonusMoney(double bonusMoney) {
        BonusMoney = bonusMoney;
    }

    public String getBonusTime() {
        return (BonusTime != null) ? BonusTime : "";
    }

    public void setBonusTime(String bonusTime) {
        BonusTime = bonusTime;
    }

    public String getCreateTime() {
        return (CreateTime != null) ? CreateTime : "";
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public double getDecIndexMark() {
        return DecIndexMark;
    }


    public void setDecIndexMark(double decIndexMark) {
        DecIndexMark = decIndexMark;
    }

    public int getExpects() {
        return Expects;
    }

    public void setExpects(int expects) {
        Expects = expects;
    }

    public String getHexIndexMark() {
        return (HexIndexMark != null) ? HexIndexMark : "";
    }

    public void setHexIndexMark(String hexIndexMark) {
        HexIndexMark = hexIndexMark;
    }

    public boolean isEmptyUser() {
        return IsEmptyUser;
    }

    public void setEmptyUser(boolean emptyUser) {
        IsEmptyUser = emptyUser;
    }

    public String getIssue() {
        return (Issue != null) ? Issue : "";
    }

    public void setIssue(String issue) {
        Issue = issue;
    }

    public int getIssueType() {
        return IssueType;
    }

    public void setIssueType(int issueType) {
        IssueType = issueType;
    }

    public boolean isTest() {
        return IsTest;
    }

    public void setTest(boolean test) {
        IsTest = test;
    }

    public double getMoney() {
        return Money;
    }

    public void setMoney(double money) {
        Money = money;
    }

    public int getPlatform() {
        return Platform;
    }

    public void setPlatform(int platform) {
        Platform = platform;
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

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean result) {
        Result = result;
    }

    public String getSchemeId() {
        return (SchemeId != null) ? SchemeId : "";
    }

    public void setSchemeId(String schemeId) {
        SchemeId = schemeId;
    }

    public String getUserName() {
        return (UserName != null) ? UserName : "";
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
