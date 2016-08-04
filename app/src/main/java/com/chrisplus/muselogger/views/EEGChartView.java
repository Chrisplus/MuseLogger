package com.chrisplus.muselogger.views;

import com.chrisplus.muselogger.R;
import com.chrisplus.muselogger.utils.ChartUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by chrisplus on 4/8/16.
 */
public class EEGChartView extends LineChart {
    public static final String TAG = EEGChartView.class.getSimpleName();

    private LineDataSet tp9Raw;
    private LineDataSet af7Raw;
    private LineDataSet af8Raw;
    private LineDataSet tp10Raw;

    private LineDataSet alphaAbsolute;
    private LineDataSet betaAbsolute;
    private LineDataSet deltaAbsolute;
    private LineDataSet gammaAbsolute;

    private LineDataSet alphaRelative;
    private LineDataSet betaRelative;
    private LineDataSet deltaRelative;
    private LineDataSet gammaRelative;
    

    public EEGChartView(Context context) {
        super(context);
    }

    public EEGChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EEGChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void configRealtimeChart() {
        setNoDataText(getContext().getString(R.string.instant_no_data_tips));
        setTouchEnabled(false);
        setScaleEnabled(false);
        setDragEnabled(false);
        setPinchZoom(false);
        setDrawGridBackground(true);
        getXAxis().setDrawLabels(false);
        getAxisRight().setEnabled(false);
    }

    public void plotByBandRelative(float alphaValue, float betaValue, float deltaValue, float
            gammaValue) {
        if (alphaRelative == null) {
            alphaRelative = ChartUtils.createLine("Alpha Relative", getResources().getColor(R
                    .color.md_amber_500));
        }

        if (betaRelative == null) {
            betaRelative = ChartUtils.createLine("Beta Relative", getResources().getColor(R.color
                    .md_deep_purple_500));
        }

        if (deltaRelative == null) {
            deltaRelative = ChartUtils.createLine("Delta Relative", getResources().getColor(R
                    .color.md_red_500));
        }

        if (gammaRelative == null) {
            gammaRelative = ChartUtils.createLine("Gamma Relative", getResources().getColor(R
                    .color.md_indigo_500));
        }


        alphaRelative.addEntry(new Entry(alphaRelative.getEntryCount(), alphaValue));
        betaRelative.addEntry(new Entry(betaRelative.getEntryCount(), betaValue));
        deltaRelative.addEntry(new Entry(deltaRelative.getEntryCount(), deltaValue));
        gammaRelative.addEntry(new Entry(gammaRelative.getEntryCount(), gammaValue));
    }


    public void plotByBandAbsolute(float alphaValue, float betaValue, float deltaValue, float
            gammaValue) {

        if (alphaAbsolute == null) {
            alphaAbsolute = ChartUtils.createLine("Alpha Absolute", getResources().getColor(R
                    .color.md_blue_500));
        }

        if (betaAbsolute == null) {
            betaAbsolute = ChartUtils.createLine("Beta Absolute", getResources().getColor(R.color
                    .md_green_500));
        }

        if (deltaAbsolute == null) {
            deltaAbsolute = ChartUtils.createLine("Delta Absolute", getResources().getColor(R
                    .color.md_lime_500));
        }

        if (gammaAbsolute == null) {
            gammaAbsolute = ChartUtils.createLine("Gamma Absolute", getResources().getColor(R
                    .color.md_indigo_500));
        }


        alphaAbsolute.addEntry(new Entry(alphaAbsolute.getEntryCount(), alphaValue));
        betaAbsolute.addEntry(new Entry(betaAbsolute.getEntryCount(), betaValue));
        deltaAbsolute.addEntry(new Entry(deltaAbsolute.getEntryCount(), deltaValue));
        gammaAbsolute.addEntry(new Entry(gammaAbsolute.getEntryCount(), gammaValue));
    }

    public void plotByChannel(float tp9Value, float af7Value, float af8Value, float tp10Value) {
        if (tp9Raw == null) {
            tp9Raw = ChartUtils.createLine("TP9", getResources().getColor(R.color.md_amber_500));
        }

        if (af7Raw == null) {
            af7Raw = ChartUtils.createLine("AF7", getResources().getColor(R.color
                    .md_deep_orange_500));
        }

        if (af8Raw == null) {
            af8Raw = ChartUtils.createLine("AF8", getResources().getColor(R.color.md_brown_500));
        }

        if (tp10Raw == null) {
            tp10Raw = ChartUtils.createLine("TP10", getResources().getColor(R.color.md_pink_500));
        }

        tp9Raw.addEntry(new Entry(tp9Raw.getEntryCount(), tp9Value));
        af7Raw.addEntry(new Entry(af7Raw.getEntryCount(), af7Value));
        af8Raw.addEntry(new Entry(af8Raw.getEntryCount(), af8Value));
        tp10Raw.addEntry(new Entry(tp10Raw.getEntryCount(), tp10Value));
    }

    public void clear() {
    }


}
