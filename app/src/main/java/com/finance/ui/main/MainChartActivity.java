package com.finance.ui.main;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.finance.App;
import com.finance.R;
import com.finance.base.BaseActivity;
import com.finance.common.Constants;
import com.finance.common.NetWorkStateReceiver;
import com.finance.common.UserCommon;
import com.finance.common.UserShell;
import com.finance.event.EventBus;
import com.finance.event.IndexEvent;
import com.finance.event.NetWorkStateEvent;
import com.finance.event.OpenPrizeDialogEvent;
import com.finance.event.UpdateUserInfoEvent;
import com.finance.event.UserLoginEvent;
import com.finance.interfaces.ICallback;
import com.finance.interfaces.IChartData;
import com.finance.interfaces.IChartListener;
import com.finance.interfaces.IChartSetting;
import com.finance.interfaces.IDismiss;
import com.finance.interfaces.IViewHandler;
import com.finance.linechartdata.CandleChartData;
import com.finance.linechartdata.LineChartData1;
import com.finance.linechartview.BaseAxisValueFormatter;
import com.finance.linechartview.LineChartSetting;
import com.finance.linechartview.XAxisValueFormatter;
import com.finance.listener.EventDistribution;
import com.finance.listener.LineChartListener;
import com.finance.listener.OpenCountDown;
import com.finance.model.ben.HistoryIssueEntity;
import com.finance.model.ben.IndexMarkEntity;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.PlaceOrderEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.model.ben.PurchaseViewEntity;
import com.finance.model.ben.UserInfoEntity;
import com.finance.ui.dialog.ExitAppDialog;
import com.finance.ui.dialog.StartDialog;
import com.finance.ui.popupwindow.OpenPrizePopWindow;
import com.finance.utils.BtnClickUtil;
import com.finance.utils.HandlerUtil;
import com.finance.utils.NetWorkUtils;
import com.finance.utils.PhoneUtil;
import com.finance.utils.StatusBarUtil;
import com.finance.utils.TimerUtil;
import com.finance.utils.ViewUtil;
import com.finance.widget.combinedchart.MCombinedChart;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页
 */
public class MainChartActivity extends BaseActivity implements MainContract.View, IRightMenu, ICentreMenu, OpenCountDown.ICallback {

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
    @BindView(R.id.ivMoneyIcon)
    ImageView ivMoneyIcon;
    @BindView(R.id.llTimer)
    View llTimer;
    @BindView(R.id.ivTimerIcon)
    ImageView ivTimerIcon;
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
    @BindView(R.id.rlPurchaseView)
    ViewGroup rlPurchaseView;
    @BindView(R.id.vAnimation)
    View vAnimation;

    //    @BindView(R.id.llRise)
//    LinearLayout llRise;
//    @BindView(R.id.llFall)
//    LinearLayout llFall;
    //    private ILineChartSetting chartSetting;
    private IChartListener chartListener;
    private IViewHandler leftMenu, rightMenu, centreMenu;
    private MainContract.Presenter mMainPresenter;
    private boolean isNetWork = true;//是否有网络连接

    private boolean isResume;
    //    private boolean isNextIssue = false;//是否点击了下一期
    //数据处理接口
    private IChartData dataSetting;
    //走势图参数配置接口
    private IChartSetting mChartSetting;
    //数据处理
    private LineChartData1 mLineChartData;//折线图
    private CandleChartData mCandleData;//蜡烛图
    private int chartType;//当前显示图像类型
    private NetWorkStateReceiver mNetWorkStateReceiver;//网络改变监听
    private ArrayList<ProductEntity> mProductEntities;
    private ArrayList<IssueEntity> mIssueEntities;
    private ProductEntity currentProduct;//当前显示的产品
    private ArrayList<IssueEntity> currentIssues;//当前可供选折的期号
    private IssueEntity currentIssue;//当前选中的期号
    private Animation animation;//更新余额动画
    private int issuesSelectIndex = 0;
    private int statusBarHigh = 0;
    private boolean isConnectService = false;//是否连接服务器成功

    @Override
    protected int getLayoutId() {
        return R.layout.activity_line_chart;
    }

    @Override
    protected void onCreated() {
        EventBus.register(this);
        mMainPresenter = new MainPresenter(mActivity, this);
        initView();
        isNetWork = NetWorkUtils.isNetworkConnected();
        //初始化参数
        PurchaseViewEntity.initValue(this);
        StartDialog mDialog = new StartDialog(this, R.drawable.start_bg);
        mDialog.show();
//        int a = ViewConfiguration.get(mActivity).getScaledDoubleTapSlop();//双击的最大间距
//        int b = ViewConfiguration.get(mActivity).getScaledTouchSlop();//移动的最小距离
//        Log.d("123", "最小滑动距离:" + a + ",:" + b);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNetWorkStateReceiver = NetWorkStateReceiver.registerReceiver(mActivity);
        isResume = true;
        if (dataSetting != null) dataSetting.onResume(chartType);
        OpenCountDown.getInstance().addCallback(this);
        if (isConnectService) {
            refreshData();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        OpenCountDown.getInstance().removeCallback(this);
        if (dataSetting != null) dataSetting.onDestroy(0);
        if (mNetWorkStateReceiver != null) {
            NetWorkStateReceiver.unregisterReceiver(mActivity, mNetWorkStateReceiver);
            mNetWorkStateReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (dataSetting != null) dataSetting.onDestroy(0);
        if (chartListener != null) chartListener.onDestroy();
        if (leftMenu != null) leftMenu.onDestroy();
        if (rightMenu != null) rightMenu.onDestroy();
        if (centreMenu != null) centreMenu.onDestroy();
        OpenCountDown.getInstance().removeCallback(this);
        EventBus.unregister(this);
        if (mNetWorkStateReceiver != null) {
            NetWorkStateReceiver.unregisterReceiver(mActivity, mNetWorkStateReceiver);
            mNetWorkStateReceiver = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        new ExitAppDialog(this).show();
    }

    private void refreshData() {
        mProductEntities = null;//产品
        mIssueEntities = null;//所有期号
        currentProduct = null;//当前显示的产品
        currentIssues = null;//当前可供选折的期号
        currentIssue = null;//当前选中的期号
        //刷新产品、期号、走势图
        mMainPresenter.getProduct();
    }

    //初始化控件
    private void initView() {
        if (StatusBarUtil.StatusBarNormalMode(this) != 0) {
            statusBarHigh = PhoneUtil.getStatusBarHigh(this);
//            rlTitleBar.setPadding(rlTitleBar.getLeft(), rlTitleBar.getPaddingTop() + statusBarHigh,
//                    rlTitleBar.getPaddingRight(), rlTitleBar.getPaddingBottom());
            //设置titleBar的高度
            rlTitleBar.getLayoutParams().height = statusBarHigh + getResources().getDimensionPixelOffset(R.dimen.home_titleBar_height);
        }
        //右边轴value显示格式类
        BaseAxisValueFormatter mRightAxisValue = new BaseAxisValueFormatter(Constants.INDEXDIGIT);
        XAxisValueFormatter mXAxisValue = new XAxisValueFormatter();
//        lineChart.setVisibility(View.GONE);
        mChartSetting = new LineChartSetting(mActivity);
        mChartSetting
                .setRightIAxisValueFormatter(mRightAxisValue)
                .setXIAxisValueFormatter(mXAxisValue)
                .initLineChart(lineChart, true);
        chartListener = new LineChartListener(mActivity, rlPurchaseView, this, lineChart)
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
        chartListener.hideView();//默认隐藏控件
        leftMenu = new LeftMenu(mActivity, this, mMainPresenter).onInit(llLeftMenu);
        rightMenu = new RightMenu(mActivity, this, this).onInit(llRightMenu);
        centreMenu = new CentreMenu(this, mMainPresenter).onInit(llCentreMenu);
        //设置数据处理
        checkedChart(Constants.CHART_LINEFILL);//当前显示的走势图类型

        //设置背景图片
        ViewUtil.setBackground(mActivity, rlTitleBar, R.drawable.title_bg);
        ViewUtil.setBackground(mActivity, llRightMenu, R.drawable.right_bg);
        ViewUtil.setBackground(mActivity, llLeftMenu, R.drawable.left_menu_bg);
        ViewUtil.setBackground(mActivity, rlZst, R.drawable.zst_bg);
        ViewUtil.setBackground(mActivity, vAnimation, R.drawable.zst_bg);
        initLayoutParam();
        initViewUser();
    }

    //获取数据处理类
    private IChartData getChartData(int type) {
        IChartData dataSetting = null;
        if (type == Constants.CHART_LINEFILL || type == Constants.CHART_LINE) {
            if (mLineChartData == null) {
                mLineChartData = new LineChartData1(mActivity, this, lineChart, mMainPresenter, vAnimation);
            }
            dataSetting = mLineChartData;
        } else if (type == Constants.CHART_CANDLE) {
            //蜡烛图
            if (mCandleData == null) {
                mCandleData = new CandleChartData(this, lineChart);
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
        chartListener.setOtherIssue(issuesSelectIndex == 0);
        chartListener.updateProductIssue(currentProduct, currentIssue, currentIssues.get(0));//更新产品和期号
        dataSetting.updateIssue(currentProduct, currentIssue);//更新产品和期号
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

    @Override
    public void placeOrder(int money, boolean Result) {
        IndexMarkEntity entry = dataSetting.getCurrentEntry();
        if (entry == null || currentProduct == null || currentIssue == null) return;
        mMainPresenter.placeOrder(currentIssue.getIssueName(), currentIssue.getIssueType(), money, currentProduct.getProductId(), Result, entry.getId());
//        purchase(money, false);
    }

    @Override
    public void checkedChart(int chartType) {
        if (this.chartType == chartType) {
            return;
        }
        if (dataSetting != null) {
            dataSetting.onDestroy(chartType);
        }
        this.chartType = chartType;
        dataSetting = getChartData(chartType);
//        updateIssue();//更新走势图
    }

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        if (event == null) return;
        initViewUser();
        isConnectService = true;
        refreshData();//获取产品信息
    }

    @Subscribe
    public void onEvent(IndexEvent event) {
        if (event == null) return;//获取开奖指数事件
        mMainPresenter.getOpenIndex(currentProduct.getProductId(), currentProduct.getProductName(), currentIssue.getIssueName(), TimerUtil.formatStr(currentIssue.getBonusTime()));
    }

    @Subscribe
    public void onEvent(OpenPrizeDialogEvent event) {
//        if (!isNextIssue) {
//            isNextIssue = false;
//        }
        refreshIessue();//刷新期号
    }

//    @Subscribe
//    public void onEvent(OpenPrizeEvent event) {
//        if (event == null) return;//获取开奖结果事件
//    }

    @Subscribe
    public void onEvent(UpdateUserInfoEvent event) {
        if (event == null) return;
        UserCommon.getUserInfo(mActivity, new ICallback<UserInfoEntity>() {
            @Override
            public void onCallback(int code, UserInfoEntity userInfoEntity, String message) {
                ivRefresh.clearAnimation();//停止动画
                if (userInfoEntity == null) return;
                initViewUser();//刷新用户信息
                if (!event.isTip()) return;
                App.getInstance().showErrorMsg(event.getMsg());
            }
        });//刷新用户信息
    }

    private Runnable mRunnable;

    @Subscribe
    public void onEvent(NetWorkStateEvent event) {
        if (event == null) return;
        if (this.isNetWork == event.isNetWork()) {
            return;
        }
        this.isNetWork = event.isNetWork();//更新状态
        if (mRunnable != null) {
            HandlerUtil.removeRunable(mRunnable);
        }
        Log.d("123", event.isNetWork() + "");
        if (!event.isNetWork()) {
            App.getInstance().showErrorMsg("网络断开！");
            dataSetting.stopNetwork();//关闭网络连接
            return;
        }
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mRunnable = null;
                refreshData();//获取产品信息
            }
        };
        HandlerUtil.runOnUiThreadDelay(mRunnable, 500);

//        if (event.isNetWork()) {
//            refreshData();//获取产品信息
//        } else {
//            dataSetting.stopNetwork();//关闭网络连接
//        }
//        boolean isNetWork = event.isNetWork();
//        if (!isNetWork) {//没有网络
//            if (dataSetting != null && this.isNetWork) dataSetting.stopNetwork();//关闭网络连接
//        } else {//当前网络为连接，上次状态也为连接不处理
//            if (!this.isNetWork) {//网络重新连接上，刷新数据
//                refreshData();//获取产品信息
//            }
//        }
//        this.isNetWork = isNetWork;//更新状态
//        Toast.makeText(this, event.getState() + "," + event.isNetWork() + "," + this.isNetWork, Toast.LENGTH_SHORT).show();
    }

    private void initViewUser() {
        UserInfoEntity entity = UserShell.getInstance().getUserInfo();
        Glide.with(mActivity)
                .load(entity.getLogo())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.userhead)
                .placeholder(R.drawable.userhead)
                .into(ivUHead);
        if (entity.getNickName().length() > 15) {
            tvUName.setText(entity.getNickName().substring(0, 15));
        } else {
            tvUName.setText(entity.getNickName());
        }
        tvMoney.setText("¥" + UserShell.getInstance().getUserMoneyStr());
    }

    private void initViewProduct(ProductEntity entity) {
        if (entity == null || entity == currentProduct) return;
        //清空开奖时间
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
        tvJZTimer.setText(TimerUtil.getTimer(entity.getBonusTime()));
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
        issuesSelectIndex = 0;
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
    public int getStatusBarHigh() {
        return statusBarHigh;
    }

    @Override
    public IndexMarkEntity getIndexMarkEntity(String indexMark) {
        return dataSetting.getIndexMarkEntity(indexMark);
    }

    @Override
    public void showOrderPopWindow(IDismiss dismiss) {
        int width = ivKM.getWidth() + llMoney.getWidth();
        mMainPresenter.showOrderPopWindow(rlTitleBar, llLeftMenu, width, rlTitleBar.getHeight() - statusBarHigh, dismiss);
    }

    @Override
    public void showDynamicPopupWindow(IDismiss dismiss) {
        int width = ivKM.getWidth() + llMoney.getWidth();
        mMainPresenter.showDynamicPopWindow(rlTitleBar, llLeftMenu, width, rlTitleBar.getHeight() - statusBarHigh, dismiss);
    }

    @Override
    public ArrayList<PurchaseViewEntity> getPurchase(int productId, String issue) {
        return chartListener.getPurchase(productId, issue);
    }

    @Override
    public void refreshIessueNextIssue() {
//        isNextIssue = true;
        refreshIessue();
    }

    @Override
    public void refreshIessue() {
        mMainPresenter.getProductIssue(mMainPresenter.getProductIds(mProductEntities));
    }

    @Override
    public boolean isRefrshChartData() {
        return dataSetting == null || dataSetting.isRefrshChartData();
    }

    @Override
    public void placeOrder(PlaceOrderEntity entity, String msg) {
        if (entity == null) {
            App.getInstance().showErrorMsg(msg);
            return;
        }
        IndexMarkEntity markEntity = getIndexMarkEntity(entity.getHexIndexMark());
        if (markEntity == null) return;
        int money = (int) entity.getMoney();
        markEntity.setData(entity.getBonusHexIndexMark());//设置当前点指数
        PurchaseViewEntity viewEntity = ViewUtil.getPurchase(mActivity, money + "", entity.isResult());
        viewEntity.setIndexMark(entity.getHexIndexMark());//购买指数
        viewEntity.setMoney(money);//设置金额
        viewEntity.setxValue(markEntity.getX());//设置坐标
        viewEntity.setyValue(markEntity.getY());
        //购买点数据
        viewEntity.setOpenTimer(TimerUtil.timerToLong(entity.getTicks()));//设置开奖时间
        viewEntity.setProductId(entity.getProductId());//设置产品
        viewEntity.setExpects(entity.getExpects());//预计收益
        viewEntity.setIssue(entity.getIssue());//预计收益
        viewEntity.setResult(entity.isResult());//涨跌
        viewEntity.setCreateTime(entity.getCreateTime());//下单时间
        chartListener.addPurchaseView(viewEntity);
        onEvent(new UpdateUserInfoEvent(false));
    }

    private OpenPrizePopWindow prizePopWindow;//开奖对话框

    @Override
    public void openPrizeDialog(HistoryIssueEntity entity, String msg, IndexMarkEntity openIndex, int productId, String issue, String productName) {
        if (entity == null) {
            App.getInstance().showErrorMsg(msg);
            return;
        }
        if (prizePopWindow != null) {
            return;
        }
        int width = PhoneUtil.getScreenWidth(mActivity);
        int height = PhoneUtil.getScreenHeight(mActivity);
        //显示开奖对话框
        prizePopWindow = new OpenPrizePopWindow(mActivity, this, entity, openIndex, productId, issue, productName, width, height);
        prizePopWindow.showPopupWindow(rlTitleBar);
        prizePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                prizePopWindow = null;
            }
        });
    }

    @Override
    public IssueEntity getIssue(int productId, String issue) {
        return mMainPresenter.getIssue(productId, issue, mIssueEntities);
    }

    @Override
    public void setShowOrder(int productId, String issueName) {
        chartListener.setShowOrder(productId, issueName);
    }

    @OnClick({R.id.ivExitLogin, R.id.llMoney, R.id.llTimer, R.id.ivRefresh})
    public void onClickListener(View view) {
        if (BtnClickUtil.isFastDoubleClick(view.getId())) {
            //防止双击
            return;
        }
        switch (view.getId()) {
            case R.id.ivExitLogin:
//                int width = PhoneUtil.getScreenWidth(mActivity);
//                int height = PhoneUtil.getScreenHeight(mActivity);
//                //显示开奖对话框
//                new OpenPrizePopWindow(mActivity, this, mChartSetting, null, null, 0, null, null, width, height).showPopupWindow(rlTitleBar);
                break;
            case R.id.llMoney:
                if (mProductEntities == null || mProductEntities.isEmpty()) return;
                ivMoneyIcon.setImageResource(R.drawable.xialaax);
                int[] location = new int[2];
                llMoney.getLocationOnScreen(location);
                mMainPresenter.showProductPopWindow(llMoney, location[0], rlTitleBar.getHeight() - statusBarHigh, mProductEntities, new IDismiss() {
                    @Override
                    public void onDismiss() {
                        ivMoneyIcon.setImageResource(R.drawable.xiala);
                    }
                });
                break;
            case R.id.llTimer:
                if (currentIssues == null || currentIssues.isEmpty()) return;
                int[] location1 = new int[2];
                llTimer.getLocationOnScreen(location1);
                ivTimerIcon.setImageResource(R.drawable.xialaax);
                mMainPresenter.showIssuePopWindow(llTimer, location1[0], rlTitleBar.getHeight() - statusBarHigh, currentIssues, issuesSelectIndex, new IDismiss() {
                    @Override
                    public void onDismiss() {
                        ivTimerIcon.setImageResource(R.drawable.xiala);
                    }
                });
                break;
            case R.id.ivRefresh:
                if (animation == null) {
                    animation = AnimationUtils.loadAnimation(mActivity, R.anim.animation_refresh_userinfo);
                }
                ivRefresh.startAnimation(animation);
                onEvent(new UpdateUserInfoEvent(true));
                break;
        }
    }

    @Override
    public void startTick() {
        //开始开奖倒计时
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {
//        //结束开奖倒计时
//        //获取开奖结果
//        chartListener.clearPurchaseView();
    }


}
