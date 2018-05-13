package com.finance.model.ben;

import com.finance.utils.TimerUtil;

/**
 * 订单
 */
public class OrderEntity {

    transient private float bonusHexIndex = 0;//开奖指数
    private String BonusHexIndexMark;//String 中奖指数
    private BonusIndexMarkEntity BonusIndexMark;//中奖指数
    private double BonusMoney;//Decimal 中奖金额
    private String BonusTime;// DateTime 开奖时间
    transient private long openTimer = 0;//开奖时间
    transient private float itemTimer = 0;//总时长
    transient private String data;
    transient private String timer;
    private String CreateTime;//DateTime 创建时间
    private double DecIndexMark;// Decimal 数值型指数
    private int Expects;//Int32 预计收益
    transient private String ExpectsStr;//预计收益字符串
    transient private float hexIndex = 0;//购买指数
    private String HexIndexMark;// String 购买指数
    private BonusIndexMarkEntity IndexMark;//IndexMark 指数
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


//    private String UserId;
//    private Object UserName;
//    private int ProductId;
//    private String ProductTxt;
//    private int IssueType;
//    private String Issue;
//    private IndexMarkEntity IndexMark;
//    private String HexIndexMark;
//    private boolean Result;
//    private int Expects;
//    private double Money;
//    private double BonusMoney;
//    private double Balance;
//    private String BonusTime;
//    private BonusIndexMarkEntity BonusIndexMark;
//    private String BonusHexIndexMark;
//    private String CreateTime;
//    private boolean IsTest;
//    private boolean IsEmptyUser;
//    private int Status;
//    private int Platform;
//    private int SubAccount;
//    private int Currency;
//    private String RiskGroup;
//    private String TaskId;


    public float getBonusHexIndex() {
        return bonusHexIndex;
    }

    public void setBonusHexIndex(float bonusHexIndex) {
        this.bonusHexIndex = bonusHexIndex;
    }

    public float getHexIndex() {
        return hexIndex;
    }

    public void setHexIndex(float hexIndex) {
        this.hexIndex = hexIndex;
    }

    public float getItemTimer() {
        return itemTimer;
    }

    public void setItemTimer(float itemTimer) {
        this.itemTimer = itemTimer;
    }

    public BonusIndexMarkEntity getBonusIndexMark() {
        return BonusIndexMark;
    }

    public void setBonusIndexMark(BonusIndexMarkEntity bonusIndexMark) {
        BonusIndexMark = bonusIndexMark;
    }

    public BonusIndexMarkEntity getIndexMark() {
        return IndexMark;
    }

    public void setIndexMark(BonusIndexMarkEntity indexMark) {
        IndexMark = indexMark;
    }

    public String getExpectsStr() {
        if (ExpectsStr == null)
            ExpectsStr = "￥" + ((100 + Expects) * Money / 100);
        return ExpectsStr;
    }

    public void setExpectsStr(String expectsStr) {
        ExpectsStr = expectsStr;
    }

    public String getData() {
        if (data == null)
            data = TimerUtil.getDate(CreateTime);
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTimer() {
        if (timer == null)
            timer = TimerUtil.getTimer(CreateTime);
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public long getOpenTimer() {
        if (openTimer == 0)
            openTimer = TimerUtil.timerToLong(BonusTime);
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


    public static class BonusIndexMarkEntity {
        /**
         * ProductId : 106
         * Ticks : 636616342200000000
         * SellPrice : 1.1914
         * BidPrice : 1.19125
         * RealtimePrice : 1.191325
         * Expects : 79
         * Unnecessary : true
         * IsLast : true
         */

        private int ProductId;//产品ID
        private long Ticks;//时间戳
        private double SellPrice;//卖出价格
        private double BidPrice;//买入价格
        private double RealtimePrice;//指数
        private int Expects;//预计收益
        private boolean Unnecessary;//是否为非必要数据
        private boolean IsLast;//是否为上次相同点,该类型点不允许购买

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int ProductId) {
            this.ProductId = ProductId;
        }

        public long getTicks() {
            return Ticks;
        }

        public void setTicks(long Ticks) {
            this.Ticks = Ticks;
        }

        public double getSellPrice() {
            return SellPrice;
        }

        public void setSellPrice(double SellPrice) {
            this.SellPrice = SellPrice;
        }

        public double getBidPrice() {
            return BidPrice;
        }

        public void setBidPrice(double BidPrice) {
            this.BidPrice = BidPrice;
        }

        public double getRealtimePrice() {
            return RealtimePrice;
        }

        public void setRealtimePrice(double RealtimePrice) {
            this.RealtimePrice = RealtimePrice;
        }

        public int getExpects() {
            return Expects;
        }

        public void setExpects(int Expects) {
            this.Expects = Expects;
        }

        public boolean isUnnecessary() {
            return Unnecessary;
        }

        public void setUnnecessary(boolean Unnecessary) {
            this.Unnecessary = Unnecessary;
        }

        public boolean isIsLast() {
            return IsLast;
        }

        public void setIsLast(boolean IsLast) {
            this.IsLast = IsLast;
        }
    }

}
