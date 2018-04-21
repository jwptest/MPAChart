package com.finance.ui.main;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.finance.R;
import com.finance.base.BaseActivity;
import com.finance.ben.PurchaseViewEntity;
import com.finance.interfaces.ILineChartDataSetting;
import com.finance.interfaces.ILineChartListener;
import com.finance.interfaces.IViewHandler;
import com.finance.linechartdata.LineChartDataSetting;
import com.finance.linechartview.BaseAxisValueFormatter;
import com.finance.linechartview.LineChartSetting;
import com.finance.linechartview.XAxisValueFormatter;
import com.finance.listener.LineChartListener;
import com.finance.utils.PhoneUtil;
import com.finance.utils.StatusBarUtil;
import com.finance.utils.ViewUtil;
import com.finance.widget.linechart.MLineChart;
import com.github.mikephil.charting.data.Entry;

import butterknife.BindView;

/**
 * 首页
 */
public class MainChartActivity extends BaseActivity implements MainContract.View, IRightMenu {

    @BindView(R.id.rlTitleBar)
    View rlTitleBar;
    @BindView(R.id.llLeftMenu)
    View llLeftMenu;
    @BindView(R.id.llRightMenu)
    View llRightMenu;
    @BindView(R.id.llCentreMenu)
    View llCentreMenu;
    @BindView(R.id.rlZst)
    View rlZst;
    @BindView(R.id.lineChart)
    MLineChart lineChart;
    @BindView(R.id.ivIcon)
    ImageView ivIcon;
    @BindView(R.id.vEndLine)
    View vEndLine;
    @BindView(R.id.tvEndLineDes)
    TextView tvEndLineDes;
    @BindView(R.id.ivEndLineIcon)
    ImageView ivEndLineIcon;
    @BindView(R.id.vSettlementLine)
    View vSettlementLine;
    @BindView(R.id.tvSettlementDes)
    TextView tvSettlementDes;
    @BindView(R.id.ivSettlementIcon)
    ImageView ivSettlementIcon;
    @BindView(R.id.vTransverseContrast)
    View vTransverseContrast;
    @BindView(R.id.tvTransverseContrastDes)
    TextView tvTransverseContrastDes;
    @BindView(R.id.ivExitLogin)
    ImageView ivExitLogin;
    @BindView(R.id.ivKM)
    ImageView ivKM;
    @BindView(R.id.llMoney)
    View llMoney;
    @BindView(R.id.llTimer)
    View llTimer;
    @BindView(R.id.ivRefresh)
    ImageView ivRefresh;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvUName)
    TextView tvUName;
    @BindView(R.id.ivUHead)
    ImageView ivUHead;
    @BindView(R.id.ivTransaction)
    ImageView ivTransaction;
    @BindView(R.id.tvTransaction)
    TextView tvTransaction;
    @BindView(R.id.ivOrder)
    ImageView ivOrder;
    @BindView(R.id.tvOrder)
    TextView tvOrder;
    @BindView(R.id.ivDynamic)
    ImageView ivDynamic;
    @BindView(R.id.tvDynamic)
    TextView tvDynamic;
    @BindView(R.id.ivMoneyAdd)
    ImageView ivMoneyAdd;
    @BindView(R.id.tvInvestmentMoney)
    TextView tvInvestmentMoney;
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

    //    private ILineChartSetting chartSetting;
    private ILineChartListener chartListener;
    private ILineChartDataSetting dataSetting;
    private IViewHandler leftMenu, rightMenu, centreMenu;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_line_chart;
    }

    @Override
    protected void onCreated() {
        initView();
        //初始化参数
        PurchaseViewEntity.initValue(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dataSetting != null) dataSetting.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dataSetting != null) dataSetting.onStop();
    }

    @Override
    protected void onDestroy() {
        if (dataSetting != null) dataSetting.onDestroy();
        if (chartListener != null) chartListener.onDestroy();
        if (leftMenu != null) leftMenu.onDestroy();
        if (rightMenu != null) rightMenu.onDestroy();
        if (centreMenu != null) centreMenu.onDestroy();
        super.onDestroy();
    }

    //初始化控件
    private void initView() {
        if (StatusBarUtil.StatusBarNormalMode(this) != 0) {
            int statusBarHigh = PhoneUtil.getStatusBarHigh(this);
            rlTitleBar.setPadding(rlTitleBar.getLeft(), rlTitleBar.getPaddingTop() + statusBarHigh,
                    rlTitleBar.getPaddingRight(), rlTitleBar.getPaddingBottom());
            //设置titleBar的高度
            rlTitleBar.getLayoutParams().height = statusBarHigh + getResources().getDimensionPixelOffset(R.dimen.home_titleBar_hight);
        }
        //右边轴value显示格式类
        BaseAxisValueFormatter mRightAxisValue = new BaseAxisValueFormatter();
        BaseAxisValueFormatter mXAxisValue = new XAxisValueFormatter();
//        lineChart.setVisibility(View.GONE);
        new LineChartSetting(this, lineChart)
                .setRightIAxisValueFormatter(mRightAxisValue)
                .setXIAxisValueFormatter(mXAxisValue)
                .initLineChart();
        dataSetting = new LineChartDataSetting(this, lineChart)
                .onInit();
        chartListener = new LineChartListener(this, lineChart)
                .initListener()
                .setIvIcon(ivIcon)
                .setEndLine(vEndLine)
                .setTvEndLineDes(tvEndLineDes)
                .setIvEndLineIcon(ivEndLineIcon)
                .setTransverseContrast(vTransverseContrast)
                .setTvTransverseContrastDes(tvTransverseContrastDes)
                .setSettlementLine(vSettlementLine)
                .setTvSettlementDes(tvSettlementDes)
                .setIvSettlementIcon(ivSettlementIcon)
                .setRightAxisValueFormatter(mRightAxisValue)
                .setXAxisValueFormatter(mXAxisValue);
        leftMenu = new LeftMenu(this).onInit(llLeftMenu);
        rightMenu = new RightMenu(this).onInit(llRightMenu);
        centreMenu = new CentreMenu().onInit(llCentreMenu);
        //设置背景图片
        setBackground(rlTitleBar, R.drawable.title_bg);
        setBackground(llRightMenu, R.drawable.right_bg);
        setBackground(llLeftMenu, R.drawable.left_menu_bg);
        setBackground(rlZst, R.drawable.zst_bg);

        initLayoutParam();
    }

    private void initLayoutParam() {
        //布局比例 1:7.6:2
        float mWidth = PhoneUtil.getScreenWidth(mActivity);
        float item = mWidth / 10.6f;
        llLeftMenu.getLayoutParams().width = (int) item;//左菜单栏
        ivExitLogin.getLayoutParams().width = (int) item;//title退出登录按钮
        rlZst.getLayoutParams().width = (int) (item * 7.6f);//走势图
        //右边菜单栏
        llRightMenu.getLayoutParams().width = (int) (item * 2) + PhoneUtil.dip2px(mActivity, 1);
        //设置中间菜单
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llCentreMenu.getLayoutParams();
        //距离左菜单栏20dp
        params.leftMargin = llLeftMenu.getLayoutParams().width + PhoneUtil.dip2px(mActivity, 20);
        //距离右菜单栏40dp
        params.rightMargin = llRightMenu.getLayoutParams().width + PhoneUtil.dip2px(mActivity, 40);

//        rlZst.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if (rlZst.getHeight() <= 0) return;
//                rlZst.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llCentreMenu.getLayoutParams();
//                params.topMargin = rlZst.getHeight() - PhoneUtil.dip2px(mActivity, 30) - getResources().getDimensionPixelOffset(R.dimen.home_centre_hight);
//                llCentreMenu.setLayoutParams(params);
//            }
//        });
    }

    private void setBackground(final View view, int drawableId) {
        Glide.with(this)
                .load(drawableId)
                .asBitmap()
                .skipMemoryCache(true)
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        view.setBackground(drawable);
                    }
                });
    }

    @Override
    public void rise(int money) {
        purchase(money, true);
    }

    @Override
    public void fall(int money) {
        purchase(money, false);
    }

    //购买
    private void purchase(int money, boolean isAdd) {
        Entry entry = chartListener.getCurrentEntry();
        float x = entry.getX();
        float y = entry.getY();
        PurchaseViewEntity viewEntity = ViewUtil.getPurchase(mActivity, money + "", isAdd);
        viewEntity.setxValue(x);
        viewEntity.setyValue(y);
        viewEntity.setMoney(money);
        chartListener.addPurchaseView(viewEntity);
    }


}
