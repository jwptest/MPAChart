package com.finance.event;

import com.finance.model.ben.IssueEntity;

import java.util.ArrayList;

/**
 * 推送产品期号事件
 */
public class PushIssuesEvent {
    private ArrayList<IssueEntity> issues;

    public PushIssuesEvent(ArrayList<IssueEntity> issues) {
        this.issues = issues;
    }

    public ArrayList<IssueEntity> getIssues() {
        return issues;
    }
}
