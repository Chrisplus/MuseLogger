package com.chrisplus.muselogger.fragments;

import com.chrisplus.muselogger.R;
import com.chrisplus.muselogger.views.DashButtonView;
import com.chrisplus.muselogger.views.IndicatorView;
import com.chrisplus.muselogger.views.SquareArcView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chrisplus on 22/7/16.
 */
public class DashboardFragment extends Fragment {
    public static final String TAG = DashboardFragment.class.getSimpleName();

    @BindView(R.id.dashboard_battery_indicator)
    public SquareArcView batteryIndicator;

    @BindView(R.id.dashboard_tp9_indicator)
    public IndicatorView tpIndicator;

    @BindView(R.id.dashboard_af7_indicator)
    public IndicatorView af7Indicator;

    @BindView(R.id.dashboard_fpz_indicator)
    public IndicatorView fpzIndicator;

    @BindView(R.id.dashboard_af8_indicator)
    public IndicatorView af8Indicator;

    @BindView(R.id.dashboard_tp10_indicator)
    public IndicatorView tp10Indicator;

    @BindView(R.id.dashboard_dash_instant)
    public DashButtonView instantBtn;

    @BindView(R.id.dashboard_dash_emotion)
    public DashButtonView emotionBtn;

    @BindView(R.id.dashboard_dash_logviewer)
    public DashButtonView logviewBtn;

    @BindView(R.id.dashboard_dash_settings)
    public DashButtonView settingBtn;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
