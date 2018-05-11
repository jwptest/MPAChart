package com.finance.model.ben;

import java.util.ArrayList;

/**
 * 历史指数
 */
public class HistoryIssueEntity extends ResponseEntity {

    private ArrayList<String> IndexMarks;
    private String Issue;
    private IssueInfoEntity IssueInfo;

    public ArrayList<String> getIndexMarks() {
        return IndexMarks;
    }

    public void setIndexMarks(ArrayList<String> indexMarks) {
        IndexMarks = indexMarks;
    }

    public String getIssue() {
        return (Issue != null) ? Issue : "";
    }

    public void setIssue(String issue) {
        Issue = issue;
    }

    public IssueInfoEntity getIssueInfo() {
        return IssueInfo;
    }

    public void setIssueInfo(IssueInfoEntity issueInfo) {
        IssueInfo = issueInfo;
    }


    public static class IssueInfoEntity {
        //{Id: 2317697, ProductId: 106, ProductTxt: "", IssueType: 0, IssueName: "00201805111327", …}
        private int Id;
        private int ProductId;
        private int IssueType;
        private String IssueName;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int productId) {
            ProductId = productId;
        }

        public int getIssueType() {
            return IssueType;
        }

        public void setIssueType(int issueType) {
            IssueType = issueType;
        }

        public String getIssueName() {
            return (IssueName != null) ? IssueName : "";
        }

        public void setIssueName(String issueName) {
            IssueName = issueName;
        }
    }


}
