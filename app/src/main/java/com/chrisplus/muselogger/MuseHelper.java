package com.chrisplus.muselogger;

import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseManagerAndroid;

import android.content.Context;

import java.util.List;

/**
 * Created by chrisplus on 22/7/16.
 */
public class MuseHelper {
    public static final String TAG = MuseHelper.class.getSimpleName();

    private static MuseHelper instance;

    private MuseManagerAndroid museManager;

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
}
