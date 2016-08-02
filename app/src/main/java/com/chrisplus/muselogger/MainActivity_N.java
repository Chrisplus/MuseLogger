package com.chrisplus.muselogger;

import com.choosemuse.libmuse.ConnectionState;
import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseConnectionPacket;
import com.chrisplus.muselogger.adapters.MuseListAdapter;
import com.chrisplus.muselogger.fragments.DashboardFragment;
import com.chrisplus.muselogger.fragments.MuseMonitor;
import com.chrisplus.muselogger.utils.WidgetUtils;
import com.chrisplus.muselogger.views.ActionBarView;
import com.chrisplus.muselogger.views.MuseListHeaderView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.logger.Logger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chrisplus on 19/7/16.
 */
public class MainActivity_N extends AppCompatActivity {

    public static final String TAG = MainActivity_N.class.getSimpleName();
    public static final String TAG_CURRENT_FRAGMENT = "muselogger_current_fragment";

    private ActionBarView actionBarView;
    private MuseListHeaderView headerView;

    private DialogPlus museDialog;
    private MuseListAdapter museListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        setupViews();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                DashboardFragment.newInstance(), TAG_CURRENT_FRAGMENT).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MuseHelper.getInstance(this).stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listenMuseList();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void setupViews() {
        actionBarView = new ActionBarView(this);
        actionBarView.setStatusOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMuseDialog();
            }
        });

        headerView = new MuseListHeaderView(this);
        setupActionBar(actionBarView);
        museListAdapter = new MuseListAdapter(this);
    }

    private void setupActionBar(ActionBarView abView) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setCustomView(abView);
        }
    }

    private void listenMuseList() {
        MuseHelper.getInstance(this).observeMuseList().subscribeOn(Schedulers.io()).observeOn
                (AndroidSchedulers.mainThread()).subscribe(new Action1<List<Muse>>() {
            @Override
            public void call(List<Muse> muses) {
                museListAdapter.setMuses(muses);
                actionBarView.setDeviceStatus(ActionBarView.DeviceStatus.DISCONNECTED);
            }
        });
    }


    private void listenMuseData(Muse muse) {
        MuseMonitor museMonitor = getCurrentMuseMonitor();

        //TODO add fragment interface
        if (museMonitor != null) {
            museMonitor.setTargetedMuse(muse);
        }
    }

    private void listenMuseConnectionStatus(final Muse muse, final int position) {
        MuseHelper.getInstance(this).observeMuseConnectionStatus(muse).subscribeOn(Schedulers.io
                ()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<MuseConnectionPacket>() {
            @Override
            public void call(MuseConnectionPacket museConnectionPacket) {
                Logger.t(TAG).d("on muse connection status changed " + museConnectionPacket
                        .getCurrentConnectionState().name());
                museListAdapter.updateMuse(position, museConnectionPacket);

                if (museConnectionPacket.getCurrentConnectionState() == ConnectionState.CONNECTED) {
                    toggleMuseDialog();
                    actionBarView.setDeviceStatus(ActionBarView.DeviceStatus.CONNECTED);
                    listenMuseData(muse);

                } else if (museConnectionPacket.getCurrentConnectionState() == ConnectionState
                        .CONNECTING) {
                    actionBarView.setDeviceStatus(ActionBarView.DeviceStatus.CONNECTING);
                } else {
                    actionBarView.setDeviceStatus(ActionBarView.DeviceStatus.DISCONNECTED);
                }
            }
        });
    }

    private void toggleMuseDialog() {
        if (museDialog == null) {
            museDialog = WidgetUtils.buildListDialog(museListAdapter, new ListHolder(), new
                    OnItemClickListener() {
                        @Override
                        public void onItemClick(DialogPlus dialog, Object item, View view, int
                                position) {
                            Muse muse = museListAdapter.getItem(position);
                            listenMuseConnectionStatus(muse, position);
                            muse.runAsynchronously();
                        }
                    }, headerView, this);
        }

        if (!museDialog.isShowing()) {
            museDialog.show();
        } else {
            museDialog.dismiss();
        }
    }

    private MuseMonitor getCurrentMuseMonitor() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_CURRENT_FRAGMENT);
        if (fragment != null && fragment.isVisible()) {
            return (MuseMonitor) fragment;
        } else {
            return null;
        }
    }

}
