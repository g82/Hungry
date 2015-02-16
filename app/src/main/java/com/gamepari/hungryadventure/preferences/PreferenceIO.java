package com.gamepari.hungryadventure.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gamepari on 2/16/15.
 */
public class PreferenceIO {

    public static final String KEY_COUNTRY = "saved_country";
    public static final String KEY_START_DATE = "started_date";
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

}
