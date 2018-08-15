package com.maciejkrolik.meteostats.ui.stationlist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maciejkrolik.meteostats.R;

public class AllStationsListFragment extends Fragment
        implements OnStationClickListener, DialogInterface.OnDismissListener {

    public AllStationsListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_all_stations_list, container, false);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }

    @Override
    public void onStationClick(int position) {

    }
}
