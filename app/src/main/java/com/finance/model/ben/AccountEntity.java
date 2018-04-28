package com.finance.model.ben;

/**
 * 用户账号信息
 */
public class AccountEntity {
    private String AccountId;//Guid 账户ID
    private double Money;//Decimal 余额
    private String Name;//String 描述名称,红包为红包名称
    private double RechargeMoney;// Decimal 原始充值金额
    private int Type;//用户账户类型
    private String ValidEndTime;// DateTime 有效期结束时间
    private String ValidStartTime;// DateTime 有效期开始时间

    public String getAccountId() {
        return (AccountId != null) ? AccountId : "";
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public double getMoney() {
        return Money;
    }

    public void setMoney(double money) {
        Money = money;
    }

    public String getName() {
        return (Name != null) ? Name : "";
    }

    public void setName(String name) {
        Name = name;
    }

    public double getRechargeMoney() {
        return RechargeMoney;
    }

    public void setRechargeMoney(double rechargeMoney) {
        RechargeMoney = rechargeMoney;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getValidEndTime() {
        return (ValidEndTime != null) ? ValidEndTime : "";
    }

    public void setValidEndTime(String validEndTime) {
        ValidEndTime = validEndTime;
    }

    public String getValidStartTime() {
        return (ValidStartTime != null) ? ValidStartTime : "";
    }

    public void setValidStartTime(String validStartTime) {
        ValidStartTime = validStartTime;
    }
}
