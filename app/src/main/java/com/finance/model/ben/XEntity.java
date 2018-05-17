package com.finance.model.ben;

/**
 * X轴数据
 */
public class XEntity {

    private long minIndex;
    private long maxIndex;
    private String value;

    public long getMinIndex() {
        return minIndex;
    }

    public void setMinIndex(long minIndex) {
        this.minIndex = minIndex;
    }

    public long getMaxIndex() {
        return maxIndex;
    }

    public void setMaxIndex(long maxIndex) {
        this.maxIndex = maxIndex;
    }

    public String getValue() {
        return (value != null) ? value : "";
    }

    public void setValue(String value) {
        this.value = value;
    }
}
