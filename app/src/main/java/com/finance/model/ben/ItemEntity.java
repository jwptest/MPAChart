package com.finance.model.ben;

/**
 * 列表数据
 */
public class ItemEntity<D> {
    private String title;
    private String message;
    private D data;

    public String getTitle() {
        return (title != null) ? title : "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return (message != null) ? message : "";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}
