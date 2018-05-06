package com.finance.ui.main;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.finance.R;
import com.finance.base.BaseActivity;
import com.finance.common.Constants;
import com.finance.common.UserShell;
import com.finance.interfaces.IChartData;
import com.finance.interfaces.IChartListener;
import com.finance.interfaces.IDismiss;
import com.finance.interfaces.IViewHandler;
import com.finance.linechartdata.CandleChartData;
import com.finance.linechartdata.LineChartData;
import com.finance.linechartview.BaseAxisValueFormatter;
import com.finance.linechartview.LineChartSetting;
import com.finance.linechartview.XAxisValueFormatter;
import com.finance.listener.EventDistribution;
import com.finance.listener.LineChartListener;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.model.ben.PurchaseViewEntity;
import com.finance.model.ben.UserInfoEntity;
import com.finance.utils.BtnClickUtil;
import com.finance.utils.PhoneUtil;
import com.finance.utils.StatusBarUtil;
import com.finance.utils.ViewUtil;
import com.finance.widget.combinedchart.MCombinedChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页
 */
public class MainChartActivity extends BaseActivity implements MainContract.View, IRightMenu, ICentreMenu {

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
    MCombinedChart lineChart;
    @BindView(R.id.ivIcon)
    ImageView ivIcon;
    @BindView(R.id.vEndLine)
    View vEndLine;
    @BindView(R.id.tvEndLineDes)
    TextView tvEndLineDes;
    //    @BindView(R.id.ivEndLineIcon)
//    ImageView ivEndLineIcon;
    @BindView(R.id.vSettlementLine)
    View vSettlementLine;
    @BindView(R.id.tvSettlementDes)
    TextView tvSettlementDes;
    //    @BindView(R.id.ivSettlementIcon)
//    ImageView ivSettlementIcon;
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
    @BindView(R.id.tvMoneyType)
    TextView tvMoneyType;
    @BindView(R.id.tvBFB)
    TextView tvBFB;
    @BindView(R.id.tvJZTimer)
    TextView tvJZTimer;
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
    private IChartListener chartListener;
    private IViewHandler leftMenu, rightMenu, centreMenu;

    private MainContract.Presenter mMainPresenter;

    private boolean isResume;

    //数据处理接口
    private IChartData dataSetting;
    //数据处理
    private LineChartData mLineChartData;//折线图
    private CandleChartData mCandleData;//蜡烛图
    private String chartType;//当前显示图像类型

    private ArrayList<ProductEntity> mProductEntities;
    private ArrayList<IssueEntity> mIssueEntities;

    private ProductEntity currentProduct;
    private ArrayList<IssueEntity> currentIssues;
    private IssueEntity currentIssue;
    private int issuesSelectIndex = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_line_chart;
    }

    @Override
    protected void onCreated() {
        mMainPresenter = new MainPresenter(mActivity, this);
        initView();
        //初始化参数
        PurchaseViewEntity.initValue(this);
        mMainPresenter.getProduct();//获取产品信息
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
        if (dataSetting != null) dataSetting.onResume(chartType);
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
            rlTitleBar.getLayoutParams().height = statusBarHigh + getResources().getDimensionPixelOffset(R.dimen.home_titleBar_height);
        }
        //右边轴value显示格式类
        BaseAxisValueFormatter mRightAxisValue = new BaseAxisValueFormatter(Constants.INDEXDIGIT);
        BaseAxisValueFormatter mXAxisValue = new XAxisValueFormatter();
//        lineChart.setVisibility(View.GONE);
        new LineChartSetting(this, lineChart)
                .setRightIAxisValueFormatter(mRightAxisValue)
                .setXIAxisValueFormatter(mXAxisValue)
                .initLineChart();
        chartListener = new LineChartListener(this, lineChart)
                .initListener()
                .setIvIcon(ivIcon)
                .setEndLine(vEndLine)
                .setTvEndLineDes(tvEndLineDes)
//                .setIvEndLineIcon(ivEndLineIcon)
                .setTransverseContrast(vTransverseContrast)
                .setTvTransverseContrastDes(tvTransverseContrastDes)
                .setSettlementLine(vSettlementLine)
                .setTvSettlementDes(tvSettlementDes)
//                .setIvSettlementIcon(ivSettlementIcon)
                .setRightAxisValueFormatter(mRightAxisValue)
                .setXAxisValueFormatter(mXAxisValue);
        leftMenu = new LeftMenu(mActivity, this, mMainPresenter).onInit(llLeftMenu);
        rightMenu = new RightMenu(mActivity, this, this).onInit(llRightMenu);
        centreMenu = new CentreMenu(this).onInit(llCentreMenu);
        //设置数据处理
        checkedChart(Constants.CHART_LINEFILL);//当前显示的走势图类型
        //设置背景图片
        setBackground(rlTitleBar, R.drawable.title_bg);
        setBackground(llRightMenu, R.drawable.right_bg);
        setBackground(llLeftMenu, R.drawable.left_menu_bg);
        setBackground(rlZst, R.drawable.zst_bg);

        initLayoutParam();
        initViewUser();
    }

    //获取数据处理类
    private IChartData getChartData(String type) {
        IChartData dataSetting = null;
        if (TextUtils.equals(type, Constants.CHART_LINEFILL) || TextUtils.equals(type, Constants.CHART_LINE)) {
            if (mLineChartData == null) {
                mLineChartData = new LineChartData(mActivity, this, lineChart, mMainPresenter)
                        .onInit();
            }
            dataSetting = mLineChartData;
        } else if (TextUtils.equals(type, Constants.CHART_CANDLE)) {
            //蜡烛图
            if (mCandleData == null) {
                mCandleData = new CandleChartData(this, lineChart)
                        .onInit();
            }
            dataSetting = mCandleData;
        }
        if (isResume && dataSetting != null)
            dataSetting.onResume(type);
        chartListener.setIChartData(dataSetting);
        return dataSetting;
    }

    //更新期号
    private void updateIssue() {
        dataSetting.updateIssue(currentProduct, currentIssue);//更新产品和期号
        chartListener.updateIssue(currentIssue);//更新期号
        EventDistribution.getInstance().issue(currentIssue);
    }

    private void initLayoutParam() {
//        //布局比例 1:7.6:2
//        float mWidth = PhoneUtil.getScreenWidth(mActivity);
//        float item = mWidth / 10.6f;
//        llLeftMenu.getLayoutParams().width = (int) item;//左菜单栏
//        ivExitLogin.getLayoutParams().width = (int) item;//title退出登录按钮
//        rlZst.getLayoutParams().width = (int) (item * 7.6f);//走势图
//        //右边菜单栏
//        llRightMenu.getLayoutParams().width = (int) (item * 2) + PhoneUtil.dip2px(mActivity, 1);
//        //设置中间菜单
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llCentreMenu.getLayoutParams();
//        //距离左菜单栏20dp
//        params.leftMargin = llLeftMenu.getLayoutParams().width + PhoneUtil.dip2px(mActivity, 20);
//        //距离右菜单栏40dp
//        params.rightMargin = llRightMenu.getLayoutParams().width + PhoneUtil.dip2px(mActivity, 40);

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
        if (entry == null) return;
        float x = entry.getX();
        float y = entry.getY();
        PurchaseViewEntity viewEntity = ViewUtil.getPurchase(mActivity, money + "", isAdd);
        viewEntity.setxValue(x);
        viewEntity.setyValue(y);
        viewEntity.setMoney(money);
        chartListener.addPurchaseView(viewEntity);
    }

    @Override
    public void checkedChart(String chartType) {
        if (TextUtils.equals(this.chartType, chartType)) {
            return;
        }
        if (dataSetting != null) {
            dataSetting.onStop();
        }
        this.chartType = chartType;
        dataSetting = getChartData(chartType);
        updateIssue();//更新走势图
    }

    private void initViewUser() {
        UserInfoEntity entity = UserShell.getInstance().getUserInfo();
        Glide.with(mActivity)
                .load(entity.getLogo())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivUHead);
        tvUName.setText(entity.getUserName());
        tvMoney.setText("￥" + UserShell.getInstance().getUserMoneyStr());
    }

    private void initViewProduct(ProductEntity entity) {
        tvMoneyType.setText(entity.getProductName());
        tvBFB.setText(entity.getExpects() + "%");
        currentProduct = entity;
        EventDistribution.getInstance().product(entity);
        if (mIssueEntities == null) {
            refreshIessue();//刷新期号
        } else {
            currentIssues = mMainPresenter.getProductIssue(entity.getProductId(), mIssueEntities);
            initViewIssue(0);
        }
    }

    private void initViewIssue(int selIndex) {
        IssueEntity entity = currentIssues.get(selIndex);
        issuesSelectIndex = selIndex;
        tvJZTimer.setText(mMainPresenter.issueNameFormat(entity.getBonusTime()));
        currentIssue = entity;
        updateIssue();//更新走势图
    }

    @Override
    public void product(ArrayList<ProductEntity> Products, String msg) {
        if (Products == null || Products.isEmpty()) {
            showErrorMsg(msg);
            return;
        }
        mProductEntities = Products;
        initViewProduct(mProductEntities.get(0));
    }

    @Override
    public void issue(ArrayList<IssueEntity> issues, String msg) {
        if (issues == null || issues.isEmpty()) {
            showErrorMsg(msg);
            return;
        }
        mIssueEntities = issues;
        ArrayList<IssueEntity> issueEntities = mMainPresenter.getProductIssue(currentProduct.getProductId(), issues);
        if (issueEntities == null || issueEntities.isEmpty()) return;
        currentIssues = issueEntities;
        initViewIssue(issuesSelectIndex);
    }

    @Override
    public void setProduct(ProductEntity product) {
        if (product == null) return;
        initViewProduct(product);
    }

    @Override
    public void setIssue(IssueEntity issue, int index) {
        if (issue == null) return;
        initViewIssue(index);
    }

    @Override
    public void showOrderPopWindow(IDismiss dismiss) {
        int width = ivKM.getWidth() + llMoney.getWidth();
        mMainPresenter.showOrderPopWindow(rlTitleBar, llLeftMenu, width, null, dismiss);
    }

    @Override
    public void showDynamicPopupWindow(IDismiss dismiss) {
        int width = ivKM.getWidth() + llMoney.getWidth();
        mMainPresenter.showDynamicPopWindow(rlTitleBar, llLeftMenu, width, null, dismiss);
    }

    @Override
    public void refreshIessue() {
        mMainPresenter.getProductIssue(mMainPresenter.getProductIds(mProductEntities));
    }

    @Override
    public boolean isRefrshChartData() {
        return dataSetting == null || dataSetting.isRefrshChartData();
    }

    @OnClick({R.id.ivExitLogin, R.id.llMoney, R.id.llTimer, R.id.ivRefresh})
    public void onClickListener(View view) {
        if (BtnClickUtil.isFastDoubleClick(view.getId())) {
            //防止双击
            return;
        }
        switch (view.getId()) {
            case R.id.ivExitLogin:
                break;
            case R.id.llMoney:
                mMainPresenter.showProductPopWindow(llMoney, mProductEntities);
                break;
            case R.id.llTimer:
                mMainPresenter.showIssuePopWindow(llTimer, currentIssues, issuesSelectIndex);
                break;
            case R.id.ivRefresh:
                break;
        }

    }


}
