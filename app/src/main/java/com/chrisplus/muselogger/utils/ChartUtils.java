package com.chrisplus.muselogger.utils;

import com.github.mikephil.charting.data.LineDataSet;

/**
 * Created by chrisplus on 4/8/16.
 */
public class ChartUtils {
    private ChartUtils() {

    }

    public static LineDataSet createLine(String name, int color) {
        LineDataSet lineDataSet = new LineDataSet(null, name);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setColor(color);

        return lineDataSet;
    }


}
