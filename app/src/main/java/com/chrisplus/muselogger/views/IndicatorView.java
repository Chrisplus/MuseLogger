package com.chrisplus.muselogger.views;

import com.chrisplus.muselogger.R;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chrisplus on 21/7/16.
 */
public class IndicatorView extends FrameLayout {
    @BindView(R.id.indicator_name)
    public TextView indicatorName;
    @BindView(R.id.indicator_view)
    public View indicatorView;

    private ElectrodeType electrodeType = ElectrodeType.FPZ;

    public enum ElectrodeType {

        TP9(R.color.md_indigo_700, R.string.eeg_channel_tp9),
        TP10(R.color.md_teal_700, R.string.eeg_channel_tp10),
        AF7(R.color.md_green_700, R.string.eeg_channel_af7),
        AF8(R.color.md_purple_700, R.string.eeg_channel_af8),
        FPZ(R.color.md_blue_700, R.string.eeg_channel_fpz);

        private int colorRes;
        private int nameRes;

        ElectrodeType(int colorRes, int nameRes) {
            this.colorRes = colorRes;
            this.nameRes = nameRes;
        }

        public int getColorRes() {
            return colorRes;
        }

        public int getNameRes() {
            return nameRes;
        }
    }

    public IndicatorView(Context context) {
        super(context);
        initView();
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    public void setType(ElectrodeType type) {
        electrodeType = type;
        indicatorName.setText(electrodeType.getNameRes());
        setColor(electrodeType.getColorRes(), false);
    }

    public void setEmpty() {
        setColor(electrodeType.getColorRes(), false);
    }

    public void setFull() {
        setColor(electrodeType.getColorRes(), true);
    }


    private void setColor(int colorRes, boolean isFull) {
        GradientDrawable shapeDrawable = (GradientDrawable) indicatorView.getBackground();
        shapeDrawable.setStroke(2, getResources().getColor(colorRes));

        if (!isFull) {
            shapeDrawable.setColor(getResources().getColor(R.color.md_grey_300));
        } else {
            shapeDrawable.setColor(getResources().getColor(colorRes));
        }
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_indicator, this, true);
        ButterKnife.bind(this);
    }


}
