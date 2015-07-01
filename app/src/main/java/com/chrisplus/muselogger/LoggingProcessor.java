package com.chrisplus.muselogger;

import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by chrisplus on 7/1/15.
 */
public class LoggingProcessor {

    public static final String FILE_FOLDER = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "MuseLogger" + File.separator;

    public static final String PX_AA = "Absolute_Alpha_";

    public static final String PX_AB = "Absolute_Beta_";

    public static final String PX_RAW = "Raw_EEG_";

    public static final String EX = ".csv";

    private HashMap<MuseDataPacketType, FileWriter> writers;


    public LoggingProcessor() {

    }

    public void startLogging(String note) {
        if (initWriters(note)) {
            EventBus.getDefault().register(this);
        }
    }

    public void stopLogging() {
        closeWriters();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(final MuseDataPacket data) {
        if (writers != null && !writers.isEmpty()) {
            FileWriter writer = writers.get(data.getPacketType());

            if (writer != null) {
                try {
                    writer.write(
                            getTimestamp() + "," + data.getValues().get(0) + "," + data.getValues()
                                    .get(1) + "," + data.getValues().get(2) + ","
                                    + data.getValues().get(3) + "\n");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public boolean isLogging() {
        return EventBus.getDefault().isRegistered(this);
    }

    private boolean initWriters(String note) {
        File dir = makeDir();
        long currentTime = System.currentTimeMillis();

        File absoluteAlpha = new File(dir, PX_AA + note + "_" + currentTime + EX);
        File absoluteBeta = new File(dir, PX_AB + note + "_" + currentTime + EX);
        File rawEEG = new File(dir, PX_RAW + note + "_" + currentTime + EX);

        try {
            writers = new HashMap<>();
            FileWriter absoluteAlphaWriter = new FileWriter(absoluteAlpha);
            FileWriter absoluteBetaWriter = new FileWriter(absoluteBeta);
            FileWriter rawEEGWriter = new FileWriter(rawEEG);
            writers.put(MuseDataPacketType.ALPHA_ABSOLUTE, absoluteAlphaWriter);
            writers.put(MuseDataPacketType.BETA_ABSOLUTE, absoluteBetaWriter);
            writers.put(MuseDataPacketType.EEG, rawEEGWriter);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean closeWriters() {
        if (writers != null && !writers.isEmpty()) {
            for (Map.Entry<MuseDataPacketType, FileWriter> next : writers.entrySet()) {
                if (next.getValue() != null) {
                    try {
                        next.getValue().flush();
                        next.getValue().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }

            writers.clear();
        }

        return true;
    }

    private File makeDir() {
        File dir = new File(FILE_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }


    private String getTimestamp() {
        return System.currentTimeMillis() + "";
    }


}
