package com.finance.linechartdata;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.finance.R;
import com.finance.common.Constants;
import com.finance.event.DataRefreshEvent;
import com.finance.event.EventBus;
import com.finance.interfaces.ICallback;
import com.finance.interfaces.IChartData;
import com.finance.listener.EventDistribution;
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

/**
 * 显示数据设置类
 */
public class LineChartData implements IChartData, ICallback<ArrayList<String>>, EventDistribution.IChartDraw, EventDistribution.IPurchase {

    private MCombinedChart mChart;
    private Activity activity;
    private MainContract.View mView;
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
    private Callback mCallback;//时时数据回调接口
    private MThread mMThread;//解析数据线程
    private ArrayList<IndexMarkEntity> mIndexMarkEntities;//推送的指数数据
    private int dpPx10;//开奖线距离右边标签间距
    private boolean isRefrshChartData;//是否在刷新走势图数据
    private boolean isDraw = false;//是否绘制

    public LineChartData(Activity activity, MainContract.View view, MCombinedChart chart, MainContract.Presenter presenter) {
        this.activity = activity;
        this.mView = view;
        this.mChart = chart;
        this.mPresenter = presenter;
        this.mXAxis = mChart.getXAxis();
        this.mIndexMarkEntities = new ArrayList<>(2);
        this.mEntries = new ArrayList<>();
        this.dpPx10 = activity.getResources().getDimensionPixelOffset(R.dimen.dp_20);
        onInit();
    }

    private void onInit() {
        mCallback = new Callback();
    }

    @Override
    public void onResume(int type) {
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

        EventDistribution.getInstance().addChartDraws(this);
        EventDistribution.getInstance().addPurchase(this);
    }

    public void onStop() {
        EventBus.post(new DataRefreshEvent(false));
        EventDistribution.getInstance().removeChartDraws(null);
        EventDistribution.getInstance().removePurchase(null);
        isStop = true;
        stopNetwork();
    }

    @Override
    public void updateIssue(ProductEntity productEntity, IssueEntity issueEntity) {
        if (productEntity == null || issueEntity == null) return;
        if (this.issueEntity != null && this.issueEntity == issueEntity) {
            return;
        }
        isRefrshChartData = true;
//        addCount = 0;
        stopNetwork();//停止以前的网络请求
        this.productEntity = productEntity;
        this.issueEntity = issueEntity;
        isInitData = false;
        //获取期号
        mPresenter.getHistoryIssues(productEntity.getProductId(), 300, this);
        //获取时时数据
        mHttpConnection = mPresenter.getAlwaysIssues(productEntity.getProductId(), mCallback);
    }

    @Override
    public Entry getEntry(String trim) {
        if (mEntries == null || mEntries.isEmpty()) return null;
        long trimL1 = TimerUtil.timerToLong(trim);
        long trimL2 = TimerUtil.timerToLong(((IndexMarkEntity) mEntries.get(0)).getTime());
//        long trimL2 = ((IndexMarkEntity) mEntries.get(0)).getTime();
//        long trimL2 = ((IndexMarkEntity) mEntries.get(0)).getTime();
        trimL1 = trimL1 - trimL2;
        int x = (int) (trimL1 / Constants.ISSUEINTERVAL);
        return new Entry(x, 0);
    }

    @Override
    public IndexMarkEntity getIndexMarkEntity(String indexMark) {
        if (mEntries == null || mEntries.isEmpty() || TextUtils.isEmpty(indexMark)) return null;
        int size = mEntries.size();
        IndexMarkEntity entity;
        for (int i = size - 1; i >= 0; i--) {
            entity = (IndexMarkEntity) mEntries.get(i);
            if (TextUtils.equals(entity.getId(), indexMark))
                return entity;
        }
        return null;
    }

    @Override
    public boolean isRefrshChartData() {
        return isRefrshChartData;
    }

    @Override
    public void onDestroy() {
        EventBus.post(new DataRefreshEvent(false));
    }

    private LineDataSet createSet(ArrayList<Entry> entries) {
        LineDataSet set = new LineDataSet(entries, "");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.parseColor("#FFFFED79"));//走势图线条颜色
//        int color = Color.parseColor("#6CAEDC");
//        set.setCircleColor(Color.parseColor("#90ffed79"));
        set.setLineWidth(1f);
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);//不画点
        set.setDrawValues(false);//不画点的值
        set.setDrawFilled(true);
        set.setDrawIcons(false);//显示图标
        //set.setCircleRadius(4f);
//        set.setFillAlpha(80);
//        set.setHighLightColor(color);//高亮颜色
//        set.setValueTextColor(color);
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
            set.setFillColor(Color.parseColor("#50FFED79"));
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

    private void updateData(ArrayList<IndexMarkEntity> entities) {
        if (entities == null || entities.isEmpty()) return;
        if (mIndexMarkEntities != null && !mIndexMarkEntities.isEmpty()) {
            int start = entities.size();
            for (IndexMarkEntity entity : mIndexMarkEntities) {
                //更新下标
                entity.setX(start);
                entities.add(entity);
                start++;
            }
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
        isDraw = false;
        isRefrshChartData = false;
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
//        rightAxis.setAxisMaximum(2);
//        rightAxis.setAxisMinimum(0);
        mChart.notifyDataSetChanged();

//        mChart.fitScreen();//重设所有缩放和拖动，使图表完全适合它的边界（完全缩小）。
        //        int count = lineData.getEntryCount();
//        mChart.moveViewToX(count);
//        if (mXAxis.getAxisMinimum() < count + 200) {
//            mXAxis.setAxisMaximum(count + 310);
//        }
        mChart.invalidate();
    }

    @Override
    public void stopNetwork() {
        if (mHttpConnection != null) {
            mHttpConnection.stop();
            mHttpConnection = null;
        }
        if (mMThread != null) {
            mMThread.isDiscarded = true;//设置废弃
            mMThread = null;
        }
    }

    @Override
    public long getStartTimer() {
        return 0;
    }

    @Override
    public void onDraw(Entry entry) {
//        if (isDraw) return;
//        //绘制完成回调
//        float X = getEntry(issueEntity.getBonusTime()).getX();//数据总条数
//        float labelX = mChart.getFixedPosition();
//        float labelWidth = mChart.getLabelWidth();
//        float endX = labelX - labelWidth - dpPx10;
//        float itemWidth = endX / X;
//        float addItem = (labelWidth + dpPx10) / itemWidth;
//        mXAxis.setAxisMaximum(X + addItem);
//        isDraw = true;
    }

    @Override
    public void stopPurchase(boolean isOrder) {
        if (isOrder) return;
        stopNetwork();
        mView.refreshIessue();//刷新期号
    }

    @Override
    public void openPrize(boolean isOrder) {
        stopNetwork();
        mView.refreshIessue();//刷新期号
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
            final IndexMarkEntity entity = mIndexUtil.parseExponentially(0, jsonElement.getAsString(), Constants.INDEXDIGIT);
            if (isStop) {//断开链接
                stopNetwork();
                return;
            }
            if (entity == null) return;
            HandlerUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    addCount++;
                    updateData(entity);
                }
            });
        }

        @Override
        public void noNetworkConnected() {

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
//            IndexMarkEntity entity = mIndexUtil.parseExponentially(0,strings.get(0), Constants.INDEXDIGIT);
//            Constants.setReferenceX((long) entity.getX());//更新基准下标
            final ArrayList<IndexMarkEntity> entities = mIndexUtil.parseExponentially(0, strings, Constants.INDEXDIGIT);
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
