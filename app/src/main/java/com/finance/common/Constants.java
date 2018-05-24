package com.finance.common;

import android.util.Log;

import com.finance.model.ben.IndexMarkEntity;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * 公共常量
 */
public class Constants {

    public static final String HTTPURL = "https://i.api789.top:8080/indexMark";//请求地址

    public static long SERVERCURRENTTIMER = 0;//服务器当前时间

    public static final int XANIMATION = 0;//x轴动画时间

    public static final int CHART_LINEFILL = 101;//填充折线图
    public static final int CHART_LINE = 102;//非填充折线图
    public static final int CHART_CANDLE = 103;//蜡烛图
    public static final int CHART_STICK = 104;//棒状图

    public static final int DEVICETYPE = 1;//设备类型
    public static final int PLATFORMID = 9;//设备类型

    public static final int SUCCESSCODE = 0;//请求成功code
    public static final int DEFAULTCODE = -1;//请求成功code

    public static int INDEXDIGIT = 6;//指数

    public static final int ISSUEINTERVAL = 500;//每一期间隔时间

    public static final int WIFI = 1;//wifi网络
    public static final int MOBILE = 2;//移动网络
    public static final int ALLNET = 3;//wifi网络
    public static final int UNNET = 4;//未知网络,没有网络


//    NormalData: 200, //普通数据请求
//    RateData: 0, //指数动作代码
//    TimeRateData: 20, //历史段指数请求
//    CancelData: 10, //取消指数请求动作
//    PrizePoint: 12,
//    IssueData: 300,//期号
//    OpenOrder: 500,//开奖
//    IssueBounusNotify: 550,//每一期开奖
//    IssuePrized: 900,
//    AutoRegister: 1101//自动注册

}
