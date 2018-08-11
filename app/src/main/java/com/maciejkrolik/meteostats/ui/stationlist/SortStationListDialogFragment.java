package com.maciejkrolik.meteostats.ui.stationlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;

import com.maciejkrolik.meteostats.R;

public class SortStationListDialogFragment extends DialogFragment {

    private SharedPreferences.Editor editor;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();

        boolean[] checkedItems = {
                sharedPreferences.getBoolean("show_rain", true),
                sharedPreferences.getBoolean("show_water", true),
                sharedPreferences.getBoolean("show_wind", true)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_sort_title)
                .setMultiChoiceItems(R.array.dialog_sort_options, checkedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                if (b) {
                                    editor.putBoolean(getPreferenceKey(i), true);
                                    editor.apply();
                                } else if (sharedPreferences.getBoolean(getPreferenceKey(i), true)) {
                                    editor.putBoolean(getPreferenceKey(i), false);
                                    editor.apply();
                                }
                            }
                        });

        return builder.create();
    }

    private String getPreferenceKey(int i) {
        String preferenceKey;
        switch (i) {
            case 0:
                preferenceKey = "show_rain";
                break;
            case 1:
                preferenceKey = "show_water";
                break;
            case 2:
                preferenceKey = "show_wind";
                break;
            default:
                preferenceKey = "show_rain";
                break;
        }
        return preferenceKey;
    }
}
