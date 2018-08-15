package com.maciejkrolik.meteostats.util;

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
}
