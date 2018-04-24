package com.finance.linechartdata;

import android.app.Activity;
import android.graphics.Color;

import com.finance.interfaces.IChartData;
import com.finance.widget.combinedchart.MCombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

/**
 * 蜡烛走势图
 */
public class CandleChartData implements IChartData {

    private MCombinedChart mChart;
    private Activity activity;
    private XAxis mXAxis;

    private CombinedData mCombinedData;

    public CandleChartData(Activity activity, MCombinedChart chart) {
        this.activity = activity;
        this.mChart = chart;
        mXAxis = mChart.getXAxis();
    }

    @Override
    public CandleChartData onInit() {
        mCombinedData = new CombinedData();
//        data.setData(generateLineData());//折线图
        mCombinedData.setData(generateCandleData());//蜡烛图
        return this;
    }

    @Override
    public void onResume(String type) {
        mChart.setData(mCombinedData);
        mXAxis.setAxisMaximum(20);
        mChart.invalidate();
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        mCombinedData = null;
    }

    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < 20; index++)
            entries.add(new Entry(index + 0.5f, getRandom(15, 5)));

        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.rgb(240, 238, 70));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    protected CandleData generateCandleData() {

        CandleData d = new CandleData();

        ArrayList<CandleEntry> entries = new ArrayList<CandleEntry>();

        for (int index = 0; index < 20; index += 2)
            entries.add(new CandleEntry(index + 1f, 85, 75, 82, 77f));

        CandleDataSet set = new CandleDataSet(entries, "Candle DataSet");
        set.setDecreasingColor(Color.rgb(142, 150, 175));
        set.setShadowColor(Color.DKGRAY);
        set.setBarSpace(0.3f);
        set.setValueTextSize(10f);
        set.setDrawValues(false);
        d.addDataSet(set);

        return d;
    }


}
