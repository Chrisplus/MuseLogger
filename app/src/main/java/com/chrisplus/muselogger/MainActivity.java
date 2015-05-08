package com.chrisplus.muselogger;

import com.interaxon.libmuse.ConnectionState;
import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseConnectionPacket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = "MuseLogger";

    public static final String TAG_CONNECT = "connect";

    public static final String TAG_DISCONNECT = "disconnect";

    public static final String TAG_RECORD = "record";

    public static final String TAG_STOP = "stop";

    private enum MuseState {
        UNKNOWN, CONNECTED, DISCONNECTED
    }

    private Spinner museSpinner;

    private TextView statusView;

    private Button connectBtn;

    private Button refreshBtn;

    private Button settingBtn;

    private Button recordBtn;

    private List<Muse> currentMuses;

    private TextView fp1status;

    private TextView fp2status;

    private TextView tp9status;

    private TextView tp10status;

    private final Logger logger = new Logger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        museSpinner = (Spinner) findViewById(R.id.spinner);
        statusView = (TextView) findViewById(R.id.status);

        connectBtn = (Button) findViewById(R.id.btn_connect);
        refreshBtn = (Button) findViewById(R.id.btn_refresh);
        settingBtn = (Button) findViewById(R.id.btn_setting);
        recordBtn = (Button) findViewById(R.id.btn_record);

        fp1status = (TextView) findViewById(R.id.fp1);
        fp2status = (TextView) findViewById(R.id.fp2);

        tp9status = (TextView) findViewById(R.id.tp9);
        tp10status = (TextView) findViewById(R.id.tp10);

        connectBtn.setOnClickListener(connectListener);
        refreshBtn.setOnClickListener(refreshListener);
        settingBtn.setOnClickListener(settingListener);
        recordBtn.setOnClickListener(recordListener);
        recordBtn.setOnLongClickListener(recordLongListener);
    }

    private void updateStatus(MuseState state) {
        if (state == MuseState.UNKNOWN || state == MuseState.DISCONNECTED) {
            connectBtn.setEnabled(true);
            connectBtn.setText(R.string.btn_connect);
            connectBtn.setTag(TAG_CONNECT);

            refreshBtn.setEnabled(true);

            settingBtn.setEnabled(true);
            recordBtn.setEnabled(false);
            recordBtn.setText(R.string.btn_record);
            recordBtn.setTag(TAG_RECORD);

            if (state == MuseState.UNKNOWN) {
                statusView.setText(R.string.status_unknown);
            } else {
                statusView.setText(R.string.status_disconnected);
            }

        } else if (state == MuseState.CONNECTED) {
            connectBtn.setEnabled(true);
            connectBtn.setText(R.string.btn_disconnect);
            connectBtn.setTag(TAG_DISCONNECT);

            refreshBtn.setEnabled(false);
            settingBtn.setEnabled(false);

            recordBtn.setEnabled(true);

            statusView.setText(R.string.status_connected);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        refreshSpinner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void refreshSpinner() {
        currentMuses = MuseWrapper.getInstance(getApplicationContext()).getPairedMused();
        List<String> spinnerItems = new ArrayList<String>();

        for (Muse m : currentMuses) {
            String dev_id = m.getName() + "-" + m.getMacAddress();
            spinnerItems.add(dev_id);
        }

        ArrayAdapter<String> adapterArray = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerItems);
        museSpinner.setAdapter(adapterArray);

        ConnectionState state = currentMuses.get(museSpinner.getSelectedItemPosition())
                .getConnectionState();
        if (state == ConnectionState.CONNECTED) {
            updateStatus(MuseState.CONNECTED);
        } else if (state == ConnectionState.UNKNOWN) {
            updateStatus(MuseState.UNKNOWN);
        } else {
            updateStatus(MuseState.DISCONNECTED);
        }
    }

    public void onEvent(MuseConnectionPacket packet) {
        if (packet != null) {
            final MuseState state;
            if (packet.getCurrentConnectionState() == ConnectionState.CONNECTED) {
                state = MuseState.CONNECTED;
            } else if (packet.getCurrentConnectionState() == ConnectionState.DISCONNECTED) {
                state = MuseState.DISCONNECTED;
            } else {
                state = MuseState.UNKNOWN;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateStatus(state);
                }
            });
        }
    }

    public void onEvent(final ArrayList<Double> horseshoe) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tp9status.setText(horseshoe.get(0).toString());
                fp1status.setText(horseshoe.get(1).toString());
                fp2status.setText(horseshoe.get(2).toString());
                tp10status.setText(horseshoe.get(3).toString());
            }
        });
    }

    private void showNoteDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.note_dialog_title);

        final EditText input = new EditText(this);
        dialog.setView(input);

        dialog.setPositiveButton(R.string.note_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recordBtn.setText(R.string.btn_stop);
                recordBtn.setTag(TAG_STOP);
                logger.startLogging(input.getText().toString().trim());
            }
        });

        dialog.setNegativeButton(R.string.note_dialog_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog.show();
    }

    private final View.OnClickListener connectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            connectBtn.setEnabled(false);
            refreshBtn.setEnabled(false);

            if (connectBtn.getTag().equals(TAG_CONNECT)) {
                connectBtn.setText(R.string.status_connecting);
                MuseWrapper.getInstance(getApplicationContext())
                        .connect(currentMuses.get(museSpinner.getSelectedItemPosition()));
            } else {
                MuseWrapper.getInstance(getApplicationContext())
                        .disconnect(currentMuses.get(museSpinner.getSelectedItemPosition()));
            }
        }
    };


    private final View.OnClickListener refreshListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshSpinner();
        }
    };

    private final View.OnClickListener settingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private final View.OnClickListener recordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TAG_RECORD.equals(recordBtn.getTag())) {
                recordBtn.setText(R.string.btn_stop);
                recordBtn.setTag(TAG_STOP);
                logger.startLogging();
            } else {
                recordBtn.setText(R.string.btn_record);
                recordBtn.setTag(TAG_RECORD);
                logger.stopLogging();
            }
        }
    };

    private final View.OnLongClickListener recordLongListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (TAG_RECORD.equals(recordBtn.getTag())) {
                showNoteDialog();
            }
            return false;
        }
    };


}
