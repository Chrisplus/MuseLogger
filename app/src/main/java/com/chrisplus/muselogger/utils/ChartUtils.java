package com.chrisplus.muselogger.utils;

import com.github.mikephil.charting.data.LineDataSet;

/**
 * Created by chrisplus on 4/8/16.
 */
public class ChartUtils {
    public static final String TAG = ChartUtils.class.getSimpleName();

    public static final String DEFAULT_LINESET_NAME = "Unknown Line";

    private ChartUtils() {

    }

    public static LineDataSet createLine(int color) {
        LineDataSet lineDataSet = new LineDataSet(null, DEFAULT_LINESET_NAME);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setColor(color);
        lineDataSet.setLineWidth(2f);
        return lineDataSet;
    }


}
