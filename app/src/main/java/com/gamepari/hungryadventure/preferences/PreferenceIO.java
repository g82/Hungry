package com.gamepari.hungryadventure.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gamepari on 2/16/15.
 */
public class PreferenceIO {

    public static final String KEY_COUNTRY = "saved_country";
    public static final String KEY_START_DATE = "started_date";
    public static final String KEY_USED_STEPS = "used_steps";
    public static final String KEY_AVAIL_STEPS = "avail_steps";
    private static final String DEFAULT_PREF = "hungry";

    public static final void savePreference(Context mContext, String key, String value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(DEFAULT_PREF, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static final String loadPreference(Context mContext, String key) {

        SharedPreferences pref = mContext.getSharedPreferences(DEFAULT_PREF, Context.MODE_PRIVATE);
        return pref.getString(key, null);
    }

    public static final int loadAvailCount(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(DEFAULT_PREF, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }

    public static final void saveAvailCount(Context context, String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(DEFAULT_PREF, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

}
