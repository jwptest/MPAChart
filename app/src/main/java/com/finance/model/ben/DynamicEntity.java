package com.finance.model.ben;

/**
 * 动态
 */
public class DynamicEntity {

    transient private boolean isTitle = false;

    private double BonusMoney;// Decimal 中奖金额
    private int BuyBonus;//Int32 购买还是中奖 0购买，1中奖
    private String CreateTime;//DateTime 创建时间(UTC时间)
    private String Description;//String 动态描述
    private double IndexMark;//Decimal 购买指数
    //   LogoImg ;//LogoContract 图标
    private double Money;//Decimal 下注金额
    private String Product;//String 产品ID
    private String ProductName;//String 产品名称
    private int UpLows;//Int32 涨/跌(1,0)
    private String UserName;//String 用户名

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    public double getBonusMoney() {
        return BonusMoney;
    }

    public void setBonusMoney(double bonusMoney) {
        BonusMoney = bonusMoney;
    }

    public int getBuyBonus() {
        return BuyBonus;
    }

    public void setBuyBonus(int buyBonus) {
        BuyBonus = buyBonus;
    }

    public String getCreateTime() {
        return (CreateTime != null) ? CreateTime : "";
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getDescription() {
        return (Description != null) ? Description : "";
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getIndexMark() {
        return IndexMark;
    }

    public void setIndexMark(double indexMark) {
        IndexMark = indexMark;
    }

    public double getMoney() {
        return Money;
    }

    public void setMoney(double money) {
        Money = money;
    }

    public String getProduct() {
        return (Product != null) ? Product : "";
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getProductName() {
        return (ProductName != null) ? ProductName : "";
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getUpLows() {
        return UpLows;
    }

    public void setUpLows(int upLows) {
        UpLows = upLows;
    }

    public String getUserName() {
        return (UserName != null) ? UserName : "";
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
