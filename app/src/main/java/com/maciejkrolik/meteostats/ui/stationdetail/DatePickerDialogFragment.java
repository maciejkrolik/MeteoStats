package com.maciejkrolik.meteostats.ui.stationdetail;

import android.app.Dialog;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
    }
}
