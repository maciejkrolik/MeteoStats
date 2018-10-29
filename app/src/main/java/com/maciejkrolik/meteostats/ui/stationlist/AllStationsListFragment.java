package com.maciejkrolik.meteostats.ui.stationlist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maciejkrolik.meteostats.MeteoStatsApplication;
import com.maciejkrolik.meteostats.data.StationRepository;
import com.maciejkrolik.meteostats.data.model.Station;

import java.util.List;

public class AllStationsListFragment extends StationListBaseFragment {

    private List<Station> allStations;

    @Override
    void setupViewModel() {
        StationRepository stationRepository =
                ((MeteoStatsApplication) getActivity().getApplication())
                        .getApplicationComponent()
                        .getStationRepository();

        AllStationsViewModel viewModel = ViewModelProviders
                .of(this, new StationListViewModelFactory(stationRepository))
                .get(AllStationsViewModel.class);

        viewModel.getAllStations().observe(this, new Observer<List<Station>>() {
            @Override
            public void onChanged(@Nullable List<Station> stations) {
                allStations = stations;
                setupChosenStations();
            }
        });
    }

    @NonNull
    @Override
    List<Station> getChosenStations() {
        return allStations;
    }
}
