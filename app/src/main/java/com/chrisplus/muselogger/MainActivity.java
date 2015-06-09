package com.chrisplus.muselogger;

import com.interaxon.libmuse.ConnectionState;
import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseConnectionPacket;
import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends ActionBarActivity {

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

    private View tp9Status;

    private View fp1Status;

    private View fp2Status;

    private View tp10Status;

    private Spinner spinner;

    private List<Muse> currentMuses;

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

        tp9Status = findViewById(R.id.tp9indicator);
        fp1Status = findViewById(R.id.fp1indicator);
        fp2Status = findViewById(R.id.fp2indicator);
        tp10Status = findViewById(R.id.tp10indicator);

        spinner = (Spinner) findViewById(R.id.spinner);

        tp9ChartView.setChart("TP9", R.color.md_indigo_500);
        fp1ChartView.setChart("FP1", getResources().getColor(R.color.md_deep_purple_500));
        fp2ChartView.setChart("FP2", getResources().getColor(R.color.md_teal_500));
        tp10ChartView.setChart("TP10", getResources().getColor(R.color.md_deep_orange_500));

        connectAction.setOnClickListener(connectClickListener);
        recordAction.setOnClickListener(recordClickListener);
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
        if (packet != null && packet.getPacketType() == MuseDataPacketType.ALPHA_RELATIVE) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("Hello", packet.getValues().get(0).floatValue() + "");
                    tp9ChartView.addEntry(packet.getValues().get(0).floatValue());
                    fp1ChartView.addEntry(packet.getValues().get(1).floatValue());
                    fp2ChartView.addEntry(packet.getValues().get(2).floatValue());
                    tp10ChartView.addEntry(packet.getValues().get(3).floatValue());
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

}
