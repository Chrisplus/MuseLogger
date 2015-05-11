package com.chrisplus.muselogger;

import com.interaxon.libmuse.MuseDataPacket;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Created by chrisplus on 5/7/15.
 */
public class Logger {

    public static final String FILE_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "MuseLogger" + File.separator;

    public static final String PX_EEG = "EEG_";

    public static final String PX_ACC = "ACC_";

    public static final String EX = ".csv";

    private FileWriter eegWriter;

    private FileWriter accWriter;

    private File dir;

    public Logger() {
        dir = makeDir();
    }

    public synchronized void startLogging() {
        EventBus.getDefault().register(this);

        String currentTime = System.currentTimeMillis() + "";
        File eeg = new File(dir, PX_EEG + currentTime + EX);
        File acc = new File(dir, PX_ACC + currentTime + EX);

        try {
            eegWriter = new FileWriter(eeg);
            accWriter = new FileWriter(acc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void startLogging(String note) {
        EventBus.getDefault().register(this);

        String currentTime = System.currentTimeMillis() + "";
        File eeg = new File(dir, PX_EEG + currentTime + "_" + note + EX);
        File acc = new File(dir, PX_ACC + currentTime + "_" + note + EX);

        try {
            eegWriter = new FileWriter(eeg);
            accWriter = new FileWriter(acc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void stopLogging() {
        EventBus.getDefault().unregister(this);
        try {
            if (eegWriter != null) {
                eegWriter.close();
            }

            if (accWriter != null) {
                accWriter.close();
            }

            eegWriter = null;
            accWriter = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File makeDir() {
        File dir = new File(FILE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public void onEvent(MuseDataPacket packet) {
        switch (packet.getPacketType()) {
            case EEG:
                if (eegWriter != null) {
                    try {
                        eegWriter
                                .write(packet.getTimestamp() + "," + packet.getValues().get(0) + ","
                                        + packet.getValues().get(1) + "," + packet.getValues()
                                        .get(2) + "," + packet.getValues().get(3) + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case ACCELEROMETER:
                if (accWriter != null) {
                    try {
                        accWriter
                                .write(packet.getTimestamp() + "," + packet.getValues().get(0) + ","
                                        + packet.getValues().get(1) + "," + packet.getValues()
                                        .get(2) + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }
}
