package com.chrisplus.muselogger.fragments;

import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseDataPacket;
import com.chrisplus.muselogger.MuseHelper;
import com.chrisplus.muselogger.R;
import com.chrisplus.muselogger.views.EEGChartView;
import com.chrisplus.muselogger.views.IndicatorArrayView;
import com.orhanobut.logger.Logger;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chrisplus on 2/8/16.
 */
public class InstantViewFragment extends Fragment implements MuseMonitor {

    public static final String TAG = InstantViewFragment.class.getSimpleName();

    @BindView(R.id.instant_indicator_array)
    public IndicatorArrayView indicatorArrayView;

    @BindView(R.id.instant_chart)
    public EEGChartView charView;

    private Context context;
    private Muse currentMuse;
    private Subscription museDataSubscription;

    public static InstantViewFragment newInstance(Muse muse) {
        InstantViewFragment fragment = new InstantViewFragment();
        fragment.currentMuse = muse;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instant_chart, container, false);
        ButterKnife.bind(this, view);

        charView.configRealtimeChart();
        charView.setMode(EEGChartView.ChartMode.RAW_EEG);

        return view;
    }

    @Override
    public void setTargetedMuse(Muse muse) {
        if (muse != null) {
            currentMuse = muse;
            if (museDataSubscription != null && !museDataSubscription.isUnsubscribed()) {
                museDataSubscription.unsubscribe();
            }

            listenMuseData(currentMuse);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentMuse != null) {
            setTargetedMuse(currentMuse);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (museDataSubscription != null) {
            museDataSubscription.unsubscribe();
        }
    }

    private void listenMuseData(Muse muse) {
        Logger.t(TAG).d("start listen muse data at instant view fragment");
        museDataSubscription = MuseHelper.getInstance(context)
                .observeMuseData(muse)
                .subscribeOn(Schedulers.io())
                .sample(10, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MuseDataPacket>() {
                    @Override
                    public void call(MuseDataPacket museDataPacket) {
                        processMuseData(museDataPacket);
                    }
                });
    }

    private void processMuseData(MuseDataPacket museDataPacket) {
        switch (museDataPacket.packetType()) {
            case HSI_PRECISION:
                indicatorArrayView.setIndicators(museDataPacket.values().get(0).intValue(),
                        museDataPacket.values()
                                .get(1).intValue(), museDataPacket.values().get(2).intValue(),
                        museDataPacket.values().get(3).intValue());
                break;
            case EEG:
                plotEEG(museDataPacket);
                break;
        }
    }


    private void plotEEG(MuseDataPacket museDataPacket) {

        charView.plot(museDataPacket.values().get(0).floatValue(), museDataPacket.values()
                        .get(1).floatValue(), museDataPacket.values().get(2).floatValue(),
                museDataPacket.values().get(3).floatValue());

    }


}
