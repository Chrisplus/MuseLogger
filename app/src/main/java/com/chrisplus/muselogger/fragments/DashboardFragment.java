package com.chrisplus.muselogger.fragments;

import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseDataPacket;
import com.chrisplus.muselogger.MuseHelper;
import com.chrisplus.muselogger.R;
import com.chrisplus.muselogger.views.DashButtonView;
import com.chrisplus.muselogger.views.IndicatorView;
import com.chrisplus.muselogger.views.SquareArcView;
import com.orhanobut.logger.Logger;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chrisplus on 22/7/16.
 */
public class DashboardFragment extends Fragment implements MuseMonitor {

    public static final String TAG = DashboardFragment.class.getSimpleName();

    @BindView(R.id.dashboard_battery_indicator)
    public SquareArcView batteryIndicator;

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

    private Context context;
    private Muse currentMuse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setTargetedMuse(Muse muse) {
        if (muse != null) {
            currentMuse = muse;
            listenMuseData(currentMuse);
        }
    }

    private void listenMuseData(final Muse muse) {
        Logger.t(TAG).d("start listen muse data at dashboard fragment");
        MuseHelper.getInstance(context).observeMuseData(muse).subscribeOn(Schedulers.io()).observeOn
                (AndroidSchedulers.mainThread()).subscribe(new Action1<MuseDataPacket>() {
            @Override
            public void call(MuseDataPacket museDataPacket) {
                processMuseData(museDataPacket);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    private void processMuseData(MuseDataPacket museDataPacket) {
        switch (museDataPacket.packetType()) {
            case BATTERY:
                batteryIndicator.setProgress(museDataPacket.values().get(0).intValue());
                break;
            case HSI_PRECISION:
                setIndicators(museDataPacket.values().get(0).intValue(), museDataPacket.values()
                                .get(1).intValue(), museDataPacket.values().get(2).intValue(),
                        museDataPacket.values().get(3).intValue());
                break;
        }
    }

    private void setIndicators(int tp9Level, int af7Level, int af8Level, int tp10Level) {
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
