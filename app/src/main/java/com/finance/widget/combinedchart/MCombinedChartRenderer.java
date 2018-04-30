package com.finance.widget.combinedchart;

import android.graphics.Canvas;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.renderer.BubbleChartRenderer;
import com.github.mikephil.charting.renderer.CandleStickChartRenderer;
import com.github.mikephil.charting.renderer.DataRenderer;
import com.github.mikephil.charting.renderer.ScatterChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Renderer class that is responsible for rendering multiple different data-types.
 */
public class MCombinedChartRenderer extends DataRenderer {

    /**
     * all rederers for the different kinds of data this combined-renderer can draw
     */
    protected List<DataRenderer> mRenderers = new ArrayList<DataRenderer>(5);

    protected WeakReference<MCombinedChart> mChart;

    private WeakReference<OnDrawCompletion> drawCompletion;

    public MCombinedChartRenderer(MCombinedChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        mChart = new WeakReference<MCombinedChart>(chart);
        createRenderers();
    }

    /**
     * Creates the renderers needed for this combined-renderer in the required order. Also takes the DrawOrder into
     * consideration.
     */
    public void createRenderers() {

        mRenderers.clear();

        MCombinedChart chart = (MCombinedChart) mChart.get();
        if (chart == null)
            return;

        MCombinedChart.DrawOrder[] orders = chart.getDrawOrder();

        for (MCombinedChart.DrawOrder order : orders) {

            switch (order) {
                case BAR:
                    if (chart.getBarData() != null)
                        mRenderers.add(new BarChartRenderer(chart, mAnimator, mViewPortHandler));
                    break;
                case BUBBLE:
                    if (chart.getBubbleData() != null)
                        mRenderers.add(new BubbleChartRenderer(chart, mAnimator, mViewPortHandler));
                    break;
                case LINE:
                    if (chart.getLineData() != null) {
                        mRenderers.add(new MLineChartRenderer(chart, mAnimator, mViewPortHandler));
//                        MLineChartRenderer renderer = new MLineChartRenderer(chart, mAnimator, mViewPortHandler);
//                        mRenderers.add(renderer);
//                        if (mChart.get() != null)
//                            mChart.get().setMcombinedchartRenderer(renderer);
                    }
                    break;
                case CANDLE:
                    if (chart.getCandleData() != null)
                        mRenderers.add(new CandleStickChartRenderer(chart, mAnimator, mViewPortHandler));
                    break;
                case SCATTER:
                    if (chart.getScatterData() != null)
                        mRenderers.add(new ScatterChartRenderer(chart, mAnimator, mViewPortHandler));
                    break;
            }
        }
    }

    @Override
    public void initBuffers() {
        for (DataRenderer renderer : mRenderers)
            renderer.initBuffers();
    }

    @Override
    public void drawData(Canvas c) {
        for (DataRenderer renderer : mRenderers)
            renderer.drawData(c);
    }

    @Override
    public void drawValues(Canvas c) {
        for (DataRenderer renderer : mRenderers)
            renderer.drawValues(c);
    }

    @Override
    public void drawExtras(Canvas c) {
        if (mRenderers == null || mRenderers.isEmpty()) {
            return;
        }
        for (DataRenderer renderer : mRenderers)
            renderer.drawExtras(c);
        //绘制完成，刷新其他控件
        OnDrawCompletion completion = drawCompletion == null ? null : drawCompletion.get();
        if (completion == null) return;
        ChartData data = getChartData(mRenderers.get(0), mChart.get());
        if (data == null) return;
        IDataSet iDataSet = null;
        if (data instanceof LineData) {
            iDataSet = ((LineData) data).getDataSets().get(0);
        } else if (data instanceof BarData) {
            iDataSet = ((BarData) data).getDataSets().get(0);
        } else if (data instanceof BubbleData) {
            iDataSet = ((BubbleData) data).getDataSets().get(0);
        } else if (data instanceof CandleData) {
            iDataSet = ((CandleData) data).getDataSets().get(0);
        } else if (data instanceof ScatterData) {
            iDataSet = ((ScatterData) data).getDataSets().get(0);
        }
        if (iDataSet == null) return;
        Entry entry1 = iDataSet.getEntryForIndex(iDataSet.getEntryCount() - 1);//开奖点
//        Entry entry2 = iDataSet.getEntryForIndex(iDataSet.getEntryCount() - 2);//截止购买点
//        Entry entry3 = iDataSet.getEntryForIndex(iDataSet.getEntryCount() - 3);//绘制的最后一个点
        if (entry1 == null) return;
        completion.completion(entry1, iDataSet);
    }

    protected List<Highlight> mHighlightBuffer = new ArrayList<Highlight>();

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {
        MCombinedChart chart = mChart.get();
        if (chart == null) return;
        for (DataRenderer renderer : mRenderers) {
            ChartData data = getChartData(renderer, chart);
            int dataIndex = data == null ? -1 : ((CombinedData) chart.getData()).getAllData().indexOf(data);
            mHighlightBuffer.clear();
            for (Highlight h : indices) {
                if (h.getDataIndex() == dataIndex || h.getDataIndex() == -1)
                    mHighlightBuffer.add(h);
            }
            renderer.drawHighlighted(c, mHighlightBuffer.toArray(new Highlight[mHighlightBuffer.size()]));
        }
    }

    private ChartData getChartData(DataRenderer renderer, MCombinedChart chart) {
        if (renderer == null || chart == null) return null;
        ChartData data = null;
        if (renderer instanceof BarChartRenderer)
            data = chart.getBarData();
        else if (renderer instanceof MLineChartRenderer)
            data = chart.getLineData();
        else if (renderer instanceof CandleStickChartRenderer)
            data = chart.getCandleData();
        else if (renderer instanceof ScatterChartRenderer)
            data = chart.getScatterData();
        else if (renderer instanceof BubbleChartRenderer)
            data = chart.getBubbleData();
        return data;
    }

    /**
     * Returns the sub-renderer object at the specified index.
     *
     * @param index
     * @return
     */
    public DataRenderer getSubRenderer(int index) {
        if (index >= mRenderers.size() || index < 0)
            return null;
        else
            return mRenderers.get(index);
    }

    /**
     * Returns all sub-renderers.
     *
     * @return
     */
    public List<DataRenderer> getSubRenderers() {
        return mRenderers;
    }

    public void setSubRenderers(List<DataRenderer> renderers) {
        this.mRenderers = renderers;
    }


    public void setOnDrawCompletion(OnDrawCompletion drawCompletion) {
        this.drawCompletion = new WeakReference<OnDrawCompletion>(drawCompletion);
    }
}
