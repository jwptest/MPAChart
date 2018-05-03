package com.finance.linechartdata;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.finance.R;
import com.finance.common.Constants;
import com.finance.event.DataRefreshEvent;
import com.finance.event.EventBus;
import com.finance.interfaces.ICallback;
import com.finance.interfaces.IChartData;
import com.finance.model.ben.IndexMarkEntity;
import com.finance.model.ben.IssueEntity;
import com.finance.model.ben.ProductEntity;
import com.finance.model.http.BaseCallback;
import com.finance.model.http.HttpConnection;
import com.finance.ui.main.MainContract;
import com.finance.utils.HandlerUtil;
import com.finance.utils.IndexUtil;
import com.finance.utils.TimerUtil;
import com.finance.widget.combinedchart.MCombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import static android.R.attr.x;

/**
 * 显示数据设置类
 */
public class LineChartData implements IChartData, ICallback<ArrayList<String>> {

    private MCombinedChart mChart;
    private Activity activity;
    private MainContract.Presenter mPresenter;

    private XAxis mXAxis;
    private CombinedData combinedData;
    private LineData lineData;//显示数据
    private ArrayList<Entry> mEntries;
    private boolean isInitData = false;//是否初始化数据
    private boolean isResume = false;
    private boolean isStop = false;//是否停止

    private ProductEntity productEntity;//产品
    private IssueEntity issueEntity;//期号
    private HttpConnection mHttpConnection;
    private Callback mCallback;
    private MThread mMThread;//解析数据线程
    private ArrayList<IndexMarkEntity> mIndexMarkEntities;//推送的指数数据
    private int dpPx10;

    public LineChartData(Activity activity, MCombinedChart chart, MainContract.Presenter presenter) {
        this.activity = activity;
        this.mChart = chart;
        this.mPresenter = presenter;
        mXAxis = mChart.getXAxis();
        mIndexMarkEntities = new ArrayList<>(2);
        mEntries = new ArrayList<>();
        dpPx10 = activity.getResources().getDimensionPixelOffset(R.dimen.dp_10);
    }

    @Override
    public LineChartData onInit() {
        mCallback = new Callback();
        return this;
    }

    @Override
    public void onResume(String type) {
        //X轴显示最大值
//        mXAxis.setAxisMaximum(data.getDataSets().get(0).getEntryCount() + 300);
//        LineDataSet dataSet = (LineDataSet) data.getDataSetByIndex(0);
//        if (TextUtils.equals(type, Constants.CHART_LINEFILL)) {
//            dataSet.setDrawFilled(true);
//            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        } else if (TextUtils.equals(type, Constants.CHART_LINE)) {
//            dataSet.setDrawFilled(false);
//            dataSet.setMode(LineDataSet.Mode.LINEAR);
//        }
//        mChart.setData(combinedData);
//        mChart.invalidate();
    }

    @Override
    public void onStop() {
        EventBus.post(new DataRefreshEvent(false));
        isStop = true;
        stopNetwork();
    }

    @Override
    public void updateIssue(ProductEntity productEntity, IssueEntity issueEntity) {
        if (productEntity == null || issueEntity == null) return;
        if (this.productEntity != null && (this.productEntity == productEntity || this.productEntity.getProductId() == productEntity.getProductId())) {
            return;
        }
        stopNetwork();//停止以前的网络请求
        this.productEntity = productEntity;
        this.issueEntity = issueEntity;
        //获取期号
        mPresenter.getHistoryIssues(productEntity.getProductId(), this);
        //获取时时数据
        mHttpConnection = mPresenter.getAlwaysIssues(productEntity.getProductId(), mCallback);
    }

    @Override
    public Entry getEntry(String trim) {
        if (mEntries == null || mEntries.isEmpty()) return null;
        long trimL1 = TimerUtil.timerToLong(trim);
        long trimL2 = TimerUtil.timerToLong(((IndexMarkEntity) mEntries.get(0)).getTime());
        trimL1 = trimL1 - trimL2;
        int x = (int) (trimL1 / Constants.ISSUEINTERVAL);
        return new Entry(x, 0);
    }

    @Override
    public void onDestroy() {
        EventBus.post(new DataRefreshEvent(false));
    }

    private LineDataSet createSet(ArrayList<Entry> entries) {
        LineDataSet set = new LineDataSet(entries, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set.setColor(ColorTemplate.getHoloBlue());
        int color = Color.parseColor("#6CAEDC");
        set.setCircleColor(color);
        set.setLineWidth(1f);
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);//不画点
        set.setDrawValues(false);//不画点的值
        set.setDrawFilled(true);
        set.setDrawIcons(false);//显示图标
        //set.setCircleRadius(4f);
//        set.setFillAlpha(80);
//        set.setHighLightColor(color);//高亮颜色
        set.setValueTextColor(color);
        set.setHighlightEnabled(false);//不显示高亮
//        set.setDrawHighlightIndicators(true);
//        set.setDrawVerticalHighlightIndicator(true);
//        set.setFillAlpha(50);
        set.setValueTextSize(9f);

        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        set.setFormLineWidth(1f);
//        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//        set.enableDashedLine(10f, 5f, 0f);
//        set.setFormSize(15.f);
//        set.enableDashedHighlightLine(10f, 5f, 0f);
//        set.setCircleRadius(1f);
//        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));

        if (Utils.getSDKInt() >= 18) {
            Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.fade_red);
            set.setFillDrawable(drawable);
        } else {
            set.setFillColor(Color.BLACK);
        }
        return set;
    }

    @Override
    public void onCallback(int code, ArrayList<String> strings, String message) {
        if (mMThread != null) {
            mMThread.isDiscarded = true;//设置为废弃
            mMThread = null;
        }
        mMThread = new MThread(this, strings);
        mMThread.start();
    }

//    private float x;

    private void updateData(ArrayList<IndexMarkEntity> entities) {
        if (entities == null || entities.isEmpty()) return;
        if (mIndexMarkEntities != null && !mIndexMarkEntities.isEmpty()) {
            entities.addAll(mIndexMarkEntities);
            mIndexMarkEntities.clear();
        }
        mEntries.clear();
        mEntries.addAll(entities);
//        if (combinedData == null) {
//            combinedData = new CombinedData();
//            lineData = new LineData(createSet(mEntries));
//            combinedData.setData(lineData);
//            mChart.setData(combinedData);
//        } else {
//            //刷新数据
//            combinedData.notifyDataChanged();
//            mChart.setData(combinedData);
//        }
        combinedData = new CombinedData();
        lineData = new LineData(createSet(mEntries));
        combinedData.setData(lineData);
        mChart.setData(combinedData);
        isInitData = true;//已经初始化
        mChart.invalidate();

        HandlerUtil.runOnUiThreadDelay(new Runnable() {
            @Override
            public void run() {
                float X = getEntry(issueEntity.getBonusTime()).getX();//数据总条数
                float labelX = mChart.getFixedPosition();
                float labelWidth = mChart.getLabelWidth();
                float endX = labelX - labelWidth - dpPx10;
                float itemWidth = endX / X;
                float addItem = (labelWidth + dpPx10) / itemWidth;
                mXAxis.setAxisMaximum(X + addItem + 100);
            }
        }, 1000);

        //发送事件
        EventBus.post(new DataRefreshEvent(true));
    }

    private void updateData(IndexMarkEntity entity) {
        if (!isInitData) {
            mIndexMarkEntities.add(entity);
            return;
        }
        mEntries.add(entity);
        //刷新
        lineData.notifyDataChanged();
        mChart.notifyDataSetChanged();
//        int count = lineData.getEntryCount();
//        mChart.moveViewToX(count);
//        if (mXAxis.getAxisMinimum() < count + 200) {
//            mXAxis.setAxisMaximum(count + 310);
//        }
        mChart.invalidate();
    }

    private void stopNetwork() {
        if (mHttpConnection != null) {
            mHttpConnection.stop();
            mHttpConnection = null;
        }
        if (mMThread != null) {
            mMThread.isDiscarded = true;//设置废弃
            mMThread = null;
        }
    }

    private class Callback extends BaseCallback {

        private IndexUtil mIndexUtil;

        private Callback() {
            mIndexUtil = new IndexUtil();
        }

        @Override
        public void onMessageReceived(JsonElement jsonElement) {
            //在子线程运行
            if (isStop) {//断开链接
                stopNetwork();
                return;
            }
            if (lineData == null) return;
            final IndexMarkEntity entity = mIndexUtil.parseExponentially(lineData.getEntryCount(),
                    jsonElement.getAsString(), Constants.INDEXDIGIT);
            if (isStop) {//断开链接
                stopNetwork();
                return;
            }
            if (entity == null) return;
            HandlerUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateData(entity);
                }
            });
        }
    }

    private static class MThread extends Thread {
        private boolean isDiscarded = false;//是否废弃
        private ArrayList<String> strings;
        private IndexUtil mIndexUtil;
        private LineChartData mLineChartData;

        private MThread(LineChartData mLineChartData, ArrayList<String> strings) {
            this.strings = strings;
            this.mLineChartData = mLineChartData;
            mIndexUtil = new IndexUtil();
        }

        @Override
        public void run() {
            if (isDiscarded || mLineChartData == null) return;
            final ArrayList<IndexMarkEntity> entities = mIndexUtil.parseExponentially(strings, Constants.INDEXDIGIT);
            if (isDiscarded || mLineChartData == null) return;
            HandlerUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLineChartData.updateData(entities);
                }
            });
        }

    }

}
