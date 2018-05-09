package com.finance.linechartdata;

import android.content.Context;
import android.text.TextUtils;

import com.finance.common.Constants;
import com.finance.event.DataRefreshEvent;
import com.finance.event.EventBus;
import com.finance.interfaces.ICallback;
import com.finance.listener.EventDistribution;
import com.finance.model.ben.IndexMarkEntity;
import com.finance.model.http.BaseCallback;
import com.finance.model.http.HttpConnection;
import com.finance.ui.main.MainContract;
import com.finance.utils.HandlerUtil;
import com.finance.utils.IndexUtil;
import com.finance.utils.TimerUtil;
import com.finance.utils.ViewUtil;
import com.finance.widget.combinedchart.MCombinedChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import static com.finance.utils.TimerUtil.timerToLong;

/**
 *
 */
public class LineChartData1 extends BaseChartData<Entry> implements ICallback<ArrayList<String>>, EventDistribution.IChartDraw {

    private LineData lineData;//显示数据
    private HttpConnection mHttpConnection;
    private Callback mCallback;//时时数据回调接口
    private MThread mMThread;//解析数据线程
    private ArrayList<IndexMarkEntity> mIndexMarkEntities;//推送的指数数据
    private ArrayList<IndexMarkEntity> mHistoryIndexMarks;//历史指数
    private boolean isRefrshChartData;//是否在刷新走势图数据
    private boolean isDraw = false;//是否绘制
    private long issueLengthTime;//当前期显示的数据时间长度

    public LineChartData1(Context context, MainContract.View view, MCombinedChart chart, MainContract.Presenter presenter) {
        super(context, view, chart, presenter);
        this.mIndexMarkEntities = new ArrayList<>(6);
    }

    @Override
    public Entry getEntry(String trim) {
        if (mChartDatas == null || mChartDatas.isEmpty()) return null;
        long trimL1 = timerToLong(trim);
        long trimL2 = timerToLong(((IndexMarkEntity) mChartDatas.get(0)).getTime());
        trimL1 = trimL1 - trimL2;
        int x = (int) (trimL1 / Constants.ISSUEINTERVAL);
        return new Entry(x, 0);
    }

    @Override
    public IndexMarkEntity getIndexMarkEntity(String indexMark) {
        if (mChartDatas == null || mChartDatas.isEmpty() || TextUtils.isEmpty(indexMark))
            return null;
        int size = mChartDatas.size();
        IndexMarkEntity entity;
        for (int i = size - 1; i >= 0; i--) {
            entity = (IndexMarkEntity) mChartDatas.get(i);
            if (TextUtils.equals(entity.getId(), indexMark))
                return entity;
        }
        return null;
    }

    @Override
    public boolean isRefrshChartData() {
        return isRefrshChartData || isAnimation;
    }

    @Override
    protected void onInit() {
        LineDataSet set = ViewUtil.createLineDataSet(mContext, mChartDatas);
        lineData = new LineData(set);
        combinedData.setData(lineData);
        mChart.setData(combinedData);
    }

    @Override
    public void onResume(String type) {
        super.onResume(type);
        EventDistribution.getInstance().addChartDraws(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventDistribution.getInstance().removeChartDraws(this);
        //发送事件
        EventBus.post(new DataRefreshEvent(false));
        if (mCallback != null) {
            mCallback.isStop = true;
            mCallback = null;
        }
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

    @Override
    protected void updateData() {
        isRefrshChartData = true;
        if (mCallback != null) {
            mCallback.isStop = true;
            mCallback = null;
        }
        EventBus.post(new DataRefreshEvent(false));
        mCallback = new Callback(this);
        stopNetwork();//停止以前的网络请求
        long timer = TimerUtil.timerToLong(issueEntity.getBonusTime()) - Constants.SERVERCURRENTTIMER;
        //获取期号
        mPresenter.getHistoryIssues(productEntity.getProductId(), (int) (timer / 1000), this);
        //获取时时数据
//        mHttpConnection = mPresenter.getAlwaysIssues(productEntity.getProductId(), mCallback);
        mPresenter.getAlwaysIssues(productEntity.getProductId(), mCallback);
        startRemoveDataAnimation();//启动删除动画
    }

    @Override
    protected void stopAddAnimation() {
        //添加数据动画执行完成
        addAlwaysDatas(mChartDatas);
        invalidateChart();//刷新数据
        //发送事件
        EventBus.post(new DataRefreshEvent(true));
    }

    @Override
    protected void stopRemoveAnimation() {
        //删除数据动画执行完成
        addHistoryDatas();
    }

    @Override
    protected long getLengthTime() {
        return issueLengthTime;
    }

    //添加历史数据
    private void addHistoryDatas() {
        if (isAnimation) return;
        if (mHistoryIndexMarks == null && mIndexMarkEntities.isEmpty()) return;
        ArrayList<Entry> entries = new ArrayList<>();
        if (mHistoryIndexMarks != null) {
            entries.addAll(mHistoryIndexMarks);
            mHistoryIndexMarks.clear();
            mHistoryIndexMarks = null;
        }
        addAlwaysDatas(entries);
        isDraw = false;
        //刷新时长长度
        long timer1 = TimerUtil.timerToLong(((IndexMarkEntity) entries.get(0)).getTime());
        long timer2 = TimerUtil.timerToLong(issueEntity.getBonusTime());
        issueLengthTime = timer2 - timer1;
        startAddDataAnimation(entries);
    }

    //添加时时数据
    private void addAlwaysDatas(ArrayList<Entry> entries) {
        if (mIndexMarkEntities != null && !mIndexMarkEntities.isEmpty()) {
            int start = entries.size();
            for (IndexMarkEntity entity : mIndexMarkEntities) {
                //更新下标
                entity.setX(start);
                entries.add(entity);
                start++;
            }
            mIndexMarkEntities.clear();
        }
    }

    //刷新时时数据
    private void updateAlwaysData(IndexMarkEntity entity) {
        if (isRefrshChartData || isAnimation) {
            mIndexMarkEntities.add(entity);
            return;
        }
        mChartDatas.add(entity);
        invalidateChart();//刷新数据
    }

    private void updateHistoryData(ArrayList<IndexMarkEntity> entities) {
        isRefrshChartData = false;
        if (entities == null || entities.isEmpty()) return;
        mHistoryIndexMarks = entities;
        addHistoryDatas();
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

    @Override
    public void onDraw(Entry entry) {
        if (isDraw) return;
//        setAxisMaximum();
        isDraw = true;
    }

    private static class Callback extends BaseCallback {

        private IndexUtil mIndexUtil;
        private LineChartData1 mChartData;
        private boolean isStop = false;

        private Callback(LineChartData1 chartData) {
            mIndexUtil = new IndexUtil();
            mChartData = chartData;
        }

        @Override
        public void onMessageReceived(JsonElement jsonElement) {
            //在子线程运行
            if (isStop) {//断开链接
                return;
            }
            if (mChartData.lineData == null) return;
            final IndexMarkEntity entity = mIndexUtil.parseExponentially(mChartData.lineData.getEntryCount(), jsonElement.getAsString(), Constants.INDEXDIGIT);
            if (isStop) {//断开链接
                return;
            }
            if (entity == null) return;
            HandlerUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    addCount++;
                    mChartData.updateAlwaysData(entity);
                }
            });
        }
    }

    private static class MThread extends Thread {
        private boolean isDiscarded = false;//是否废弃
        private ArrayList<String> strings;
        private IndexUtil mIndexUtil;
        private LineChartData1 mLineChartData;

        private MThread(LineChartData1 mLineChartData, ArrayList<String> strings) {
            this.strings = strings;
            this.mLineChartData = mLineChartData;
            mIndexUtil = new IndexUtil();
        }

        @Override
        public void run() {
            if (isDiscarded || mLineChartData == null) return;
//            IndexMarkEntity entity = mIndexUtil.parseExponentially(strings.get(0), Constants.INDEXDIGIT);
//            Constants.setReferenceX((long) entity.getX());//更新基准下标
            final ArrayList<IndexMarkEntity> entities = mIndexUtil.parseExponentially(0, strings, Constants.INDEXDIGIT);
            if (isDiscarded || mLineChartData == null) return;
            HandlerUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLineChartData.updateHistoryData(entities);
                }
            });
        }

    }

}
