package com.finance.model.ben;

/**
 * 开奖指数
 */
public class OpenIndexEntity extends ResponseEntity {

    private String IndexMark;//开奖指数 6A08D5B6C40797620050001D1040

    public String getIndexMark() {
        return (IndexMark != null) ? IndexMark : "";
    }

    public void setIndexMark(String indexMark) {
        IndexMark = indexMark;
    }
}
