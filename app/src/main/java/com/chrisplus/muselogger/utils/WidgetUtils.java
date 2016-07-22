package com.chrisplus.muselogger.utils;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;

/**
 * Created by chrisplus on 22/7/16.
 */
public class WidgetUtils {
    public static final String TAG = WidgetUtils.class.getSimpleName();

    private WidgetUtils() {

    }

    public static final DialogPlus buildListDialog(BaseAdapter adapter, ListHolder listHolder,
                                                   OnItemClickListener onItemClickListener, View
                                                           headerView, Context activityContext) {

        final DialogPlus dialogPlus = DialogPlus.newDialog(activityContext).setAdapter(adapter)
                .setHeader(headerView).setContentHolder(listHolder).setOnItemClickListener
                        (onItemClickListener).setGravity(Gravity.TOP).create();

        return dialogPlus;
    }


}
