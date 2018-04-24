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
import com.finance.interfaces.IChartData;
import com.finance.utils.HandlerUtil;
import com.finance.widget.combinedchart.MCombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * 显示数据设置类
 */
public class LineChartData implements IChartData {

    private MCombinedChart mChart;
    private Activity activity;
    private XAxis mXAxis;

    private CombinedData combinedData;
    private LineData data;
    private boolean isResume = false;

    public LineChartData(Activity activity, MCombinedChart chart) {
        this.activity = activity;
        this.mChart = chart;
        mXAxis = mChart.getXAxis();
    }

    @Override
    public LineChartData onInit() {
        combinedData = new CombinedData();
        data = initLineData(5 * 60 * 1000);
        combinedData.setData(data);
        return this;
    }

    @Override
    public void onResume(String type) {
        isWorker = true;
        //X轴显示最大值
        mXAxis.setAxisMaximum(data.getDataSets().get(0).getEntryCount() + 300);
        LineDataSet dataSet = (LineDataSet) data.getDataSetByIndex(0);
        if (TextUtils.equals(type, Constants.CHART_LINEFILL)) {
            dataSet.setDrawFilled(true);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else if (TextUtils.equals(type, Constants.CHART_LINE)) {
            dataSet.setDrawFilled(false);
            dataSet.setMode(LineDataSet.Mode.LINEAR);
        }
        mChart.setData(combinedData);
        mChart.invalidate();
        if (!isResume) {
            isResume = true;
            HandlerUtil.runOnUiThreadDelay(new Runnable() {
                @Override
                public void run() {
                    if (!isWorker) return;
                    worker = getWorker();
                    worker.start();
                    EventBus.post(new DataRefreshEvent(true));
                }
            }, Constants.XANIMATION);
        }
    }

    @Override
    public void onStop() {
        isWorker = false;
        worker = null;
        EventBus.post(new DataRefreshEvent(false));
    }

    @Override
    public void onDestroy() {
        isWorker = false;
        worker = null;
        EventBus.post(new DataRefreshEvent(false));
    }

    private boolean isWorker = false;
    private Thread worker;

    private Thread getWorker() {
        return new Thread() {
            @Override
            public void run() {
                while (isWorker) {
                    HandlerUtil.runOnUiThread(runnable);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private long startTime = 0;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            LineData data = mChart.getData().getLineData();
            if (data == null) return;
            ILineDataSet dataSet = data.getDataSetByIndex(0);
            if (dataSet == null) {
                dataSet = createSet(null);
                data.addDataSet(dataSet);
            }
            int count = data.getEntryCount();
//            Entry entry = dataSet.getEntryForIndex(count - 1);
//            entry.setIcon(null);//去掉图标
            Entry entry = getEntry(count);
            data.addEntry(entry, 0);
            data.notifyDataChanged();
            //刷新
            mChart.notifyDataSetChanged();
//            mChart.moveViewToX(data.getEntryCount() - 20);

            long mis = System.currentTimeMillis();
            float vt = (mis - startTime) / 100f;
//            Log.d("123", "startTime:" + startTime);
//            Log.d("123", "mis:" + mis);

//            if (mChart.getScaleX() != 1)
//                mChart.setScaleX(mChart.getScaleX() - 0.1f);
//            if (mChart.getScaleY() != 1)
//                mChart.setScaleY(mChart.getScaleY() - 0.1f);

            mChart.moveViewToX(data.getEntryCount());

            //dep/s52

//            if (data.getEntryCount() % 10 == 0) {
//                mChart.moveViewToX(data.getEntryCount());
//            } else {
//                mChart.invalidate();
//            }

            if (mXAxis.getAxisMinimum() < count + 200) {
                mXAxis.setAxisMaximum(count + 310);
//                mChart.setMaxVisibleValueCount(count + 300);
            }
        }
    };

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
        set.setHighLightColor(color);
        set.setValueTextColor(color);
        set.setHighlightEnabled(true);
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

    private LineData initLineData(int timer) {
        int size = timer / 500;
        ArrayList<Entry> entries = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            entries.add(getEntry(i));
        }
        LineDataSet dataSet = createSet(entries);
        LineData data = new LineData(dataSet);
        return data;
    }

    private Random rand = new Random();
    private int count;
    private boolean isUp = true;
    private float value = 51f;

    private Entry getEntry(int x) {
        if (count <= 0) {
            count = rand.nextInt(5) + 1;
            isUp = !isUp;
        }
        if (isUp) {
            value += 0.1f;
        } else {
            value -= 0.1f;
        }
        if (value > 60) value = 60;
        else if (value < 50) value = 50;
        count--;
//        float val = rand.nextInt(3) + 50;//生成50——100直接的数
        return new Entry(x, value);
    }


}
