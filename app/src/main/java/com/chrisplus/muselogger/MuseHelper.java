package com.chrisplus.muselogger;

import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseArtifactPacket;
import com.choosemuse.libmuse.MuseConnectionListener;
import com.choosemuse.libmuse.MuseConnectionPacket;
import com.choosemuse.libmuse.MuseDataListener;
import com.choosemuse.libmuse.MuseDataPacket;
import com.choosemuse.libmuse.MuseListener;
import com.choosemuse.libmuse.MuseManagerAndroid;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created by chrisplus on 22/7/16.
 */
public class MuseHelper {
    public static final String TAG = MuseHelper.class.getSimpleName();

    private static MuseHelper instance;

    private MuseManagerAndroid museManager;

    private MuseListener museListener = new MuseListener() {
        @Override
        public void museListChanged() {

        }
    };

    private MuseDataListener museDataListener = new MuseDataListener() {
        @Override
        public void receiveMuseDataPacket(MuseDataPacket museDataPacket, Muse muse) {

        }

        @Override
        public void receiveMuseArtifactPacket(MuseArtifactPacket museArtifactPacket, Muse muse) {

        }
    };

    private MuseConnectionListener museConnectionListener = new MuseConnectionListener() {
        @Override
        public void receiveMuseConnectionPacket(MuseConnectionPacket museConnectionPacket, Muse
                muse) {

        }
    };

    private MuseHelper(Context context) {
        museManager = MuseManagerAndroid.getInstance();
        museManager.setContext(context);
    }

    public synchronized static MuseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MuseHelper(context);
        }
        return instance;
    }

    public void startListening() {
        if (museManager != null) {
            museManager.startListening();
        }
    }

    public void stopListening() {
        if (museManager != null) {
            museManager.stopListening();
        }
    }

    public List<Muse> refreshMuse() {
        startListening();
        return museManager.getMuses();
    }

    public Observable<ArrayList<Muse>> refreshMuseObservable() {
        return Observable.defer(new Func0<Observable<ArrayList<Muse>>>() {
            @Override
            public Observable<ArrayList<Muse>> call() {
                startListening();
                return Observable.just(museManager.getMuses());
            }
        });
    }

}
