package com.chrisplus.muselogger;

import com.choosemuse.libmuse.Muse;
import com.chrisplus.muselogger.adapters.MuseListAdapter;
import com.chrisplus.muselogger.fragments.DashboardFragment;
import com.chrisplus.muselogger.utils.WidgetUtils;
import com.chrisplus.muselogger.views.ActionBarView;
import com.chrisplus.muselogger.views.MuseListHeaderView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chrisplus on 19/7/16.
 */
public class MainActivity_N extends AppCompatActivity {


    private ActionBarView actionBarView;
    private MuseListHeaderView headerView;

    private DialogPlus museDialog;
    private MuseListAdapter museListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard);
        setupViews();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                DashboardFragment.newInstance());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MuseHelper.getInstance(this).stopListening();
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

    private void toggleMuseDialog() {
        if (museDialog == null) {
            museListAdapter = new MuseListAdapter(this);
            museDialog = WidgetUtils.buildListDialog(museListAdapter, new ListHolder(), new
                    OnItemClickListener() {

                        @Override
                        public void onItemClick(DialogPlus dialog, Object item, View view, int
                                position) {

                        }
                    }, headerView, this);
        }

        if (!museDialog.isShowing()) {
            museDialog.show();

            MuseHelper.getInstance(this)
                    .refreshMuseObservable().subscribeOn(Schedulers.newThread
                    ()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ArrayList<Muse>>() {

                        private ArrayList<Muse> tmpMuses = new ArrayList<Muse>();

                        @Override
                        public void onCompleted() {
                            museListAdapter.setMuses(tmpMuses);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(ArrayList<Muse> muses) {
                            tmpMuses.addAll(muses);
                        }
                    });
        } else {
            museDialog.dismiss();
        }
    }

}
