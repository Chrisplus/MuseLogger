
package com.chrisplus.muselogger;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    public final static String TAG = MainActivity.class.getSimpleName();
    public final static String TAG_CONNECT = "connect";
    public final static String TAG_DISCONNECT = "disconnect";

    private TextView statusText;
    private Button actionButton;
    private TextView logText;
    private MuseConnector connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        statusText = (TextView) findViewById(R.id.statustext);
        actionButton = (Button) findViewById(R.id.connectbutton);
        logText = (TextView) findViewById(R.id.logview);

        connector = new MuseConnector();

        actionButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String tag = (String) v.getTag();

                if (tag != null && tag.equalsIgnoreCase(TAG_CONNECT)) {
                    /* connect */
                    log("connect to muse");
                    new ConnectTask().execute(connector);

                } else if (tag != null && tag.equalsIgnoreCase(TAG_DISCONNECT)) {
                    /* disconnect */
                    log("diconnect from muse");
                    connector.close();

                } else {

                }
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void log(final String msg) {
        Log.d(TAG, msg);
        if (logText != null) {
            logText.append(msg + "\n");
            final int scrollAmount = logText.getLayout().getLineTop(
                    logText.getLineCount())
                    - logText.getHeight();
            // if there is no need to scroll, scrollAmount will be <=0
            if (scrollAmount > 0) {
                logText.scrollTo(0, scrollAmount);
            } else {
                logText.scrollTo(0, 0);
            }
        }
    }

    /**
     * Update status
     * 
     * @param status 0 for unpaired, 1 for connected, 2 for disconnected
     */
    private void updateStatus(int status) {
        if (statusText == null || actionButton == null) {
            return;
        }

        switch (status) {
            case 0:
                statusText.setText(R.string.status_unpaired);
                actionButton.setEnabled(false);
                break;
            case 1:
                statusText.setText(R.string.status_connected);
                actionButton.setText(R.string.btn_disconnect);
                actionButton.setTag(TAG_DISCONNECT);
                actionButton.setEnabled(true);

                /* start logging task */
                break;
            case 2:
                statusText.setText(R.string.status_disconnected);
                actionButton.setText(R.string.btn_connect);
                actionButton.setTag(TAG_CONNECT);
                actionButton.setEnabled(true);
                break;
            default:
                log("status update error");
                break;
        }
    }

    private void refreshStatus() {
        boolean isPaired = BluetoothUtils.checkMusePaired();

        if (!isPaired) {
            updateStatus(0);
            return;
        }

        updateStatus(2);
    }

    private class ConnectTask extends AsyncTask<MuseConnector, String, Boolean> {

        @Override
        protected Boolean doInBackground(MuseConnector... params) {
            connector = params[0];
            int count = 0;
            while (true) {
                if (connector != null) {
                    boolean res = connector.connect();
                    if (res) {
                        return true;
                    } else if (count > 9) {
                        return false;
                    } else {
                        ++count;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                log("connected to muse successfully");
                updateStatus(1);
            }
            super.onPostExecute(result);
        }

    }

    private class LoggingTask extends
            AsyncTask<BluetoothSocket, String, Boolean> {

        @Override
        protected void onPreExecute() {

            if (connector != null) {
                log("start logging service");
                connector.startStreaming();
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (connector != null) {
                log("stop logging service");
                connector.stopStreaming();
            }
        }

        @Override
        protected Boolean doInBackground(BluetoothSocket... params) {
            BluetoothSocket socket = params[0];

            try {
                InputStream reader = socket.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;

        }

    }
}
