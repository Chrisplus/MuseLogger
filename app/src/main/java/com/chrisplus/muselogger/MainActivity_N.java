package com.chrisplus.muselogger;

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
        setContentView(R.layout.activity_main_new);
        setupActionBar();
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar);
            TextView actionBarTitleView = (TextView) findViewById(R.id.actionbar_title);
            getSupportActionBar().setElevation(0);
            actionBarTitleView.setText(R.string.app_name);
        }
    }
}
