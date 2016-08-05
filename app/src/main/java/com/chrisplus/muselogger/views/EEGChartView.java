package com.chrisplus.muselogger.views;

import com.chrisplus.muselogger.R;
import com.chrisplus.muselogger.utils.ChartUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by chrisplus on 4/8/16.
 */
public class EEGChartView extends LineChart {
    public static final String TAG = EEGChartView.class.getSimpleName();

//    private LineDataSet tp9Raw;
//    private LineDataSet af7Raw;
//    private LineDataSet af8Raw;
//    private LineDataSet tp10Raw;
//
//    private LineDataSet alphaAbsolute;
//    private LineDataSet betaAbsolute;
//    private LineDataSet deltaAbsolute;
//    private LineDataSet gammaAbsolute;
//
//    private LineDataSet alphaRelative;
//    private LineDataSet betaRelative;
//    private LineDataSet deltaRelative;
//    private LineDataSet gammaRelative;

    private LineDataSet lineSetA;
    private LineDataSet lineSetB;
    private LineDataSet lineSetC;
    private LineDataSet lineSetD;
    private ChartMode currentMode = ChartMode.UNKNOWN;

    public enum ChartMode {
        RAW_EEG,
        BAND_ABSOLUTE,
        BAND_RELATIVE,
        UNKNOWN
    }

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

    public void plot(float val_1, float val_2, float val_3, float val_4) {
        lineSetA.addEntry(new Entry(lineSetA.getEntryCount(), val_1));
        lineSetB.addEntry(new Entry(lineSetB.getEntryCount(), val_2));
        lineSetC.addEntry(new Entry(lineSetC.getEntryCount(), val_3));
        lineSetD.addEntry(new Entry(lineSetD.getEntryCount(), val_4));

        getData().notifyDataChanged();
        notifyDataSetChanged();
        setVisibleXRangeMaximum(110);
        moveViewToX(lineSetA.getEntryCount());
    }


    public void clear() {
        getData().clearValues();
    }

    public void setMode(ChartMode newMode) {
        if (currentMode != newMode) {
            currentMode = newMode;

            if (lineSetA == null) {
                lineSetA = ChartUtils.createLine(getResources().getColor(R.color
                        .md_deep_orange_500));
            }

            if (lineSetB == null) {
                lineSetB = ChartUtils.createLine(getResources().getColor(R.color.md_green_500));
            }

            if (lineSetC == null) {
                lineSetC = ChartUtils.createLine(getResources().getColor(R.color.md_brown_500));
            }

            if (lineSetD == null) {
                lineSetD = ChartUtils.createLine(getResources().getColor(R.color.md_purple_500));
            }

            lineSetA.clear();
            lineSetB.clear();
            lineSetC.clear();
            lineSetD.clear();

            switch (currentMode) {
                case RAW_EEG:
                    lineSetA.setLabel("TP9");
                    lineSetB.setLabel("AF7");
                    lineSetC.setLabel("AF8");
                    lineSetD.setLabel("TP10");
                    break;
                case BAND_ABSOLUTE:
                    lineSetA.setLabel("Alpha Absolute");
                    lineSetB.setLabel("Beta Absolute");
                    lineSetC.setLabel("Delta Absolute");
                    lineSetD.setLabel("Gamma Absolute");
                    break;
                case BAND_RELATIVE:
                    lineSetA.setLabel("ALpha Relative");
                    lineSetB.setLabel("Beta Relative");
                    lineSetC.setLabel("Delta Relative");
                    lineSetD.setLabel("Gamma Relative");
                    break;
                default:
                    lineSetA.setLabel("Erro Lable A");
                    lineSetB.setLabel("Erro Lable B");
                    lineSetC.setLabel("Erro Lable C");
                    lineSetD.setLabel("Erro Lable D");
            }

            if (getData() == null) {
                setData(new LineData());
            }

            getData().clearValues();
            getData().addDataSet(lineSetA);
            getData().addDataSet(lineSetB);
            getData().addDataSet(lineSetC);
            getData().addDataSet(lineSetD);
        }
    }
}
