package com.finance.ui.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.finance.R;
import com.finance.base.BaseViewHandle;
import com.finance.utils.PhoneUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 右菜单处理
 */
public class RightMenu extends BaseViewHandle {

    @BindView(R.id.ivMoneyAdd)
    ImageView ivMoneyAdd;
    @BindView(R.id.tvInvestmentMoney)
    TextView tvInvestmentMoney;
    @BindView(R.id.ivEditBg)
    ImageView ivEditBg;
    @BindView(R.id.ivMoneyReduce)
    ImageView ivMoneyReduce;
    @BindView(R.id.tvProfit)
    TextView tvProfit;
    @BindView(R.id.tvInterestRate)
    TextView tvInterestRate;
//    @BindView(R.id.llRise)
//    LinearLayout llRise;
//    @BindView(R.id.llFall)
//    LinearLayout llFall;

    private IRightMenu mRightMenu;

    private int sept = 10;//金额步长
    private int minMoney = 10;//最少投资金额
    private int maxMoney = 100;//最多投资金额

    public RightMenu(IRightMenu rightMenu) {
        this.mRightMenu = rightMenu;
    }

    @Override
    public RightMenu onInit(View rootView) {
        super.onInit(rootView);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (RightMenu.this.rootView.getWidth() <= 0) return;
                Context context = RightMenu.this.rootView.getContext();
                RightMenu.this.rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = RightMenu.this.rootView.getWidth() - PhoneUtil.dip2px(RightMenu.this.rootView.getContext(), 1);
                width = width - (context.getResources().getDimensionPixelOffset(R.dimen.dp_5) * 2);
                width = width - (context.getResources().getDimensionPixelOffset(R.dimen.dp_2) * 2);

                float item = width / 4;
                int hight = context.getResources().getDimensionPixelOffset(R.dimen.dp_47);
                if (item < hight) {
                    hight = (int) item;
                }
                ViewGroup.LayoutParams paramsAdd = ivMoneyAdd.getLayoutParams();
                paramsAdd.width = (int) item;
                paramsAdd.height = hight;
                ivMoneyAdd.setLayoutParams(paramsAdd);

                ViewGroup.LayoutParams paramsReduce = ivMoneyReduce.getLayoutParams();
                paramsReduce.width = (int) item;
                paramsReduce.height = hight;
                ivMoneyReduce.setLayoutParams(paramsReduce);

                ViewGroup.LayoutParams paramsEditBg = ivEditBg.getLayoutParams();
                paramsEditBg.width = (int) (item * 2);
                paramsEditBg.height = hight;
                ivEditBg.setLayoutParams(paramsEditBg);

                ViewGroup.LayoutParams paramsEdit = tvInvestmentMoney.getLayoutParams();
                paramsEdit.width = paramsEditBg.width;
                paramsEdit.height = hight;
                tvInvestmentMoney.setLayoutParams(paramsEdit);
            }
        });
        return this;
    }

    @OnClick({R.id.ivMoneyAdd, R.id.ivMoneyReduce, R.id.ivRise, R.id.ivFall})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivMoneyAdd://金额加
                deliveryUpdate(sept);
                break;
            case R.id.ivMoneyReduce://金额减
                deliveryUpdate(-sept);
                break;
            case R.id.ivRise://涨
                rise();
                break;
            case R.id.ivFall://跌
                fall();
                break;
        }
    }

    private int getMoney() {
        String tag = tvInvestmentMoney.getTag() + "";
        int money = 10;
        try {
            money = Integer.parseInt(tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return money;
    }

    /**
     * 递增或者递减
     */
    private void deliveryUpdate(int sept) {
        int money = getMoney();
        money += sept;
        setMoney(money);
    }

    /**
     * 设置金额
     */
    private void setMoney(int money) {
        if (minMoney != -1 && money < minMoney) money = minMoney;
        else if (maxMoney != -1 && money > maxMoney) money = maxMoney;
        tvInvestmentMoney.setText(String.format(Locale.CHINA, "￥%s", money));
        tvInvestmentMoney.setTag(money);
    }

    private void rise() {
        //看涨
        mRightMenu.rise(getMoney());
    }

    private void fall() {
        //看跌
        mRightMenu.fall(getMoney());
    }

}
