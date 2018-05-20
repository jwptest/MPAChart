package com.finance.event;

/**
 * 开奖对话框打开事件
 */
public class OpenPrizeDialogEvent {
    private boolean isOpenDialog = false;

    public OpenPrizeDialogEvent(boolean isOpenDialog) {
        this.isOpenDialog = isOpenDialog;
    }

    public boolean isOpenDialog() {
        return isOpenDialog;
    }

}
