package com.chrisplus.muselogger;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by chrisplus on 6/9/15.
 */
public class ChartView extends LineChart {

    public final static int MAX_VISIBLE_RANGE = 30;

    private LineData data;

    private LineDataSet set;

    public ChartView(Context context) {
        super(context);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setChart(String chartName, int color) {

        /*binding data*/
        data = new LineData();
        set = initSet(chartName, color);
        data.addDataSet(set);

        setData(data);
        setDescription(chartName);
        getXAxis().setDrawLabels(false);
        getAxisRight().setEnabled(false);
        getLegend().setEnabled(false);
        setPinchZoom(false);
        setDragEnabled(false);
        setTouchEnabled(false);
        setVisibleXRange(MAX_VISIBLE_RANGE);

    }

    public void addEntry(float value) {
        data.addXValue(set.getEntryCount() + "");
        data.addEntry(new Entry(value, set.getEntryCount()), 0);
        notifyDataSetChanged();
        moveViewToX(data.getXValCount() - MAX_VISIBLE_RANGE - 1);
        invalidate();
    }

    private LineDataSet initSet(String name, int color) {
        LineDataSet set = new LineDataSet(null, name);
        set.setDrawValues(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(color);
        set.setDrawCircles(false);
        set.setLineWidth(2f);
        set.setDrawValues(false);
        return set;
    }
}
