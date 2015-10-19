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

    public static final String PX_AA = "Absolute_Alpha";

    public static final String PX_AB = "Absolute_Beta";

    public static final String PX_RAW = "Raw_EEG";

    public static final String EX = ".csv";

    private HashMap<MuseDataPacketType, FileWriter> writers;

    private File absoluteAlpha;
    private File absoluteBeta;
    private File rawEEG;


    public LoggingProcessor() {

    }

    public void startLogging() {
        if (initWriters()) {
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

    public boolean renameRecords(String note) {
        boolean rawEEGRename = false;
        boolean absoluteAlphaRename = false;
        boolean absoluteBetaRename = false;
        if (rawEEG != null) {
            rawEEGRename = rawEEG.renameTo(new File(rawEEG.getAbsolutePath() + "_" + note + EX));
        }

        if (absoluteAlpha != null) {
            absoluteAlphaRename = absoluteAlpha.renameTo(new File(absoluteAlpha.getAbsolutePath() + "_" + note + EX));
        }

        if (absoluteBeta != null) {
            absoluteBetaRename = absoluteBeta.renameTo(new File(absoluteBeta.getAbsolutePath() + "_" + note + EX));
        }


        return rawEEGRename && absoluteAlphaRename && absoluteBetaRename;
    }

    private boolean initWriters() {
        File dir = makeDir();
        long currentTime = System.currentTimeMillis();

        absoluteAlpha = new File(dir, currentTime + "_" + PX_AA);
        absoluteBeta = new File(dir, currentTime + "_" + PX_AB);
        rawEEG = new File(dir, currentTime + "_" + PX_RAW);

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
