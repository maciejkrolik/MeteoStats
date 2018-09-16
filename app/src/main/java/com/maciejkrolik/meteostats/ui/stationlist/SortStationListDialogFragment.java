package com.maciejkrolik.meteostats.ui.stationlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.util.SharedPreferenceUtils;

public class SortStationListDialogFragment extends DialogFragment {

    private SharedPreferences.Editor editor;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();

        boolean[] checkedItems = SharedPreferenceUtils.getCheckedItemsInfo(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_sort_title)
                .setMultiChoiceItems(R.array.dialog_sort_options, checkedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                                String preferenceKeyOfClickedItem = SharedPreferenceUtils.getPreferenceKey(which);
                                if (isChecked) {
                                    editor.putBoolean(preferenceKeyOfClickedItem, true);
                                    editor.apply();
                                } else if (sharedPreferences.getBoolean(preferenceKeyOfClickedItem, true)) {
                                    editor.putBoolean(SharedPreferenceUtils.getPreferenceKey(which), false);
                                    editor.apply();
                                }
                            }
                        });

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
