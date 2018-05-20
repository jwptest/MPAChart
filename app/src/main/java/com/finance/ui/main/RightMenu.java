package com.finance.ui.main;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.finance.R;
import com.finance.base.BaseViewHandle;
import com.finance.event.EventBus;
import com.finance.event.OpenPrizeDialogEvent;
import com.finance.listener.EventDistribution;
import com.finance.listener.OpenCountDown;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.ui.popupwindow.KeyboardPopupWindow;
import com.finance.utils.BtnClickUtil;
import com.finance.utils.NumberUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 右菜单处理
 */
public class RightMenu extends BaseViewHandle implements EventDistribution.IProductChecked, EventDistribution.IPurchase, EventDistribution.IIssueChecked, OpenCountDown.ICallback {

    @BindView(R.id.ivMoneyAdd)
    ImageView ivMoneyAdd;
    @BindView(R.id.tvInvestmentMoney)
    TextView tvInvestmentMoney;
    @BindView(R.id.ivEditBg)
    ImageView ivEditBg;
    @BindView(R.id.rlEdit)
    View rlEdit;
    @BindView(R.id.llRise)
    View llRise;
    @BindView(R.id.llFall)
    View llFall;
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

    private int sept = 10;//金额步长
    private int minMoney = 10;//最少投资金额
    private int maxMoney = 400;//最多投资金额

    private long serviceTimer;//服务器时间
    private boolean isUpdateText = true;
    private boolean isCountDown = false;

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
        OpenCountDown.getInstance().addCallback(this);
        //注册开奖对话框打开事件
        EventBus.register(RightMenu.this);
        setMoney(10);
        return this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventDistribution.getInstance().removePurchase(this);
        EventDistribution.getInstance().removeProduct(this);
        EventDistribution.getInstance().removeIssue(this);
        OpenCountDown.getInstance().removeCallback(this);
        EventBus.unregister(RightMenu.this);
    }

    @OnClick({R.id.ivMoneyAdd, R.id.ivMoneyReduce, R.id.llFall, R.id.llRise, R.id.ivEditBg, R.id.ivRightNext})
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
            case R.id.llRise://涨
                rise();
                break;
            case R.id.llFall://跌
                fall();
                break;
            case R.id.ivEditBg://打开键盘
                openKeyboardPopupWindow();
                break;
            case R.id.ivRightNext://下一期
                isUpdateText = false;
                buttonChecked(false);
                mView.refreshIessueNextIssue();//刷新期号
                mView.setShowOrder(mProductEntity.getProductId(), mIssueEntity.getIssueName());
                break;
        }
    }

    private void openKeyboardPopupWindow() {
        int[] location = new int[2];
        rlEdit.getLocationInWindow(location);
        int y1 = location[1];
        int x1 = location[0];
        llRise.getLocationInWindow(location);
        int dp5 = mActivity.getResources().getDimensionPixelOffset(R.dimen.dp_5);
        int height = location[1] - y1 - rlEdit.getHeight() - dp5 - dp5;
        int y = y1 - mView.getStatusBarHigh() + rlEdit.getHeight();
        final KeyboardPopupWindow popupWindow = new KeyboardPopupWindow(mActivity, rlEdit.getWidth(), height, x1, y + dp5);
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
            }

            @Override
            public void onDelete() {
                String tag = tvInvestmentMoney.getTag() + "";
                if (TextUtils.isEmpty(tag)) return;
                tag = tag.substring(0, tag.length() - 1);
                updateMoney(tag);
            }
        });
        popupWindow.showPopupWindow(rlEdit);
    }

    private int getMoney() {
        String tag = tvInvestmentMoney.getTag() + "";
        int money = 0;
        try {
            money = Integer.parseInt(tag);
        } catch (Exception e) {
//            e.printStackTrace();
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
        mRightMenu.placeOrder(getMoney(), true);
    }

    private void fall() {
        if (mView.isRefrshChartData()) return;
        //看跌
        mRightMenu.placeOrder(getMoney(), false);
    }

    @Override
    public void productChecked(ProductEntity entity) {
        mProductEntity = entity;
        if (entity != null)
            tvInterestRate.setText(String.format(Locale.CANADA, "收益率%s%s", entity.getExpects(), "%"));
        else tvInterestRate.setText("收益率-%");
        setMoney(getMoney());
    }

    private void buttonChecked(boolean isNext) {
        isCountDown = isNext;//倒计时布局是否显示
        if (isNext) {
            llRise.setVisibility(View.GONE);
            llFall.setVisibility(View.GONE);
            llRightNext.setVisibility(View.VISIBLE);
        } else {
            llRise.setVisibility(View.VISIBLE);
            llFall.setVisibility(View.VISIBLE);
            llRightNext.setVisibility(View.GONE);
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

    @Override
    public void startTick() {
        isUpdateText = true;
        buttonChecked(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (isUpdateText)
            tvRightTimer.setText(millisUntilFinished / 1000 + "");
    }

    @Override
    public void onFinish() {
        isUpdateText = false;
        tvRightTimer.setText("0");
//        buttonChecked(false);
    }

    @Subscribe
    public void onEvent(OpenPrizeDialogEvent event) {
        if (isCountDown) {
            buttonChecked(false);
        }
    }

}
