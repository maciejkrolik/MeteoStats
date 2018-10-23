package com.maciejkrolik.meteostats.ui.stationlist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.maciejkrolik.meteostats.data.StationRepository;
import com.maciejkrolik.meteostats.data.model.Station;

import java.util.List;

public class AllStationsViewModel extends ViewModel {

    private LiveData<List<Station>> allStations;

    private final StationRepository stationRepository;

    public AllStationsViewModel(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public LiveData<List<Station>> getAllStations() {
        if (allStations == null) {
            loadAllStations();
        }
        return allStations;
    }

    private void loadAllStations() {
        allStations = stationRepository.getAllStations();
    }
}
