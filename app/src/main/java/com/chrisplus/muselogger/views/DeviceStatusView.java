package com.chrisplus.muselogger.views;

import com.chrisplus.muselogger.R;
import com.pnikosis.materialishprogress.ProgressWheel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chrisplus on 21/7/16.
 */
public class DeviceStatusView extends LinearLayout {

    @BindView(R.id.device_status_progress)
    public ProgressWheel deviceStatusProgress;

    @BindView(R.id.device_status_icon)
    public ImageView deviceStatusIcon;

    public DeviceStatusView(Context context) {
        super(context);
        initView();
    }

    public DeviceStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DeviceStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_device_status, this, true);
        ButterKnife.bind(this);
    }
}
