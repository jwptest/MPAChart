package com.finance.ui.test;

import android.graphics.Color;

import com.finance.R;
import com.finance.base.BaseActivity;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import butterknife.BindView;

/**
 *
 */
public class CandleStickChartActivity extends BaseActivity {

    @BindView(R.id.cscK)
    CombinedChart mChart;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_candlestickchart;
    }

    @Override
    protected void onCreated() {

//        new LineChartSetting(this, mChart)
//                .setRightIAxisValueFormatter(mRightAxisValue)
//                .setXIAxisValueFormatter(mXAxisValue)
//                .initLineChart();
//        mChart.getDescription().setEnabled(false);
//        mChart.setBackgroundColor(Color.WHITE);
//        mChart.setDrawGridBackground(false);
//        mChart.setDrawBarShadow(false);
//        mChart.setHighlightFullBarEnabled(false);
//        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
//                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE,
//                CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE,
//                CombinedChart.DrawOrder.SCATTER
//        });

//        Legend l = mChart.getLegend();
//        l.setWordWrapEnabled(true);
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//
//        YAxis rightAxis = mChart.getAxisRight();
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setAxisMinimum(0f);// this replaces setStartAtZero(true)
//
//        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setDrawGridLines(false);
//        leftAxis.setAxisMinimum(0f);// this replaces setStartAtZero(true)
//
//        XAxis xAxis = mChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
//        xAxis.setAxisMinimum(0f);
//        xAxis.setGranularity(1f);
//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return value + "";
//            }
//        });

        CombinedData data = new CombinedData();

        data.setData(generateLineData());
        data.setData(generateCandleData());//蜡烛图
//        data.setValueTypeface(mTfLight);

//        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        mChart.setData(data);
        mChart.invalidate();

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
