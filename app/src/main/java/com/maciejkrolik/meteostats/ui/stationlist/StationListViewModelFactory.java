package com.maciejkrolik.meteostats.ui.stationlist;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.maciejkrolik.meteostats.data.StationRepository;

public class StationListViewModelFactory implements ViewModelProvider.Factory {

    private final StationRepository stationRepository;

    public StationListViewModelFactory(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AllStationsViewModel(stationRepository);
    }
}
