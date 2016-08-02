package com.chrisplus.muselogger.fragments;

import com.choosemuse.libmuse.Muse;
import com.chrisplus.muselogger.R;
import com.chrisplus.muselogger.views.IndicatorArrayView;
import com.github.mikephil.charting.charts.LineChart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chrisplus on 2/8/16.
 */
public class InstantViewFragment extends Fragment implements MuseMonitor {

    public static final String TAG = InstantViewFragment.class.getSimpleName();

    @BindView(R.id.instant_indicator_array)
    public IndicatorArrayView indicatorArrayView;

    @BindView(R.id.instant_chart)
    public LineChart charView;

    public static InstantViewFragment newInstance(Muse muse) {
        InstantViewFragment fragment = new InstantViewFragment();
        fragment.setTargetedMuse(muse);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instant_chart, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setTargetedMuse(Muse muse) {

    }
}
