package com.chrisplus.muselogger.views;

import com.chrisplus.muselogger.R;

import android.content.Context;
import android.content.res.TypedArray;
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

        AF7(R.color.md_green_700, R.color.md_green_300, R.string.eeg_channel_af7),
        AF8(R.color.md_purple_700, R.color.md_purple_300, R.string.eeg_channel_af8),
        TP9(R.color.md_indigo_700, R.color.md_indigo_300, R.string.eeg_channel_tp9),
        TP10(R.color.md_teal_700, R.color.md_teal_300, R.string.eeg_channel_tp10),
        FPZ(R.color.md_blue_700, R.color.md_blue_300, R.string.eeg_channel_fpz);

        private int colorFullRes;
        private int colorHalfRes;
        private int nameRes;

        ElectrodeType(int colorFullRes, int colorHalfRes, int nameRes) {
            this.colorFullRes = colorFullRes;
            this.colorHalfRes = colorHalfRes;
            this.nameRes = nameRes;
        }

        public int getColorFullRes() {
            return colorFullRes;
        }

        public int getNameRes() {
            return nameRes;
        }

        public int getColorHalfRes() {
            return colorHalfRes;
        }
    }

    public IndicatorView(Context context) {
        super(context);
        initView(null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }


    private void setType(ElectrodeType type) {
        electrodeType = type;
        indicatorName.setText(electrodeType.getNameRes());
        setEmpty();
    }

    public void setEmpty() {
        setColor(R.color.md_grey_300);
    }

    public void setFull() {
        setColor(electrodeType.getColorFullRes());
    }

    public void setHalf() {
        setColor(electrodeType.getColorHalfRes());
    }


    private void setColor(int colorRes) {
        GradientDrawable shapeDrawable = (GradientDrawable) indicatorView.getBackground();
        shapeDrawable.setStroke(2, getResources().getColor(colorRes));
        shapeDrawable.setColor(getResources().getColor(colorRes));

    }

    private void initView(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_indicator, this, true);
        ButterKnife.bind(this);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable
                    .IndicatorView, 0, 0);
            try {
                int typeIndex = typedArray.getInt(R.styleable.IndicatorView_type, ElectrodeType
                        .FPZ.ordinal());
                setType(ElectrodeType.values()[typeIndex]);
            } finally {
                typedArray.recycle();
            }
        }
    }


}
