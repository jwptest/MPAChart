package com.finance.model.ben;

/**
 * 消息实体
 */
public class NoteMessage {
//    private String CreateTime DateTime 创建时间
//    private String Id String 消息ID
//    QaDetails IEnumerable<QaDetail> 详细内容
//    private String Remark String 客服备注
//    Review Int32 评价0未议价，3好评，2中评，1差评
//    StatusMsg Int32 状态1完成，0处理中
//    TypeId Int32 平台类型 0幸运农场，1二元，2，3，4，
//    TypeMsg QaNotesMsgType 消息类型，0聊天消息，1推送消息，2，3，4
//    private String UserId String 用户ID
//    private String UserIdSer String 客服ID


    private String BonusMoney;// Decimal 中奖金额
    private String BuyBonus;//Int32 购买还是中奖 0购买，1中奖
    private String CreateTime;//DateTime 创建时间(UTC时间)
    private String Description;//String 动态描述
    private String IndexMark;//Decimal 购买指数
    private String LogoImg;//LogoContract 图标
    private String Money;//Decimal 下注金额
    private String Product;//String 产品ID
    private String ProductName;// String 产品名称
    private String UpLows;//Int32 涨/跌(1,0)
    private String UserName;//String 用户名

    public String getBonusMoney() {
        return (BonusMoney != null) ? BonusMoney : "";
    }

    public void setBonusMoney(String bonusMoney) {
        BonusMoney = bonusMoney;
    }

    public String getBuyBonus() {
        return (BuyBonus != null) ? BuyBonus : "";
    }

    public void setBuyBonus(String buyBonus) {
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

    public String getIndexMark() {
        return (IndexMark != null) ? IndexMark : "";
    }

    public void setIndexMark(String indexMark) {
        IndexMark = indexMark;
    }

    public String getLogoImg() {
        return (LogoImg != null) ? LogoImg : "";
    }

    public void setLogoImg(String logoImg) {
        LogoImg = logoImg;
    }

    public String getMoney() {
        return (Money != null) ? Money : "";
    }

    public void setMoney(String money) {
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

    public String getUpLows() {
        return (UpLows != null) ? UpLows : "";
    }

    public void setUpLows(String upLows) {
        UpLows = upLows;
    }

    public String getUserName() {
        return (UserName != null) ? UserName : "";
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
