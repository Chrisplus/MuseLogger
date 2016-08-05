package com.chrisplus.muselogger.fragments;

import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseDataPacket;
import com.chrisplus.muselogger.MainActivity_N;
import com.chrisplus.muselogger.MuseHelper;
import com.chrisplus.muselogger.R;
import com.chrisplus.muselogger.views.DashButtonView;
import com.chrisplus.muselogger.views.IndicatorArrayView;
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

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
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

    @BindView(R.id.dashboard_indicator_array)
    public IndicatorArrayView indicatroArrayView;

    @BindView(R.id.dashboard_dash_instant)
    public DashButtonView instantBtn;

    @BindView(R.id.dashboard_dash_emotion)
    public DashButtonView emotionBtn;

    @BindView(R.id.dashboard_dash_logviewer)
    public DashButtonView logviewBtn;

    @BindView(R.id.dashboard_dash_settings)
    public DashButtonView settingBtn;

    @OnClick(R.id.dashboard_dash_instant)
    public void openInstantView(DashButtonView view) {
        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in_center, R.anim
                .fade_out_center, R.anim.fade_in_center, R.anim.fade_out_center).replace(R.id
                .main_container, InstantViewFragment
                .newInstance(currentMuse), MainActivity_N.TAG_CURRENT_FRAGMENT).addToBackStack
                (InstantViewFragment.class.getSimpleName
                        ()).commit();
    }

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    private Context context;
    private Muse currentMuse;
    private Subscription museDataSubscription;

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
            if (museDataSubscription != null && !museDataSubscription.isUnsubscribed()) {
                museDataSubscription.unsubscribe();
            }

            listenMuseData(currentMuse);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (museDataSubscription != null) {
            museDataSubscription.unsubscribe();
        }
    }

    private void listenMuseData(final Muse muse) {
        Logger.t(TAG).d("start listen muse data at dashboard fragment");
        museDataSubscription = MuseHelper.getInstance(context)
                .observeMuseData(muse)
                .subscribeOn(Schedulers.io())
                .sample(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MuseDataPacket>() {
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
                indicatroArrayView.setIndicators(museDataPacket.values().get(0).intValue(),
                        museDataPacket.values()
                                .get(1).intValue(), museDataPacket.values().get(2).intValue(),
                        museDataPacket.values().get(3).intValue());
                break;
        }
    }


}
