package com.finance.model.datasource;

/**
 * 分页处理基类
 */
public abstract class DataSource {
    protected int page = 1;
    protected int size = 20;
    private int pageMin;

    public DataSource() {
        this(1, 20);
    }

    public DataSource(int page) {
        this(page, 20);
    }

    public DataSource(int page, int size) {
        this.page = page;
        this.size = size;
        this.pageMin = page;
    }

    public void refresh() {
        load(page, size);
    }

    public void loadMore() {
        load(++page, size);
    }

    //加载失败调用方法
    protected void loadFail() {
        page--;
        if (page < pageMin)
            page = pageMin;
    }

    public abstract void load(int page, int size);

}
