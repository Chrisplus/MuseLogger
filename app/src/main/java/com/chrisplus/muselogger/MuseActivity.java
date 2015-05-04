package com.chrisplus.muselogger;

import com.chrisplus.muselogger.Fragment.ConnectFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class MuseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muse);
        getFragmentManager().beginTransaction().replace(R.id.container, new ConnectFragment())
                .commit();
    }

}
