package com.maciejkrolik.meteostats.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceUtils {

    public static String getPreferenceKey(int i) {
        String preferenceKey;
        switch (i) {
            case 0:
                preferenceKey = "show_rain";
                break;
            case 1:
                preferenceKey = "show_water";
                break;
            case 2:
                preferenceKey = "show_winddir";
                break;
            case 3:
                preferenceKey = "show_windlevel";
                break;
            default:
                preferenceKey = "show_rain";
                break;
        }
        return preferenceKey;
    }

    public static boolean[] getCheckedItemsInfo(Context context) {
        final SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return new boolean[]{
                sharedPreferences.getBoolean("show_rain", true),
                sharedPreferences.getBoolean("show_water", false),
                sharedPreferences.getBoolean("show_winddir", false),
                sharedPreferences.getBoolean("show_windlevel", false)
        };
    }
}
