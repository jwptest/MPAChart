package com.finance.listener;

import com.github.mikephil.charting.data.Entry;

/**
 * 事件中心
 */
public class EventDistribution {

    private IChartDraw mChartDraws;
    private IPurchase mPurchase;

    private EventDistribution() {
    }

    private static class EventDistributionInstance {
        private static final EventDistribution INSTANCE = new EventDistribution();
    }

    public static EventDistribution getInstance() {
        return EventDistributionInstance.INSTANCE;
    }

    public void setChartDraws(IChartDraw sChartDraws) {
        this.mChartDraws = sChartDraws;
    }

    public IChartDraw getChartDraws() {
        return mChartDraws;
    }

    public IPurchase getPurchase() {
        return mPurchase;
    }

    public void setPurchase(IPurchase purchase) {
        mPurchase = purchase;
    }

    public interface IChartDraw {
        //走势图绘制完成回调方法
        void onDraw(Entry entry);
    }

    public interface IPurchase {
        //截止购买回调事件 isOrder是否有订单
        void stopPurchase(boolean isOrder);

        //开奖事件回调
        void openPrize(boolean isOrder);
    }

}
