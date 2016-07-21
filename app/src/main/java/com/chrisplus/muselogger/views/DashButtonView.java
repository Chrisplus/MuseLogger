package com.chrisplus.muselogger.views;

import com.chrisplus.muselogger.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chrisplus on 21/7/16.
 */
public class DashButtonView extends LinearLayout {

    @BindView(R.id.dashbtn_icon)
    public ImageView dashIcon;

    @BindView(R.id.dashbtn_name)
    public TextView dashName;

    public DashButtonView(Context context) {
        super(context);
        initView(null);
    }

    public DashButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public DashButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_dashbutton, this, true);
        ButterKnife.bind(this);

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable
                    .DashButtonView, 0, 0);
            try {
                dashIcon.setImageResource(typedArray.getResourceId(R.styleable
                        .DashButtonView_dashIcon, R.drawable.ic_emotion));
                dashName.setText(typedArray.getResourceId(R.styleable.DashButtonView_dashTitle, R
                        .string.dash_btn_emotion));
            } finally {
                typedArray.recycle();
            }
        }
    }
}
