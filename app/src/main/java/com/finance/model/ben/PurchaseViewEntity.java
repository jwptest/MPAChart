package com.finance.model.ben;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.finance.R;

/**
 * 购买布局实体
 */
public class PurchaseViewEntity {

    public static int viewHeight;//布局的高度
    public static int iconWidth;//icon的宽度
    public static int moneyTextWidth;//显示money控件的宽度
    public static int leftWidth;//图标距离左边框的距离

    /**
     * 初始化购买点的参数
     */
    public static void initValue(Context context) {
        Resources resources = context.getResources();
        viewHeight = resources.getDimensionPixelOffset(R.dimen.home_puy_item_height);
        moneyTextWidth = resources.getDimensionPixelOffset(R.dimen.home_puy_item_text_width);
        leftWidth = resources.getDimensionPixelOffset(R.dimen.home_puy_item_icon_left);
        iconWidth = resources.getDimensionPixelOffset(R.dimen.home_puy_item_icon_width);
    }

    //    home_layout_buyingpoint ;//对应布局文件
    private View rootView;
    private View line;//水平线
    private ImageView ivZD;//涨跌图标
    private TextView tvBuyingMone;//金额显示图标
    private float xValue;//当前点的x和Y轴的值
    private float yValue;
    private boolean isDisplay = false;//是否显示在布局上
    //    private boolean isShow = true;//是否需要显示在布局上
    private RelativeLayout.LayoutParams mRootParams;
    private RelativeLayout.LayoutParams mMoneyParams;
    private RelativeLayout.LayoutParams mIconParams;

    private String id;//购买点的唯一标识
    private int money;//当前点购买的金额
    private String indexMark;//购买指数
    private long openTimer;//开奖时间
    private int ProductId;//购买的产品Id
    private int Expects;//预计收益
    private String CreateTime;//订单创建时间
    private String Issue;//购买起期数
    private boolean Result;//涨跌

    public PurchaseViewEntity copy() {
        PurchaseViewEntity entity = new PurchaseViewEntity();
        entity.id = id;
        entity.money = money;
        entity.indexMark = indexMark;
        entity.openTimer = openTimer;
        entity.ProductId = ProductId;
        entity.Expects = Expects;
        entity.CreateTime = CreateTime;
        entity.Issue = Issue;
        entity.Result = Result;
        return entity;
    }


    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean result) {
        Result = result;
    }

    public String getIssue() {
        return (Issue != null) ? Issue : "";
    }

    public void setIssue(String issue) {
        Issue = issue;
    }

    public String getCreateTime() {
        return (CreateTime != null) ? CreateTime : "";
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public int getExpects() {
        return Expects;
    }

    public void setExpects(int expects) {
        Expects = expects;
    }

    public long getOpenTimer() {
        return openTimer;
    }

    public void setOpenTimer(long openTimer) {
        this.openTimer = openTimer;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getIndexMark() {
        return (indexMark != null) ? indexMark : "";
    }

    public void setIndexMark(String indexMark) {
        this.indexMark = indexMark;
    }

//    public boolean isShow() {
//        return isShow;
//    }
//
//    public void setShow(boolean show) {
//        isShow = show;
//    }

    public RelativeLayout.LayoutParams getIconParams() {
        return mIconParams;
    }

    public void setIconParams(RelativeLayout.LayoutParams iconParams) {
        mIconParams = iconParams;
    }

    public RelativeLayout.LayoutParams getRootParams() {
        return mRootParams;
    }

    public void setRootParams(RelativeLayout.LayoutParams rootParams) {
        mRootParams = rootParams;
    }

    public RelativeLayout.LayoutParams getMoneyParams() {
        return mMoneyParams;
    }

    public void setMoneyParams(RelativeLayout.LayoutParams moneyParams) {
        mMoneyParams = moneyParams;
    }

    public boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(boolean display) {
        isDisplay = display;
    }

    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public View getLine() {
        return line;
    }

    public void setLine(View line) {
        this.line = line;
    }

    public ImageView getIvZD() {
        return ivZD;
    }

    public void setIvZD(ImageView ivZD) {
        this.ivZD = ivZD;
    }

    public TextView getTvBuyingMone() {
        return tvBuyingMone;
    }

    public void setTvBuyingMone(TextView tvBuyingMone) {
        this.tvBuyingMone = tvBuyingMone;
    }

    public float getxValue() {
        return xValue;
    }

    public void setxValue(float xValue) {
        this.xValue = xValue;
    }

    public float getyValue() {
        return yValue;
    }

    public void setyValue(float yValue) {
        this.yValue = yValue;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getId() {
        return (id != null) ? id : "";
    }

    public void setId(String id) {
        this.id = id;
    }
}
