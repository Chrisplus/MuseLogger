package com.chrisplus.muselogger;

import com.interaxon.libmuse.ConnectionState;
import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseConnectionPacket;
import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends FragmentActivity {

    public static final String TAG_CONNECT = "connect";

    public static final String TAG_DISCONNECT = "disconnect";

    public static final String TAG_RECORD = "record";

    public static final String TAG_STOP = "stop";

    private ChartView tp9ChartView;

    private ChartView fp1ChartView;

    private ChartView fp2ChartView;

    private ChartView tp10ChartView;

    private Button connectAction;

    private Button recordAction;

    private ImageView settingAction;

    private View tp9Status;

    private View fp1Status;

    private View fp2Status;

    private View tp10Status;

    private Spinner spinner;

    private List<Muse> currentMuses;

    private LoggingProcessor loggingProcessor;

    private final View.OnClickListener connectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setEnabled(false);
            if (TAG_CONNECT.equals(v.getTag())) {
                /*connect*/
                MuseWrapper.getInstance(getApplicationContext())
                        .connect(currentMuses.get(spinner.getSelectedItemPosition()));
            } else {
                /*disconnect*/
                MuseWrapper.getInstance(getApplicationContext())
                        .disconnect(currentMuses.get(spinner.getSelectedItemPosition()));
            }
        }
    };

    private final View.OnClickListener recordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setEnabled(false);

            if (TAG_RECORD.equals(v.getTag())) {
                getDefaultLoggingProcessor().startLogging("");

                if (getDefaultLoggingProcessor().isLogging()) {
                    v.setTag(TAG_STOP);
                    ((Button) v).setText(getString(R.string.btn_stop));
                }

                v.setEnabled(true);
            } else {
                getDefaultLoggingProcessor().stopLogging();

                if (!getDefaultLoggingProcessor().isLogging()) {
                    v.setTag(TAG_RECORD);
                    ((Button) v).setText(getString(R.string.btn_record));
                }

                v.setEnabled(true);
            }
        }
    };

    private final View.OnClickListener settingClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v != null && TAG_CONNECT.equals(connectAction.getTag())) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tp9ChartView = (ChartView) findViewById(R.id.tp9view);
        fp1ChartView = (ChartView) findViewById(R.id.fp1view);
        fp2ChartView = (ChartView) findViewById(R.id.fp2view);
        tp10ChartView = (ChartView) findViewById(R.id.tp10view);

        connectAction = (Button) findViewById(R.id.connectbtn);
        recordAction = (Button) findViewById(R.id.recordbtn);

        settingAction = (ImageView) findViewById(R.id.setting);

        tp9Status = findViewById(R.id.tp9indicator);
        fp1Status = findViewById(R.id.fp1indicator);
        fp2Status = findViewById(R.id.fp2indicator);
        tp10Status = findViewById(R.id.tp10indicator);

        spinner = (Spinner) findViewById(R.id.spinner);

        tp9ChartView.setChart("TP9", getResources().getColor(R.color.md_red_500));
        fp1ChartView.setChart("FP1", getResources().getColor(R.color.md_deep_purple_500));
        fp2ChartView.setChart("FP2", getResources().getColor(R.color.md_indigo_500));
        tp10ChartView.setChart("TP10", getResources().getColor(R.color.md_deep_orange_500));

        connectAction.setOnClickListener(connectClickListener);
        recordAction.setOnClickListener(recordClickListener);
        settingAction.setOnClickListener(settingClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDevices();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(final MuseConnectionPacket packet) {
        if (packet != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateStatus(packet.getCurrentConnectionState());
                }
            });
        }
    }

    public void onEvent(final MuseDataPacket packet) {
        if (packet != null && packet.getPacketType() == MuseDataPacketType.EEG) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tp9ChartView.addEntry(packet.getValues().get(0).floatValue());
                    fp1ChartView.addEntry(packet.getValues().get(1).floatValue());
                    fp2ChartView.addEntry(packet.getValues().get(2).floatValue());
                    tp10ChartView.addEntry(packet.getValues().get(3).floatValue());
                }
            });
        }
    }

    public void onEvent(final ArrayList<Double> horseshoe) {
        if (horseshoe != null && horseshoe.size() == 4) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateHorseshoe(tp9Status, horseshoe.get(0).intValue());
                    updateHorseshoe(fp1Status, horseshoe.get(1).intValue());
                    updateHorseshoe(fp2Status, horseshoe.get(2).intValue());
                    updateHorseshoe(tp10Status, horseshoe.get(3).intValue());
                }
            });
        }
    }

    private void refreshDevices() {

        currentMuses = MuseWrapper.getInstance(getApplicationContext()).getPairedMused();
        List<String> spinnerItems = new ArrayList<String>();

        if (currentMuses == null || currentMuses.isEmpty()) {
            return;
        }
        for (Muse m : currentMuses) {
            String dev_id = m.getName() + "-" + m.getMacAddress();
            spinnerItems.add(dev_id);
        }

        ArrayAdapter<String> adapterArray = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerItems);
        spinner.setAdapter(adapterArray);

        ConnectionState state = currentMuses.get(spinner.getSelectedItemPosition())
                .getConnectionState();
        updateStatus(state);

    }

    private void updateStatus(ConnectionState state) {
        if (state == ConnectionState.CONNECTED) {
            /*connected*/
            connectAction.setText(R.string.btn_disconnect);
            connectAction.setTag(TAG_DISCONNECT);
            connectAction.setEnabled(true);

            recordAction.setText(R.string.btn_record);
            recordAction.setTag(TAG_RECORD);
            recordAction.setEnabled(true);

        } else if (state == ConnectionState.DISCONNECTED) {
            /*disconnected*/
            connectAction.setText(R.string.btn_connect);
            connectAction.setTag(TAG_CONNECT);
            connectAction.setEnabled(true);

            recordAction.setText(R.string.btn_record);
            recordAction.setTag(TAG_RECORD);
            recordAction.setEnabled(false);

        } else if (state == ConnectionState.CONNECTING) {
            /*connecting*/
            connectAction.setEnabled(false);
            recordAction.setEnabled(false);
        } else {
            connectAction.setText(R.string.btn_connect);
            connectAction.setTag(TAG_CONNECT);
        }
    }

    private void updateHorseshoe(View horseshoeView, int indicator) {
        int bgColor = getResources().getColor(R.color.md_indigo_200);
        switch (indicator) {
            case 1:
                bgColor = getResources().getColor(R.color.md_indigo_900);
                break;
            case 2:
                bgColor = getResources().getColor(R.color.md_indigo_700);
                break;
            case 3:
                bgColor = getResources().getColor(R.color.md_indigo_500);
                break;
            case 4:
                bgColor = getResources().getColor(R.color.md_indigo_200);
                break;
            default:
                break;
        }

        horseshoeView.setBackgroundColor(bgColor);
    }

    private LoggingProcessor getDefaultLoggingProcessor() {
        if (loggingProcessor == null) {
            loggingProcessor = new LoggingProcessor();
        }

        return loggingProcessor;
    }

}
