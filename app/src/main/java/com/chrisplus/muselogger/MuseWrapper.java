package com.chrisplus.muselogger;

import com.interaxon.libmuse.ConnectionState;
import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseArtifactPacket;
import com.interaxon.libmuse.MuseConnectionListener;
import com.interaxon.libmuse.MuseConnectionPacket;
import com.interaxon.libmuse.MuseDataListener;
import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;
import com.interaxon.libmuse.MuseManager;
import com.interaxon.libmuse.MusePreset;

import android.content.Context;

import java.util.List;

import de.greenrobot.event.EventBus;

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
        MuseManager.refreshPairedMuses();
        return MuseManager.getPairedMuses();

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

        muse.registerDataListener(dataListener,
                MuseDataPacketType.ACCELEROMETER);

        muse.registerDataListener(dataListener,
                MuseDataPacketType.ALPHA_RELATIVE);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.ARTIFACTS);

        muse.registerDataListener(dataListener, MuseDataPacketType.HORSESHOE);

        muse.setPreset(MusePreset.PRESET_14);
        muse.enableDataTransmission(true);
    }

    private class DataListener extends MuseDataListener {

        public DataListener() {
            super();
        }

        @Override
        public void receiveMuseDataPacket(MuseDataPacket museDataPacket) {
            if (museDataPacket != null
                    && museDataPacket.getPacketType() == MuseDataPacketType.HORSESHOE) {
//                EventBus.getDefault().post(museDataPacket.getValues());
            } else {
                EventBus.getDefault().post(museDataPacket);
            }

        }

        @Override
        public void receiveMuseArtifactPacket(MuseArtifactPacket museArtifactPacket) {

        }
    }

    private class ConnectionListener extends MuseConnectionListener {

        public ConnectionListener() {
            super();
        }

        @Override
        public void receiveMuseConnectionPacket(MuseConnectionPacket museConnectionPacket) {
            EventBus.getDefault().post(museConnectionPacket);
        }
    }
}