package com.finance.model.ben;

/**
 * 指数
 */
public class IndexMarks extends ResponseEntity {

    private String[] IndexMarks;//String[] 历史指数
    private String Issue;//String 期号
    private int Second;// UInt32 查询时间秒
    private String Ticks;//DateTime 查询结束时间点

    public String[] getIndexMarks() {
        return IndexMarks;
    }

    public void setIndexMarks(String[] indexMarks) {
        IndexMarks = indexMarks;
    }

    public String getIssue() {
        return (Issue != null) ? Issue : "";
    }

    public void setIssue(String issue) {
        Issue = issue;
    }

    public int getSecond() {
        return Second;
    }

    public void setSecond(int second) {
        Second = second;
    }

    public String getTicks() {
        return (Ticks != null) ? Ticks : "";
    }

    public void setTicks(String ticks) {
        Ticks = ticks;
    }
}
