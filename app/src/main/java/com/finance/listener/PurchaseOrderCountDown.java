package com.finance.listener;

import android.text.TextUtils;

import com.finance.event.EventBus;
import com.finance.event.IndexEvent;
import com.finance.event.OpenPrizeEvent;
import com.finance.utils.TimerUtil;

/**
 * 开奖倒计时
 */
public class PurchaseOrderCountDown {

    private static class PurchaseOrderCountDownInstance {
        private static final PurchaseOrderCountDown INSTANCE = new PurchaseOrderCountDown();
    }

    public static PurchaseOrderCountDown getInstance() {
        return PurchaseOrderCountDownInstance.INSTANCE;
    }

    private long getOpenIndex;//获取开奖指数时间
    private long openTimer;//开奖时间

    public void setOpenIndex(String openIndex) {
        if (TextUtils.isEmpty(openIndex)) {
            this.getOpenIndex = 0;
            return;
        }
        this.getOpenIndex = TimerUtil.timerToLong(openIndex);
    }

    public void setOpenTimer(String openTimer) {
        if (TextUtils.isEmpty(openTimer)) {
            this.openTimer = 0;
            return;
        }
        this.openTimer = TimerUtil.timerToLong(openTimer);
    }

    public void openPrize(long current) {
        if (openTimer > 100 && current >= openTimer) {//开奖事件
            EventBus.post(new OpenPrizeEvent());
            openTimer = 0;
            getOpenIndex = 0;
        } else if (getOpenIndex > 100 && current >= getOpenIndex) {//获取开奖指数事件
            EventBus.post(new IndexEvent());
            getOpenIndex = 0;
        }
    }

}
