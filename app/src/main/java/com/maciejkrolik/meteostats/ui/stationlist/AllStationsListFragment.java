package com.maciejkrolik.meteostats.ui.stationlist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.maciejkrolik.meteostats.data.model.Station;

import java.util.List;

public class AllStationsListFragment extends StationListBaseFragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AllStationsViewModel viewModel = ViewModelProviders
                .of(this)
                .get(AllStationsViewModel.class);

        viewModel.getAllStations().observe(this, new Observer<List<Station>>() {
            @Override
            public void onChanged(@Nullable List<Station> stations) {
                allStations = stations;
                progressBar.setVisibility(View.GONE);
                setChosenStations();
            }
        });
    }
}
