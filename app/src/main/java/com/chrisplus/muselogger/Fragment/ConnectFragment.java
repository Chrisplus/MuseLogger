package com.chrisplus.muselogger.Fragment;

import com.chrisplus.muselogger.R;
import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseManager;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ConnectFragment extends Fragment {

    private Spinner museSpinner;

    private Button connectBtn;

    private Button refreshBtn;

    private TextView statusView;

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect, container, false);
        museSpinner = (Spinner) view.findViewById(R.id.muses_spinner);
        connectBtn = (Button) view.findViewById(R.id.btn_connect);
        refreshBtn = (Button) view.findViewById(R.id.btn_refresh);
        statusView = (TextView) view.findViewById(R.id.status_indicator);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshSpinner();
    }

    private void refreshSpinner() {
        MuseManager.refreshPairedMuses();
        List<Muse> pariedMuses = MuseManager.getPairedMuses();
        List<String> spinnerItems = new ArrayList<String>();
        for (Muse m : pariedMuses) {
            String dev_id = m.getName() + "-" + m.getMacAddress();
            spinnerItems.add(dev_id);
        }
        ArrayAdapter<String> adapterArray = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, spinnerItems);
        museSpinner.setAdapter(adapterArray);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getBaseContext();
    }
}
