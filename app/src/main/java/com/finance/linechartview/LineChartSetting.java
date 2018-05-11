package com.finance.linechartview;

import android.app.Activity;
import android.graphics.Color;

import com.finance.R;
import com.finance.common.Constants;
import com.finance.interfaces.IChartSetting;
import com.finance.widget.combinedchart.MCombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * MLineChart配置
 */
public class LineChartSetting implements IChartSetting {

    private Activity mActivity;
    private YAxis leftAxis, rightAxis;
    private XAxis xAxis;
    private IAxisValueFormatter mXIAxisValueFormatter;//X轴标签显示格式化
    private IAxisValueFormatter mRightIAxisValueFormatter;//右边轴标签显示格式化

    public LineChartSetting(Activity activity) {
        this.mActivity = activity;
    }

    //在调用initLineChart之前调用才会生效
    @Override
    public LineChartSetting setRightIAxisValueFormatter(IAxisValueFormatter rightIAxisValueFormatter) {
        mRightIAxisValueFormatter = rightIAxisValueFormatter;
        return this;
    }

    //在调用initLineChart之前调用才会生效
    @Override
    public LineChartSetting setXIAxisValueFormatter(IAxisValueFormatter XIAxisValueFormatter) {
        mXIAxisValueFormatter = XIAxisValueFormatter;
        return this;
    }

    /***************************初始化LineChart******************************/

    @Override
    public LineChartSetting initLineChart(MCombinedChart mChart, boolean isOffsets) {
        mChart.setDrawGridBackground(false);//设置是否画网格背景
        mChart.setDragDecelerationEnabled(false);//继续滚动后润色
        mChart.getDescription().setEnabled(true);//描述文本
        mChart.getDescription().setText("");//设置描述文本
        mChart.setNoDataText("");//没有数据显示的文本
        mChart.setTouchEnabled(false);//允许手势触摸
        mChart.setDragEnabled(false);//手势拖动走势图
        mChart.setScaleEnabled(true);//拖动放缩
        mChart.setScaleXEnabled(true);
        mChart.setScaleYEnabled(true);
        mChart.setAutoScaleMinMaxEnabled(true);//自动缩放
//        mChart.setPinchZoom(false);
        //偏移量
//        mChart.setExtraOffsets(0, 0, 0, 0);
        if (isOffsets) {
            int top = mActivity.getResources().getDimensionPixelOffset(R.dimen.dp_20);
            mChart.setViewPortOffsets(0, top, 0, 0);
        }
//        mChart.setPinchZoom(true);//放缩 作用同上
        mChart.setDrawBorders(false);
//        mChart.setGridBackgroundColor(Color.WHITE);

// 当前统计图表中最多在x轴坐标线上显示的总量
//        mChart.setVisibleXRangeMaximum(100);
//        mChart.setMaxVisibleValueCount(5);
        mChart.animateX(Constants.XANIMATION);//X轴显示动画
        mChart.animateY(0);
        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        //设置折线的描述的样式
        l.setForm(Legend.LegendForm.NONE);
        //图标描述文字颜色
        l.setTextColor(mActivity.getResources().getColor(R.color.transparent));
        initLeftAxis(mChart);//初始化左轴
        initRightAxis(mChart);//初始化右轴
        initXAxis(mChart);//初始化X轴
        initMarkerView();//初始化MarkerView
        return this;
    }

    /***************************初始化MarkerView******************************/

    public void initMarkerView() {
//        MyMarkerView mv = new MyMarkerView(mActivity, R.layout.custom_marker_view);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv); // Set the marker to the chart
    }

    /***************************初始化线******************************/

    private void initLeftAxis(MCombinedChart mChart) {
        leftAxis = mChart.getAxisLeft();
        leftAxis.setEnabled(false);
//        leftAxis.setAxisLineWidth(1f);//边框的宽度
//        leftAxis.setDrawAxisLine(false);//是否绘制轴线
//        leftAxis.setDrawLabels(false);//是否绘制标签
//        leftAxis.setLabelCount(9, true);
//        //左边边框的颜色
//        leftAxis.setAxisLineColor(mActivity.getResources().getColor(R.color.form_line_color));
//        leftAxis.setGranularityEnabled(false);
//        leftAxis.setCenterAxisLabels(true);
//        leftAxis.setDrawGridLines(false);//是否绘制网格
//        //网格设置
//        leftAxis.setGridColor(mActivity.getResources().getColor(R.color.form_line_color));
//        leftAxis.setGridLineWidth(1f);
//        //自定义标签显示格式
//        leftAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return ""+value;
//            }
//        });
//        //虚线时：单个虚线长度，虚线间隔，偏移量
//        leftAxis.enableGridDashedLine(10f, 10f, 0f);
//        leftAxis.disableGridDashedLine();//禁用虚线
//        leftAxis.resetAxisMaximum();//最大值重置
//        leftAxis.resetAxisMinimum();//最大值重置
    }

    private void initRightAxis(MCombinedChart mChart) {
        rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(true);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setAxisLineWidth(1f);//边框的宽度
        rightAxis.setDrawLabels(true);
        rightAxis.setDrawGridLines(true);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        rightAxis.setGridLineWidth(1f);
        rightAxis.setLabelCount(7, false);
//        rightAxis.setAxisMinimum(0f); //
//        rightAxis.setAxisMaximum(10f); //
//        rightAxis.setDrawLimitLinesBehindData(true);
        //标签颜色
        rightAxis.setTextColor(getColor(R.color.form_value_color));
        rightAxis.setValueFormatter(mRightIAxisValueFormatter);
        rightAxis.setGridColor(getColor(R.color.form_line_color));
        //虚线时：单个虚线长度，虚线间隔，偏移量
        rightAxis.enableGridDashedLine(10f, 0f, 0f);
        rightAxis.setAxisLineColor(getColor(R.color.transparent));
    }

    private void initXAxis(MCombinedChart mChart) {
        xAxis = mChart.getXAxis();
        //禁用x轴，设置为false后该轴任何部分都不会绘制,所以即使再设置xAxis.setDrawAxisLine(true);也不会被绘制
        xAxis.setEnabled(true);//是否绘制X轴线
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);//设置x轴位置，有四个属性。
        //轴上显示的标签
        xAxis.setValueFormatter(mXIAxisValueFormatter);
        xAxis.setAxisMinimum(0);//X轴自定义最小值
        xAxis.setDrawGridLines(true);//绘制网格线，默认为true
        //网格线绘制模式
        xAxis.enableGridDashedLine(10f, 0f, 0f);
        xAxis.setGridLineWidth(1f);//网格线宽度单位dp
        xAxis.setGridColor(getColor(R.color.form_line_color));//设置该轴的网格线颜色。
        //轴线颜色
        xAxis.setAxisLineColor(getColor(R.color.transparent));
        xAxis.setDrawLabels(false);//需要绘制标签
//        xAxis.setLabelCount(6, true);//设置x轴显示的标签个数
//        xAxis.setTextColor(getColor(R.color.form_value_color));
//        xAxis.setGranularity(10);
//        xAxis.setSpaceMax(10);
    }

    private int getColor(int colorId) {
        return mActivity.getResources().getColor(colorId);
    }

    private int getColor(String colorStr) {
        return Color.parseColor(colorStr);
    }

}
