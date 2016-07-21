package com.chrisplus.muselogger;


import com.choosemuse.libmuse.ConnectionState;
import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseArtifactPacket;
import com.choosemuse.libmuse.MuseConnectionListener;
import com.choosemuse.libmuse.MuseConnectionPacket;
import com.choosemuse.libmuse.MuseDataListener;
import com.choosemuse.libmuse.MuseDataPacket;
import com.choosemuse.libmuse.MuseDataPacketType;
import com.choosemuse.libmuse.MuseManagerAndroid;
import com.choosemuse.libmuse.MusePreset;

import android.content.Context;

import java.util.List;

/**
 * Created by chrisplus on 5/7/15.
 */
public class MuseWrapper {

    public static final String TAG = MuseWrapper.class.getSimpleName();

    private static MuseWrapper instance;

    private Context context;

    private final DataListener dataListener = new DataListener();

    private final ConnectionListener connectionListener = new ConnectionListener();

    public static synchronized MuseWrapper getInstance(Context context) {
        if (instance == null) {
            instance = new MuseWrapper(context);
        }

        return instance;
    }

    private MuseWrapper(Context ctx) {
        context = ctx;
    }

    public List<Muse> getPairedMused() {
        return MuseManagerAndroid.getInstance().getMuses();
    }

    public void connect(Muse muse) {
        config(muse);
        if (muse != null && muse.getConnectionState() != ConnectionState.CONNECTED) {
            try {
                muse.runAsynchronously();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect(Muse muse) {
        if (muse != null) {
            muse.disconnect(true);
        }
    }

    private void config(Muse muse) {
        muse.registerConnectionListener(connectionListener);

//        muse.registerDataListener(dataListener,
//                MuseDataPacketType.ACCELEROMETER);

        muse.registerDataListener(dataListener,
                MuseDataPacketType.EEG);
//        muse.registerDataListener(dataListener,
//                MuseDataPacketType.ARTIFACTS);

        muse.registerDataListener(dataListener, MuseDataPacketType.ALPHA_ABSOLUTE);
        muse.registerDataListener(dataListener, MuseDataPacketType.BETA_ABSOLUTE);

        muse.setPreset(MusePreset.PRESET_14);
        muse.enableDataTransmission(true);
    }

    private class DataListener extends MuseDataListener {

        public DataListener() {
            super();
        }

        @Override
        public void receiveMuseDataPacket(MuseDataPacket museDataPacket, Muse muse) {

        }

        @Override
        public void receiveMuseArtifactPacket(MuseArtifactPacket museArtifactPacket, Muse muse) {

        }
    }

    private class ConnectionListener extends MuseConnectionListener {

        public ConnectionListener() {
            super();
        }

        @Override
        public void receiveMuseConnectionPacket(MuseConnectionPacket museConnectionPacket, Muse
                muse) {

        }
    }
}