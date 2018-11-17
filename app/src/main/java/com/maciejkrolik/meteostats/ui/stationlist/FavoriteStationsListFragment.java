package com.maciejkrolik.meteostats.ui.stationlist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maciejkrolik.meteostats.MeteoStatsApplication;
import com.maciejkrolik.meteostats.data.StationRepository;
import com.maciejkrolik.meteostats.data.model.Station;

import java.util.List;

public class FavoriteStationsListFragment extends StationListBaseFragment {

    private List<Station> favoriteStations;

    @Override
    boolean hasInternetConnectivity() {
        // In favorite list we use only local storage so we do not need to check internet connectivity
        return true;
    }

    @Override
    void setupViewModel() {
        StationRepository stationRepository =
                ((MeteoStatsApplication) getActivity().getApplication())
                        .getApplicationComponent()
                        .getStationRepository();

        FavoriteStationsViewModel viewModel = ViewModelProviders
                .of(this, new StationListViewModelFactory(stationRepository))
                .get(FavoriteStationsViewModel.class);

        viewModel.getFavoriteStations().observe(this, new Observer<List<Station>>() {
            @Override
            public void onChanged(@Nullable List<Station> stations) {
                favoriteStations = stations;
                setupChosenStations();
            }
        });
    }

    @NonNull
    @Override
    List<Station> getChosenStations() {
        return favoriteStations;
    }
}
