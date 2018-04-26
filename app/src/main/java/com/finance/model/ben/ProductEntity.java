package com.finance.model.ben;

import java.util.ArrayList;

/**
 * 产品信息
 */
public class ProductEntity extends ResponseEntity {

    private String BgColor;// String 背景颜色
    private int CopyIndexMarkPID;// UInt32 设置为测试产品时使用的产品指数
    private double CurrIndexMark;// Decimal 当前指数
    private int Digit;//  Int32 小数位数
    private boolean Disable;//  Boolean 是否被禁用
    private int Expects;// Int32 预计收益
    private String ExtensionColor;// String 扩展颜色
    private String GatewayName;// String 接口名称,为空使用ProductId
    private String Group;// String 产品组
    private int Index;// Int32 排序
    private String IndexMark;//  String 当前指数HEX
    private ArrayList<IssueTypeEntity> IssueTypeNames;//  IssueType[] 拥有期号类型
    private int IssueTypes;// UInt32[] 拥有期号类型
    private boolean IsTest;// Boolean 是否为测试产品
    //    private String Logo;// LogoContract Logo
    private double MaxIndexMark;//  Decimal 今日最高指数
    private double MinIndexMark;//  Decimal 今日最低指数
    private int ProductId;//  UInt32 产品
    private String ProductName;// String 产品名称
    private String ProgressColor;// String 进度条颜色
    private String TitleColor;//  String Title颜色

    public String getBgColor() {
        return (BgColor != null) ? BgColor : "";
    }

    public void setBgColor(String bgColor) {
        BgColor = bgColor;
    }

    public int getCopyIndexMarkPID() {
        return CopyIndexMarkPID;
    }

    public void setCopyIndexMarkPID(int copyIndexMarkPID) {
        CopyIndexMarkPID = copyIndexMarkPID;
    }

    public double getCurrIndexMark() {
        return CurrIndexMark;
    }

    public void setCurrIndexMark(double currIndexMark) {
        CurrIndexMark = currIndexMark;
    }

    public int getDigit() {
        return Digit;
    }

    public void setDigit(int digit) {
        Digit = digit;
    }

    public boolean isDisable() {
        return Disable;
    }

    public void setDisable(boolean disable) {
        Disable = disable;
    }

    public int getExpects() {
        return Expects;
    }

    public void setExpects(int expects) {
        Expects = expects;
    }

    public String getExtensionColor() {
        return (ExtensionColor != null) ? ExtensionColor : "";
    }

    public void setExtensionColor(String extensionColor) {
        ExtensionColor = extensionColor;
    }

    public String getGatewayName() {
        return (GatewayName != null) ? GatewayName : "";
    }

    public void setGatewayName(String gatewayName) {
        GatewayName = gatewayName;
    }

    public String getGroup() {
        return (Group != null) ? Group : "";
    }

    public void setGroup(String group) {
        Group = group;
    }

    public int getIndex() {
        return Index;
    }

    public void setIndex(int index) {
        Index = index;
    }

    public String getIndexMark() {
        return (IndexMark != null) ? IndexMark : "";
    }

    public void setIndexMark(String indexMark) {
        IndexMark = indexMark;
    }

    public ArrayList<IssueTypeEntity> getIssueTypeNames() {
        return IssueTypeNames;
    }

    public void setIssueTypeNames(ArrayList<IssueTypeEntity> issueTypeNames) {
        IssueTypeNames = issueTypeNames;
    }

    public int getIssueTypes() {
        return IssueTypes;
    }

    public void setIssueTypes(int issueTypes) {
        IssueTypes = issueTypes;
    }

    public boolean isTest() {
        return IsTest;
    }

    public void setTest(boolean test) {
        IsTest = test;
    }

    public double getMaxIndexMark() {
        return MaxIndexMark;
    }

    public void setMaxIndexMark(double maxIndexMark) {
        MaxIndexMark = maxIndexMark;
    }

    public double getMinIndexMark() {
        return MinIndexMark;
    }

    public void setMinIndexMark(double minIndexMark) {
        MinIndexMark = minIndexMark;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return (ProductName != null) ? ProductName : "";
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProgressColor() {
        return (ProgressColor != null) ? ProgressColor : "";
    }

    public void setProgressColor(String progressColor) {
        ProgressColor = progressColor;
    }

    public String getTitleColor() {
        return (TitleColor != null) ? TitleColor : "";
    }

    public void setTitleColor(String titleColor) {
        TitleColor = titleColor;
    }
}
