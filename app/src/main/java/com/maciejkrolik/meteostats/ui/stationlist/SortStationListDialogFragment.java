package com.maciejkrolik.meteostats.ui.stationlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.maciejkrolik.meteostats.R;

import java.util.ArrayList;
import java.util.List;

public class SortStationListDialogFragment extends DialogFragment {

    private List<Integer> selectedItems;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        selectedItems = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_sort_title)
                .setMultiChoiceItems(R.array.dialog_sort_options, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                if (b) {
                                    selectedItems.add(i);
                                } else if (selectedItems.contains(i)) {
                                    selectedItems.remove(Integer.valueOf(i));
                                }
                            }
                        })
                .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }
}
