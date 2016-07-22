package com.chrisplus.muselogger;

import com.chrisplus.muselogger.fragments.DashboardFragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by chrisplus on 19/7/16.
 */
public class MainActivity_N extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard);
        setupActionBar();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                DashboardFragment.newInstance());
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setCustomView(R.layout.actionbar);

            getSupportActionBar().setDisplayShowCustomEnabled(true);
            TextView actionBarTitleView = (TextView) findViewById(R.id.actionbar_title);
            getSupportActionBar().setElevation(0);
            actionBarTitleView.setText(R.string.app_name);
        }
    }

}
