package com.finance.model.ben;

import java.util.ArrayList;

/**
 * 期号列表
 */
public class IssuesEntity extends ResponseEntity {

    public ArrayList<IssueEntity> getIssueInfo() {
        return IssueInfo;
    }

    public void setIssueInfo(ArrayList<IssueEntity> issueInfo) {
        IssueInfo = issueInfo;
    }

    private ArrayList<IssueEntity> IssueInfo;// Issue[]

}
