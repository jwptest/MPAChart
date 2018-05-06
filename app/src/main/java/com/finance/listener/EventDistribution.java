package com.finance.listener;

import com.finance.model.ben.ProductEntity;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * 事件中心
 */
public class EventDistribution {

    private ArrayList<IChartDraw> mChartDraws;
    private ArrayList<IPurchase> mPurchase;
    private ArrayList<IProductChecked> mProductCheckeds;

    private EventDistribution() {
        mChartDraws = new ArrayList<>(2);
        mPurchase = new ArrayList<>(2);
        mProductCheckeds = new ArrayList<>(2);
    }

    private static class EventDistributionInstance {
        private static final EventDistribution INSTANCE = new EventDistribution();
    }

    public static EventDistribution getInstance() {
        return EventDistributionInstance.INSTANCE;
    }

    public void addChartDraws(IChartDraw sChartDraws) {
        mChartDraws.add(sChartDraws);
    }

    public void removeChartDraws(IChartDraw sChartDraws) {
        mChartDraws.remove(sChartDraws);
    }

    //执行IChartDraw接口方法
    public void onDraw(Entry entry) {
        for (IChartDraw draw : mChartDraws) {
            draw.onDraw(entry);
        }
    }

    public void removePurchase(IPurchase purchase) {
        mPurchase.remove(purchase);
    }

    public void addPurchase(IPurchase purchase) {
        mPurchase.add(purchase);
    }

    public void purchase(boolean isOpenPrize, boolean isOrder) {
        for (IPurchase purchase : mPurchase) {
            if (isOpenPrize) {
                purchase.openPrize(isOrder);
            } else {
                purchase.stopPurchase(isOrder);
            }
        }
    }

    public void removeProduct(IProductChecked purchase) {
        mProductCheckeds.remove(purchase);
    }

    public void addProduct(IProductChecked purchase) {
        mProductCheckeds.add(purchase);
    }

    public void product(ProductEntity entity) {
        for (IProductChecked product : mProductCheckeds) {
            product.productChecked(entity);
        }
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

    //产品改变
    public interface IProductChecked {
        void productChecked(ProductEntity entity);
    }

}
