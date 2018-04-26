package com.finance.model.ben;

/**
 * 旗号类型
 */
public class IssueTypeEntity {

    private int RangeTimes;// Int32 k线统计时间(单位秒),默认为5秒(各Type之间相同时只有一根k线)
    private int TimeSpan;// UInt32 时间间隔
    private int TimeType;//UInt32 时间类型
    private String TypeName;//String 类型名称

    public int getRangeTimes() {
        return RangeTimes;
    }

    public void setRangeTimes(int rangeTimes) {
        RangeTimes = rangeTimes;
    }

    public int getTimeSpan() {
        return TimeSpan;
    }

    public void setTimeSpan(int timeSpan) {
        TimeSpan = timeSpan;
    }

    public int getTimeType() {
        return TimeType;
    }

    public void setTimeType(int timeType) {
        TimeType = timeType;
    }

    public String getTypeName() {
        return (TypeName != null) ? TypeName : "";
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }
}
