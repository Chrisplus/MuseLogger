package com.chrisplus.muselogger.views;

import com.chrisplus.muselogger.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chrisplus on 22/7/16.
 */
public class MuseListHeaderView extends RelativeLayout {
    @BindView(R.id.muse_header_name)
    public TextView nameView;

    @BindView(R.id.muse_header_progress)
    public ImageView refreshView;

    public MuseListHeaderView(Context context) {
        super(context);
        initView();
    }

    public MuseListHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MuseListHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_muse_list_header, this, true);
        ButterKnife.bind(this);
    }
}
