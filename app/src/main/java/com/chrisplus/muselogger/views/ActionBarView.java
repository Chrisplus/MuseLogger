package com.chrisplus.muselogger.views;

import com.chrisplus.muselogger.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chrisplus on 22/7/16.
 */
public class ActionBarView extends RelativeLayout {
    public static final String TAG = ActionBarView.class.getSimpleName();

    @BindView(R.id.actionbar_title)
    public TextView titleView;

    @BindView(R.id.actionbar_status)
    public DeviceStatusView statusView;

    public enum DeviceStatus {
        CONNECTED,
        DISCONNECTED,
        CONNECTING
    }

    public ActionBarView(Context context) {
        super(context);
        initViews();
    }

    public ActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public ActionBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public void setTitle(int titleRes) {
        titleView.setText(titleRes);
    }

    public void setStatusOnClickListener(OnClickListener listener) {
        if (statusView != null) {
            statusView.setOnClickListener(listener);
        }
    }

    public void setDeviceStatus(DeviceStatus status) {
        switch (status) {
            case CONNECTING:
                statusView.setConnecting();
                break;
            case CONNECTED:
                statusView.setConnected();
                break;
            case DISCONNECTED:
            default:
                statusView.setConnectError();
                break;
        }
    }


    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_actionbar, this, true);
        ButterKnife.bind(this);
        titleView.setText(R.string.app_name);
    }
}
