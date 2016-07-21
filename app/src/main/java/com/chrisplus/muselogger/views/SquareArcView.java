package com.chrisplus.muselogger.views;

import com.github.lzyzsd.circleprogress.ArcProgress;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by chrisplus on 19/7/16.
 */
public class SquareArcView extends ArcProgress {

    public SquareArcView(Context context) {
        super(context);
    }

    public SquareArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int size;
        if (widthMode == MeasureSpec.EXACTLY && widthSize > 0) {
            size = widthSize;
        } else if (heightMode == MeasureSpec.EXACTLY && heightSize > 0) {
            size = heightSize;
        } else {
            size = widthSize < heightSize ? widthSize : heightSize;
        }

        int finalMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        super.onMeasure(finalMeasureSpec, finalMeasureSpec);
    }
}
