package com.finance.linechartdata;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.finance.App;
import com.finance.common.Constants;
import com.finance.event.ChartDataUpdateEvent;
import com.finance.event.DataRefreshEvent;
import com.finance.event.EventBus;
import com.finance.listener.EventDistribution;
import com.finance.model.ben.IndexMarkEntity;
import com.finance.model.http.HttpConnection;
import com.finance.model.https.ApiCache;
import com.finance.model.imps.NetworkRequest;
import com.finance.ui.main.MainContract;
import com.finance.utils.HandlerUtil;
import com.finance.utils.IndexFormatUtil;
import com.finance.utils.IndexUtil;
import com.finance.utils.ViewUtil;
import com.finance.widget.combinedchart.MCombinedChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import static com.finance.utils.TimerUtil.timerToLong;

/**
 *
 */
public class LineChartData1 extends BaseChartData<Entry> implements EventDistribution.IChartDraw {

    private LineData lineData;//显示数据
    private HttpConnection /*mHttpConnection0,*/ mHttpConnection1;
    private Callback mCallback;//时时数据回调接口
    private IndexUtil mIndexUtil;//指数解析工具类
    //    private MThread mMThread;//解析数据线程
    //    private ArrayList<Entry> mAllIndexMarks;//全部数据
//    private ArrayList<Entry> mIndexMarkEntities;//推送的指数数据
    private ArrayList<IndexMarkEntity> mHistoryIndexMarks;//历史指数
    private boolean isRefrshChartData;//是否在刷新走势图数据
    //    private boolean isDraw = false;//是否绘制
    private long issueLengthTime;//当前期显示的数据时间长度
    private boolean isTimer = false;//是否可以转换时间
    //    private int drawStep = 1;//步长
    private long startTimer;//开始时间
    private ChartCurrentPointAnimation mPointAnimation;
    private IndexMarkEntity currentPoint;

    public LineChartData1(Context context, MainContract.View view, MCombinedChart chart, MainContract.Presenter presenter, View animView) {
        super(context, view, chart, presenter, animView);
//        this.mIndexMarkEntities = new ArrayList<>(6);
//        this.mAllIndexMarks = new ArrayList<>(600);
        mIndexUtil = new IndexUtil();
        this.mPointAnimation = new ChartCurrentPointAnimation(new ChartCurrentPointAnimation.IinvalidateChart() {
            @Override
            public void invalidateChart() {
                LineChartData1.this.invalidateChart();//刷新数据
            }
        });
    }

    @Override
    public Entry getEntry(String trim) {
        if (mChartDatas == null || mChartDatas.isEmpty()) return null;
        long trimL1 = timerToLong(trim);
        long trimL2 = timerToLong(((IndexMarkEntity) mChartDatas.get(0)).getTime());
//        long trimL2 = timerToLong(startTimer);
        trimL1 = trimL1 - trimL2;
        int x = (int) (trimL1 / Constants.ISSUEINTERVAL);
        return new Entry(x, 0);
//        return new Entry(Constants.getIndexX(trimL1), 0);
    }

    @Override
    public IndexMarkEntity getIndexMarkEntity(String indexMark) {
        if (mChartDatas == null || mChartDatas.isEmpty() || TextUtils.isEmpty(indexMark))
            return null;
        int size = mChartDatas.size();
        IndexMarkEntity indexMarkEntity = mIndexUtil.parseExponentially(0, indexMark, Constants.INDEXDIGIT);
        IndexMarkEntity entity;
        for (int i = size - 1; i >= 0; i--) {
            entity = (IndexMarkEntity) mChartDatas.get(i);
            if (TextUtils.equals(entity.getId(), indexMark) || (indexMarkEntity != null && indexMarkEntity.getTimeLong() >= entity.getTimeLong()))
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
    public LineData getLineData() {
        if (lineData == null) {
            LineDataSet set = ViewUtil.createLineDataSet(mContext, mChartDatas);
            lineData = new LineData(set);
        }
        return lineData;
    }

//    @Override
//    protected int getDrawSetp() {
//        return drawStep;
//    }

    @Override
    protected int getXIndex(long timer) {
        return (int) ((timer - startTimer) / Constants.ISSUEINTERVAL);
    }

    @Override
    public void onResume(int type) {
        if (currentChartType == type) return;
        if (lineData != null) {
            if (type == Constants.CHART_LINEFILL) {
                LineDataSet set = ((LineDataSet) lineData.getDataSetByIndex(0));
                set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                set.setDrawFilled(true);
            } else if (type == Constants.CHART_LINE) {
                LineDataSet set = ((LineDataSet) lineData.getDataSetByIndex(0));
                set.setMode(LineDataSet.Mode.LINEAR);
                set.setDrawFilled(false);
            }
        }
        EventDistribution.getInstance().addChartDraws(this);
        super.onResume(type);
    }

    @Override
    public void onDestroy(int chartType) {
        super.onDestroy(chartType);
        EventDistribution.getInstance().removeChartDraws(this);
        if (chartType != Constants.CHART_LINEFILL && chartType != Constants.CHART_LINE) {
            if (mCallback != null) {
                mCallback.isStop = true;
                mCallback = null;
            }
        }
    }

    @Override
    public void stopNetwork() {
//        if (mHttpConnection0 != null) {
//            mHttpConnection0.stop();
//            mHttpConnection0 = null;
//        }
        if (mHttpConnection1 != null) {
            if (topProductEntity != null) {
                mPresenter.unSubscribeProduct(topProductEntity.getProductId());
                topProductEntity = null;
                topProductEntity = productEntity;
            }
            mHttpConnection1.stop();
            mHttpConnection1 = null;
        }
//        if (mMThread != null) {
//            mMThread.isDiscarded = true;//设置废弃
//            mMThread = null;
//        }
    }

    @Override
    public long getStartTimer() {
        return startTimer;
//        return ((IndexMarkEntity) mChartDatas.get(0)).getTimeLong();
//        return 0;
    }

    @Override
    public IndexMarkEntity getCurrentEntry() {
        return currentPoint;
    }

    private long getStartTimer(long timer) {
        long startTimer = timer / 1000 / 60;
        return (startTimer * 1000 * 60 + 60000) - (5 * 60 * 1000);
    }

    @Override
    protected void updateData() {
        if (issueEntity == null || productEntity == null) return;
        isRefrshChartData = true;

        //重置介入绘制参数
//        mChart.setDrawIntervention(-1, 0, 300);
        EventBus.post(new DataRefreshEvent(false));
        stopNetwork();//停止以前的网络请求
        isTimer = false;//设置不可以转换时间
//        startRemoveDataAnimation();//启动删除动画
        long timer = timerToLong(issueEntity.getBonusTime()) - Constants.SERVERCURRENTTIMER;
        //获取期号
//        mHttpConnection0 = mPresenter.getHistoryIssues(productEntity.getProductId(), (int) (timer / 1000), getCallbackIssues());
        mPresenter.getHistoryIssues(productEntity.getProductId(), (int) (timer / 1000), getCallbackIssues());
        //获取时时数据
        mPresenter.getAlwaysIssues(productEntity.getProductId(), getAlwaysCallback());
//        mPresenter.getAlwaysIssues(productEntity.getProductId(), mCallback);
    }

    @Override
    protected void stopAddAnimation() {
        super.stopAddAnimation();
        //添加数据动画执行完成
//        addAlwaysDatas(mChartDatas);
        invalidateChart();//刷新数据
        //发送事件
        EventBus.post(new DataRefreshEvent(true));
        //走势图数据查询事件
        EventBus.post(new ChartDataUpdateEvent());
    }

    @Override
    protected void updateStartTime(Entry entry) {
        startTimer = ((IndexMarkEntity) entry).getTimeLong();
    }

    @Override
    protected ArrayList<Entry> getShowData() {
        int size = mChartDatas.size();
        return getShowData(mChartDatas, size, getStartTimer(Constants.SERVERCURRENTTIMER));
    }

    private ArrayList<Entry> getShowData(ArrayList<Entry> arrayList, int size, long startTimer) {
        IndexMarkEntity entity2;
        int startIndex = -1;
        for (int i = 0; i < size; i++) {
            entity2 = (IndexMarkEntity) arrayList.get(i);
            if (entity2.getTimeLong() >= startTimer) {
                if (startIndex == -1)
                    startIndex = i;
                entity2.setX((int) ((entity2.getTimeLong() - startTimer) / Constants.ISSUEINTERVAL));
            }
        }
        ArrayList<Entry> entitys;
        if (startIndex > 0) {
            entitys = new ArrayList<>(arrayList.subList(startIndex, size - 1));
        } else {
            entitys = new ArrayList<>(arrayList);
        }
        IndexFormatUtil.sortEntry(entitys);//排序
        updateStartTime(entitys.get(0));
        return entitys;
    }

    private ArrayList<IndexMarkEntity> getIndexMark(ArrayList<IndexMarkEntity> arrayList, int size, long startTimer) {
        IndexMarkEntity entity2;
        int startIndex = -1;
        for (int i = 0; i < size; i++) {
            entity2 = arrayList.get(i);
            if (entity2.getTimeLong() >= startTimer) {
                if (startIndex == -1)
                    startIndex = i;
                entity2.setX((int) ((entity2.getTimeLong() - startTimer) / Constants.ISSUEINTERVAL));
            }
        }
        ArrayList<IndexMarkEntity> entitys;
        if (startIndex > 0) {
            entitys = new ArrayList<>(arrayList.subList(startIndex, size - 1));
        } else {
            entitys = new ArrayList<>(arrayList);
        }
        return entitys;
    }

    //添加历史数据
    private void addHistoryDatas() {
        if (isAnimation) return;
        ArrayList<Entry> entries = new ArrayList<>();
        if (mHistoryIndexMarks != null && !mHistoryIndexMarks.isEmpty()) {
            entries.addAll(mHistoryIndexMarks);
            mHistoryIndexMarks.clear();
            mHistoryIndexMarks = null;
        }
//        addAlwaysDatas(entries);
        currentPoint = (IndexMarkEntity) entries.get(entries.size() - 1).copy();
        startAddDataAnimation(entries);
    }

//    //添加时时数据
//    private void addAlwaysDatas(ArrayList<Entry> entries) {
//        if (mIndexMarkEntities != null && !mIndexMarkEntities.isEmpty()) {
//            entries.addAll(mIndexMarkEntities);
////            for (IndexMarkEntity entity : mIndexMarkEntities) {
////                //更新下标
////                entity.setX(entries.size());
////                entries.add(entity);
////            }
//            mIndexMarkEntities.clear();
//        }
//    }

    //刷新时时数据
    private void updateAlwaysData(IndexMarkEntity entity) {
        if (!isTimer) return;
        if (isRefrshChartData || isAnimation) return;
//        mAllIndexMarks.add(entity);
        entity.setX(getXIndex(entity.getTimeLong()));
//        int r = addIndexEntity(mChartDatas, entity);
//        if (r == 1) return;
        mChartDatas.add(entity);
//        top = entity;
        int size = mChartDatas.size();
        currentPoint = entity.copy();
        mPointAnimation.updateParam(mChartDatas.get(size - 1), mChartDatas.get(size - 2));
        mPointAnimation.stopAnimation();
        mPointAnimation.startAnimation();
//        int size = mChartDatas.size();
//        if (r == 2) {
//            mPointAnimation.updateParam(mChartDatas.get(size - 1), top);
//        } else {
//            mPointAnimation.updateParam(mChartDatas.get(size - 1), mChartDatas.get(size - 2));
//        }
//        top = entity;
//        currentPoint = entity.copy();
//        mPointAnimation.stopAnimation();
//        mPointAnimation.startAnimation();
        //invalidateChart();//刷新数据
    }

    private void updateHistoryData(ArrayList<IndexMarkEntity> entities) {
        isRefrshChartData = false;
        if (entities == null || entities.isEmpty()) {

            return;
        }
//        drawStep = entities.size() / 150;
//        mChart.setDrawStep(drawStep);//设置步长
        mHistoryIndexMarks = entities;
//        mAllIndexMarks.clear();
//        mAllIndexMarks.addAll(entities);
//        ArrayList<IndexMarkEntity> indexMarkEntities = new ArrayList<>(entities);
        addHistoryDatas();
//        startTestData(indexMarkEntities);
    }

    @Override
    public void onDraw(Entry entry) {
//        if (isDraw) return;
////        setAxisMaximum();
//        isDraw = true;
    }

    private MCallbackIssues mCallbackIssues;
//    private MThread mMThread;

    private MCallbackIssues getCallbackIssues() {
        if (mCallbackIssues != null) {
            //设置为废弃
            mCallbackIssues.isDiscarded = true;
            mCallbackIssues = null;
        }
        mCallbackIssues = new MCallbackIssues();
        return mCallbackIssues;

//        if (mMThread != null) {
//            mMThread.isDiscarded = true;
//            mMThread = null;
//        }
//        return new ICallback<ArrayList<String>>() {
//            @Override
//            public void onCallback(int code, ArrayList<String> strings, String message) {
//                mMThread = new MThread(LineChartData1.this, strings);
//                mMThread.start();
//            }
//        };
    }

    private Callback getAlwaysCallback() {
        if (mCallback != null) {
            ApiCache.removeCallBacks(mCallback);
            mCallback.isStop = true;
            mCallback = null;
        }
        return mCallback = new Callback(this);
    }


    private static class Callback extends NetworkRequest.BaseCallback {

        private IndexUtil mIndexUtil;
        private LineChartData1 mChartData;
        private boolean isStop = false;
        private ArrayList<String> indexStrs;

        private Callback(LineChartData1 chartData) {
            mIndexUtil = new IndexUtil();
            mChartData = chartData;
            indexStrs = new ArrayList<>(10);
        }

        @Override
        public void onMessageReceived(String data) {
            //在子线程运行
            if (isStop) {//断开链接
                return;
            }
            if (mChartData.lineData == null) return;
            if (!mChartData.isTimer) {//不可以转换指数
//                indexStrs.add(jsonElement.getAsString());
                return;
            }
            final IndexMarkEntity entity = mIndexUtil.parseExponentially(0, data, Constants.INDEXDIGIT);
            if (isStop /*|| entity.getTime() == -1*/) {//断开链接
                return;
            }
            if (entity == null) return;
            HandlerUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    addCount++;
//                    Log.d("123", "run: " + entity.getX() + "," + entity.getY());
                    mChartData.updateAlwaysData(entity);
                }
            });
        }
    }

    private class MCallbackIssues extends NetworkRequest.BaseCallback {
        private boolean isDiscarded = false;//是否废弃
        private IndexUtil mIndexUtil;

        @Override
        public void onMessageReceived(String data) {
//            long current = System.currentTimeMillis();
//            if (current - topStartTimer < 500) {//重复返回
//                return;
//            }
            if (isDiscarded) {
                return;
            }
            ArrayList<String> issues = new Gson().fromJson(data, new TypeToken<ArrayList<String>>() {
            }.getType());
            if (issues == null || issues.isEmpty()) {
                App.getInstance().showErrorMsg("历史期号获取失败！");
                return;
            }
            if (isDiscarded) {
                return;
            }
            mIndexUtil = new IndexUtil();
            IndexMarkEntity entity = mIndexUtil.parseExponentially(0, issues.get(0), Constants.INDEXDIGIT);
//            Constants.setReferenceX(entity.getTime());//更新基准下标
//            添加时时推送的指数
            if (mCallback != null && mCallback.indexStrs != null) {
                issues.addAll(mCallback.indexStrs);
                mCallback.indexStrs.clear();
            }
            final ArrayList<IndexMarkEntity> entities = mIndexUtil.parseExponentially(entity.getTimeLong(), issues, Constants.INDEXDIGIT);
            long startTimer = getStartTimer(Constants.SERVERCURRENTTIMER);
            final ArrayList<IndexMarkEntity> entities2 = getIndexMark(entities, entities.size(), startTimer);
            if (isDiscarded) return;
            isTimer = true;
            IndexFormatUtil.sortIndex(entities2);
            updateStartTime(entities2.get(0));//更新起始时间
            HandlerUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isDiscarded) return;
                    updateHistoryData(entities2);
                }
            });
        }
    }

//    private static class MThread extends Thread {
//        private boolean isDiscarded = false;//是否废弃
//        private ArrayList<String> strings;
//        private IndexUtil mIndexUtil;
//        private LineChartData1 mLineChartData;
//
//        private MThread(LineChartData1 mLineChartData, ArrayList<String> strings) {
//            this.strings = strings;
//            this.mLineChartData = mLineChartData;
//            mIndexUtil = new IndexUtil();
//        }
//
//        @Override
//        public void run() {
//            if (isDiscarded || mLineChartData == null) return;
//            IndexMarkEntity entity = mIndexUtil.parseExponentially(0, strings.get(0), Constants.INDEXDIGIT);
////            Constants.setReferenceX(entity.getTime());//更新基准下标
////            添加时时推送的指数
//            if (mLineChartData.mCallback != null && mLineChartData.mCallback.indexStrs != null) {
//                strings.addAll(mLineChartData.mCallback.indexStrs);
//                mLineChartData.mCallback.indexStrs.clear();
//            }
//            final ArrayList<IndexMarkEntity> entities = mIndexUtil.parseExponentially(entity.getTimeLong(), strings, Constants.INDEXDIGIT);
//            long startTimer = mLineChartData.getStartTimer(Constants.SERVERCURRENTTIMER);
//            final ArrayList<IndexMarkEntity> entities2 = mLineChartData.getIndexMark(entities, entities.size(), startTimer);
////            Collections.sort(entities2, new Comparator<IndexMarkEntity>() {
////                @Override
////                public int compare(IndexMarkEntity o1, IndexMarkEntity o2) {
////                    return o1.getTimeLong() > o2.getTimeLong() ? 1 : -1;
////                }
////            });
//            if (isDiscarded || mLineChartData == null) return;
//            mLineChartData.isTimer = true;
//            IndexFormatUtil.sortIndex(entities2);
//            mLineChartData.updateStartTime(entities2.get(0));
//            HandlerUtil.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mLineChartData.updateHistoryData(entities2);
//                }
//            });
//        }
//    }

}
