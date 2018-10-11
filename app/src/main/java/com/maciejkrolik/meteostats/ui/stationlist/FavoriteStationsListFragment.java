package com.maciejkrolik.meteostats.ui.stationlist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maciejkrolik.meteostats.data.model.Station;

import java.util.List;

public class FavoriteStationsListFragment extends StationListBaseFragment {

    private List<Station> favoriteStations;

    @Override
    void setupViewModel() {
        AllStationsViewModel viewModel = ViewModelProviders
                .of(this)
                .get(AllStationsViewModel.class);

        viewModel.getAllStations().observe(this, new Observer<List<Station>>() {
            @Override
            public void onChanged(@Nullable List<Station> stations) {
                favoriteStations = stations;
                setupChosenStations();
            }
        });
    }

    @NonNull
    @Override
    List<Station> setChosenStations() {
        return favoriteStations;
    }
}
