package com.chrisplus.muselogger.views;

import com.chrisplus.muselogger.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chrisplus on 2/8/16.
 */
public class IndicatorArrayView extends LinearLayout {

    @BindView(R.id.dashboard_tp9_indicator)
    public IndicatorView tp9Indicator;

    @BindView(R.id.dashboard_af7_indicator)
    public IndicatorView af7Indicator;

    @BindView(R.id.dashboard_fpz_indicator)
    public IndicatorView fpzIndicator;

    @BindView(R.id.dashboard_af8_indicator)
    public IndicatorView af8Indicator;

    @BindView(R.id.dashboard_tp10_indicator)
    public IndicatorView tp10Indicator;

    public IndicatorArrayView(Context context) {
        super(context);
        initView();
    }

    public IndicatorArrayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public IndicatorArrayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_indicator_array, this, true);
        ButterKnife.bind(this);
    }

    public void setIndicators(int tp9Level, int af7Level, int af8Level, int tp10Level) {
        setIndicator(tp9Indicator, tp9Level);
        setIndicator(tp10Indicator, tp10Level);
        setIndicator(af7Indicator, af7Level);
        setIndicator(af8Indicator, af8Level);
        setIndicator(fpzIndicator, 1);
    }

    private void setIndicator(IndicatorView indicator, int level) {
        switch (level) {
            case 1:
                indicator.setFull();
                break;
            case 2:
                indicator.setHalf();
                break;
            default:
                indicator.setEmpty();
        }
    }

}
