
package com.chrisplus.muselogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

/**
 * Created by chrisplus on 3/10/15.
 */
public class LoggingThread extends Thread {

    public static final String FILE_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "MuseLogger" + File.separator;

    public static final int EEG_FLAG = 1;

    public static final int ACC_FLAG = 2;

    public static final int AR_FLAG = 3;

    public static final int UNKNOW_FLAG = 4;

    public static final String DATA_KEY = "key_data";

    public static final String TIME_KEY = "key_time";

    public static final String PX_EEG = "EEG_";

    public static final String PX_ACC = "ACC_";

    public static final String PX_AR = "AR_";

    public static final String EX = ".csv";

    private FileWriter eegWriter;

    private FileWriter accWriter;

    private FileWriter alphaRelativeWriter;

    private Handler loggingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            double data[] = msg.getData().getDoubleArray(DATA_KEY);
            long time = msg.getData().getLong(TIME_KEY);
            try {
                switch (msg.what) {
                    case EEG_FLAG:
                        eegWriter.write(time + "," + data[0] + "," + data[1] + "," + data[2] + ","
                                + data[3] + "\n");
                        eegWriter.flush();
                        break;
                    case ACC_FLAG:
                        accWriter
                                .write(time + "," + data[0] + "," + data[1] + "," + data[2] + "\n");
                        accWriter.flush();
                        break;
                    case AR_FLAG:
                        alphaRelativeWriter.write(time + "," + data[0] + "," + data[1] + ","
                                + data[2] + ","
                                + data[3] + "\n");
                        alphaRelativeWriter.flush();
                        break;
                    case UNKNOW_FLAG:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    public LoggingThread() {
        super();

        File dir = makeDir();
        String currentTime = getCurrentTimeStamp();
        File eeg = new File(dir, PX_EEG + currentTime + EX);
        File acc = new File(dir, PX_ACC + currentTime + EX);
        File ar = new File(dir, PX_AR + currentTime + EX);

        try {
            eegWriter = new FileWriter(eeg);
            accWriter = new FileWriter(acc);
            alphaRelativeWriter = new FileWriter(ar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Handler getLoggingHandler() {
        return loggingHandler;
    }

    @Override
    public void interrupt() {
        try {
            if (eegWriter != null) {
                eegWriter.close();
            }

            if (accWriter != null) {
                accWriter.close();
            }

            if (alphaRelativeWriter != null) {
                alphaRelativeWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.interrupt();
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private File makeDir() {
        File dir = new File(FILE_PATH);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    private String getCurrentTimeStamp() {
        return System.currentTimeMillis() + "";
    }
}
