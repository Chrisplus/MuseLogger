package com.chrisplus.muselogger;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by chrisplus on 7/1/15.
 */
public class SettingUtil {

    public final static String getRecordPath(Context context) {
        return getPreferenceString(context, context.getString(R.string.pref_record_path),
                context.getString(R.string.pref_default_record_path));
    }


    private static String getPreferenceString(Context context, String key, String defaultString) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultString);
    }

    private static Boolean getPreferenceBoolean(Context context, String key, Boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
    }
}
