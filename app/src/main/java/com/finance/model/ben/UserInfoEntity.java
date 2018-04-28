package com.finance.model.ben;

import java.util.ArrayList;

/**
 * 用户信息
 */
public class UserInfoEntity extends ResponseEntity {

    //{"UserId":"59d60b0e-5cf9-4c27-9814-86452f40916e","UserName":"$c4e48f8e55d3b98c$","NickName":"$c4e48f8e55d3b98c$",
    // "IsBindRealName":false,"RealName":"","IdCardType":0,"IdCardNo":"","IsBindMobileNumber":false,"MobileNumber":"",
    // "IsBindBankCard":false,"BankCode":0,"BankName":"","BankCardNo":"",
    // "Accounts":[{"AccountId":"0a4196d0-647a-4166-b849-a1ef8bf622da","Money":1000.0000,
    // "RechargeMoney":1000.0000,"Name":"模拟用户注册送1000","Type":10,"ValidStartTime":"2018-04-28T15:50:09.76",
    // "ValidEndTime":"2118-04-04T15:50:09.76","SubAccount":999,"SubGiftId":"00000000-0000-0000-0000-000000000000"}],
    // "Freeze":0.0,"Logo":"0","Sex":"","Country":"","ReceiveNotify":true,"IsPayPass":false,
    // "AgentId":"00000000-0000-0000-0000-000000000000","IsAgent":false,"IsTemporary":true,"PromotionCode":null,
    // "SelectAccount":999,"SubAccount":999,"MerchantInfo":null,"NeedBindContact":false,"NeedSecurity":false,
    // "MessageId":"3fc6bf1a-a7d1-4136-9ca5-5b304799b03c","SourceCode":105,"Status":0,"Message":null,"Url":null,
    // "Sign":"d215a01e518c8bf2bd16043352706606","RunTime":"0:00:00.0140669",
    // "CurrDateTime":"2018-04-28T16:10:42.5568005+08:00","CNYRate":6.6155}

    private ArrayList<AccountEntity> Accounts;//Account[] 用户账户列表
    private String AgentId;//Guid 代理Id
    private String BankCardNo;// String 绑定的银行卡号
    private int BankCode;// Int32 银行ID
    private String BankName;// String 银行名称
    private String Country;// String 国家
    private String IdCardNo;// String 身份证件号码
    private int IdCardType;// Int32 身份证件类型
    private boolean IsAgent;// Boolean 是否是代理
    private boolean IsBindBankCard;// Boolean 是否绑定银行卡
    private boolean IsBindMobileNumber;// Boolean 是否绑定手机号码
    private boolean IsBindRealName;//Boolean 是否绑定真实姓名
    private boolean IsPayPass;//Boolean 是否设置支付密码
    private boolean IsTemporary;// Boolean 是否为临时帐户
    private String Logo;//String 头像
    private String MobileNumber;//String 手机号码
    private String NickName;//String 用户昵称
    private String RealName;//String 真实姓名
    private boolean ReceiveNotify;//Boolean 是否接收通知
    private String Sex;//String 性别
    private String UserId;//Guid 用户唯一标识
    private String UserName;//String 用户名

    public ArrayList<AccountEntity> getAccounts() {
        return Accounts;
    }

    public void setAccounts(ArrayList<AccountEntity> accounts) {
        Accounts = accounts;
    }

    public String getAgentId() {
        return (AgentId != null) ? AgentId : "";
    }

    public void setAgentId(String agentId) {
        AgentId = agentId;
    }

    public String getBankCardNo() {
        return (BankCardNo != null) ? BankCardNo : "";
    }

    public void setBankCardNo(String bankCardNo) {
        BankCardNo = bankCardNo;
    }

    public int getBankCode() {
        return BankCode;
    }

    public void setBankCode(int bankCode) {
        BankCode = bankCode;
    }

    public String getBankName() {
        return (BankName != null) ? BankName : "";
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getCountry() {
        return (Country != null) ? Country : "";
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getIdCardNo() {
        return (IdCardNo != null) ? IdCardNo : "";
    }

    public void setIdCardNo(String idCardNo) {
        IdCardNo = idCardNo;
    }

    public int getIdCardType() {
        return IdCardType;
    }

    public void setIdCardType(int idCardType) {
        IdCardType = idCardType;
    }

    public boolean isAgent() {
        return IsAgent;
    }

    public void setAgent(boolean agent) {
        IsAgent = agent;
    }

    public boolean isBindBankCard() {
        return IsBindBankCard;
    }

    public void setBindBankCard(boolean bindBankCard) {
        IsBindBankCard = bindBankCard;
    }

    public boolean isBindMobileNumber() {
        return IsBindMobileNumber;
    }

    public void setBindMobileNumber(boolean bindMobileNumber) {
        IsBindMobileNumber = bindMobileNumber;
    }

    public boolean isBindRealName() {
        return IsBindRealName;
    }

    public void setBindRealName(boolean bindRealName) {
        IsBindRealName = bindRealName;
    }

    public boolean isPayPass() {
        return IsPayPass;
    }

    public void setPayPass(boolean payPass) {
        IsPayPass = payPass;
    }

    public boolean isTemporary() {
        return IsTemporary;
    }

    public void setTemporary(boolean temporary) {
        IsTemporary = temporary;
    }

    public String getLogo() {
        return (Logo != null) ? Logo : "";
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getMobileNumber() {
        return (MobileNumber != null) ? MobileNumber : "";
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getNickName() {
        return (NickName != null) ? NickName : "";
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getRealName() {
        return (RealName != null) ? RealName : "";
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public boolean isReceiveNotify() {
        return ReceiveNotify;
    }

    public void setReceiveNotify(boolean receiveNotify) {
        ReceiveNotify = receiveNotify;
    }

    public String getSex() {
        return (Sex != null) ? Sex : "";
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getUserId() {
        return (UserId != null) ? UserId : "";
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return (UserName != null) ? UserName : "";
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
