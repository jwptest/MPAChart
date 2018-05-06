package com.finance.common;

import android.util.Log;

import com.finance.model.ben.IndexMarkEntity;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * 公共常量
 */
public class Constants {

    public static final String HTTPURL = "https://i.api789.top:8080/indexMark";//请求地址

    public static final int XANIMATION = 0;//x轴动画时间

    public static final String CHART_LINEFILL = "CHART_LINEFILL";//填充折线图
    public static final String CHART_LINE = "CHART_LINE";//非填充折线图
    public static final String CHART_CANDLE = "CHART_CANDLE";//蜡烛图

    public static final int DEVICETYPE = 1;//设备类型
    public static final int PLATFORMID = 9;//设备类型

    public static final int SUCCESSCODE = 0;//请求成功code
    public static final int DEFAULTCODE = -1;//请求成功code


    public static final int INDEXDIGIT = 6;//指数

    private static int sReferenceX = 0;
    public static final int ISSUEINTERVAL = 500;//每一期间隔时间

    public static void setReferenceX(long x) {
        Log.d("123", "setReferenceX: " + x);
        sReferenceX = -1;
    }

    //获取数据的下标
    public static int getIndexX(long x) {
        return sReferenceX += 1;
//        Log.d("123", "getIndexX: " + x);
//        x = x - sReferenceX;
//        if (x <= 0) return 0;
//        return (int) x;//(int) (x / ISSUEINTERVAL);
    }

}
