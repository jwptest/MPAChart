package com.finance.ui.main;

import android.app.Activity;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.finance.R;
import com.finance.base.BaseViewHandle;
import com.finance.listener.EventDistribution;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.ui.popupwindow.KeyboardPopupWindow;
import com.finance.utils.BtnClickUtil;
import com.finance.utils.NumberUtil;
import com.finance.utils.TimerUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 右菜单处理
 */
public class RightMenu extends BaseViewHandle implements EventDistribution.IProductChecked, EventDistribution.IPurchase, EventDistribution.IIssueChecked {

    @BindView(R.id.ivMoneyAdd)
    ImageView ivMoneyAdd;
    @BindView(R.id.tvInvestmentMoney)
    TextView tvInvestmentMoney;
    @BindView(R.id.ivEditBg)
    ImageView ivEditBg;
    @BindView(R.id.rlEdit)
    View rlEdit;
    @BindView(R.id.ivRise)
    View ivRise;
    @BindView(R.id.ivFall)
    View ivFall;
    @BindView(R.id.ivMoneyReduce)
    ImageView ivMoneyReduce;
    @BindView(R.id.tvProfit)
    TextView tvProfit;
    @BindView(R.id.tvInterestRate)
    TextView tvInterestRate;
    @BindView(R.id.llRightNext)
    View llRightNext;
    @BindView(R.id.tvRightTimer)
    TextView tvRightTimer;
    @BindView(R.id.ivRightNext)
    View ivRightNext;
//    @BindView(R.id.llRise)
//    LinearLayout llRise;
//    @BindView(R.id.llFall)
//    LinearLayout llFall;

    private Activity mActivity;
    private MainContract.View mView;
    private IRightMenu mRightMenu;
    private ProductEntity mProductEntity;//当前产品
    private IssueEntity mIssueEntity;//当前期号

    private int sept = 2;//金额步长
    private int minMoney = 10;//最少投资金额
    private int maxMoney = 400;//最多投资金额

    private CountDownTimer timer;//倒计时
    private long serviceTimer;//服务器时间

    public RightMenu(Activity activity, MainContract.View view, IRightMenu rightMenu) {
        this.mActivity = activity;
        this.mView = view;
        this.mRightMenu = rightMenu;
    }

    @Override
    public RightMenu onInit(View rootView) {
        super.onInit(rootView);
//        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if (RightMenu.this.rootView.getWidth() <= 0) return;
//                Context context = RightMenu.this.rootView.getContext();
//                RightMenu.this.rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                int width = RightMenu.this.rootView.getWidth() - PhoneUtil.dip2px(RightMenu.this.rootView.getContext(), 1);
//                width = width - (context.getResources().getDimensionPixelOffset(R.dimen.dp_5) * 2);
//                width = width - (context.getResources().getDimensionPixelOffset(R.dimen.dp_2) * 2);
//
//                float item = width / 4;
//                int hight = context.getResources().getDimensionPixelOffset(R.dimen.dp_47);
//                if (item < hight) {
//                    hight = (int) item;
//                }
//                ViewGroup.LayoutParams paramsAdd = ivMoneyAdd.getLayoutParams();
//                paramsAdd.width = (int) item;
//                paramsAdd.height = hight;
//                ivMoneyAdd.setLayoutParams(paramsAdd);
//
//                ViewGroup.LayoutParams paramsReduce = ivMoneyReduce.getLayoutParams();
//                paramsReduce.width = (int) item;
//                paramsReduce.height = hight;
//                ivMoneyReduce.setLayoutParams(paramsReduce);
//
//                ViewGroup.LayoutParams paramsEditBg = ivEditBg.getLayoutParams();
//                paramsEditBg.width = (int) (item * 2);
//                paramsEditBg.height = hight;
//                ivEditBg.setLayoutParams(paramsEditBg);
//
//                ViewGroup.LayoutParams paramsEdit = tvInvestmentMoney.getLayoutParams();
//                paramsEdit.width = paramsEditBg.width;
//                paramsEdit.height = hight;
//                tvInvestmentMoney.setLayoutParams(paramsEdit);
//            }
//        });
        EventDistribution.getInstance().addPurchase(this);
        EventDistribution.getInstance().addProduct(this);
        EventDistribution.getInstance().addIssue(this);
        setMoney(10);
        return this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventDistribution.getInstance().removePurchase(this);
        EventDistribution.getInstance().removeProduct(this);
        EventDistribution.getInstance().removeIssue(this);
    }

    @OnClick({R.id.ivMoneyAdd, R.id.ivMoneyReduce, R.id.ivRise, R.id.ivFall, R.id.ivEditBg, R.id.ivRightNext})
    public void onViewClicked(View view) {
        if (BtnClickUtil.isFastDoubleClick(view.getId())) {
            //防止双击
            return;
        }
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
            case R.id.ivEditBg://打开键盘
                openKeyboardPopupWindow();
                break;
            case R.id.ivRightNext://下一期
                stopCountDown();
                buttonChecked(false);
                mView.refreshIessue();//刷新期号
                break;
        }
    }

    private KeyboardPopupWindow popupWindow;

    private void openKeyboardPopupWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
            return;
        }
        int[] location = new int[2];
        rlEdit.getLocationInWindow(location);
        int y1 = location[1];
        ivRise.getLocationInWindow(location);
        popupWindow = new KeyboardPopupWindow(mActivity, rlEdit.getWidth(), location[1] - y1);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                int money = getMoney();
                setMoney(money);
            }
        });
        popupWindow.setOnKeyListener(new KeyboardPopupWindow.IKeyListener() {
            @Override
            public void onKey(int key) {
                String tag = tvInvestmentMoney.getTag() + "" + key;
                updateMoney(tag);
            }

            @Override
            public void onOk() {
                popupWindow.dismiss();
                popupWindow = null;
            }

            @Override
            public void onDelete() {
                String tag = tvInvestmentMoney.getTag() + "";
                if (TextUtils.isEmpty(tag)) return;
                tag = tag.substring(0, tag.length() - 1);
                updateMoney(tag);
            }
        });
        popupWindow.showAsDropDown(rlEdit);
    }

    private int getMoney() {
        String tag = tvInvestmentMoney.getTag() + "";
        int money = 0;
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
        updateInterestRate(money);
    }

    private void updateMoney(String tag) {
        tvInvestmentMoney.setTag(tag);
        int money = getMoney();
        if (money > maxMoney) money = maxMoney;
        else if (money < 0) money = 0;
        tvInvestmentMoney.setText(String.format(Locale.CHINA, "￥%s", money));
        tvInvestmentMoney.setTag(money);
    }

    private void updateInterestRate(int money) {
        if (mProductEntity == null) {
            tvProfit.setText(String.format(Locale.CANADA, "￥%s", money));
            return;
        }
        float sy = money;
        sy += sy * mProductEntity.getExpects() / 100;
        tvProfit.setText(String.format(Locale.CANADA, "￥%s", NumberUtil.digitFloatStr2(sy)));
    }

    private void rise() {
        if (mView.isRefrshChartData()) return;
        //看涨
        mRightMenu.rise(getMoney());
    }

    private void fall() {
        if (mView.isRefrshChartData()) return;
        //看跌
        mRightMenu.fall(getMoney());
    }

    @Override
    public void productChecked(ProductEntity entity) {
        mProductEntity = entity;
        if (entity != null)
            tvInterestRate.setText(String.format(Locale.CANADA, "收益率%s%s", entity.getExpects(), "%"));
        else tvInterestRate.setText("收益率-%");
        setMoney(getMoney());
    }

    private void stopCountDown() {
        if (timer == null) return;
        timer.cancel();
        timer = null;
    }

    private void startCountDown(final long l) {
        stopCountDown();
        timer = new CountDownTimer(l, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvRightTimer.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                buttonChecked(false);
            }
        };
        timer.start();
    }

    private void buttonChecked(boolean isNext) {
        if (isNext) {
            ivRise.setVisibility(View.GONE);
            ivFall.setVisibility(View.GONE);
            llRightNext.setVisibility(View.VISIBLE);
            if (mIssueEntity != null) {
                long end = TimerUtil.timerToLong(mIssueEntity.getStopTime());
                long open = TimerUtil.timerToLong(mIssueEntity.getBonusTime());
                long d = open - end;
                if (d > 0) {
                    startCountDown(d);
                }
            }
        } else {
            ivRise.setVisibility(View.VISIBLE);
            ivFall.setVisibility(View.VISIBLE);
            llRightNext.setVisibility(View.GONE);
            stopCountDown();//停止倒计时
        }
    }

    @Override
    public void stopPurchase(boolean isOrder) {
        if (!isOrder) return;
        buttonChecked(true);
    }

    @Override
    public void openPrize(boolean isOrder) {

    }


    @Override
    public void issueChecked(IssueEntity entity) {
        mIssueEntity = entity;
    }
}
