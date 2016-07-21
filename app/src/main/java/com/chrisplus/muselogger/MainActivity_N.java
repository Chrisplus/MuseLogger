package com.chrisplus.muselogger;

import com.chrisplus.muselogger.views.IndicatorView;
import com.chrisplus.muselogger.views.SquareArcView;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chrisplus on 19/7/16.
 */
public class MainActivity_N extends AppCompatActivity {

    @BindView(R.id.main_battery_indicator)
    public SquareArcView batteryView;

    @BindView(R.id.main_tp9_indicator)
    public IndicatorView tp9Indicator;

    @BindView(R.id.main_af7_indicator)
    public IndicatorView af7Indicator;

    @BindView(R.id.main_fpz_indicator)
    public IndicatorView fpzIndicator;

    @BindView(R.id.main_af8_indicator)
    public IndicatorView af8Indicator;

    @BindView(R.id.main_tp10_indicator)
    public IndicatorView tp10Indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        setupActionBar();
        ButterKnife.bind(this);
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
