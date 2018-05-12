package com.finance.model.ben;

import java.util.ArrayList;

/**
 * 消息列表
 */
public class NotesMessage {

    private ArrayList<NoteMessage> NotesList;
    private int Page;// Int32 页码
    private int PageSize;// Int32 每页记录条数
    private int Total;//Int32 总记录条数

    public ArrayList<NoteMessage> getNotesList() {
        return NotesList;
    }

    public void setNotesList(ArrayList<NoteMessage> notesList) {
        NotesList = notesList;
    }

    public int getPage() {
        return Page;
    }

    public void setPage(int page) {
        Page = page;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }
}
