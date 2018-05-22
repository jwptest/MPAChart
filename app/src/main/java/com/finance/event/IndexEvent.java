package com.finance.event;

/**
 * 获取指数事件
 */
public class IndexEvent {

    private int ProductId;
    private String ProductName;
    private String IssueName;
    private String Time;
    private int digit;

    public IndexEvent(int productId, String productName, String issueName, String time, int digit) {
        this.ProductId = productId;
        this.ProductName = productName;
        this.IssueName = issueName;
        this.Time = time;
        this.digit = digit;
    }

    public int getProductId() {
        return ProductId;
    }

    public String getProductName() {
        return (ProductName != null) ? ProductName : "";
    }

    public String getIssueName() {
        return (IssueName != null) ? IssueName : "";
    }

    public String getTime() {
        return (Time != null) ? Time : "";
    }

    public int getDigit() {
        return digit;
    }
}
