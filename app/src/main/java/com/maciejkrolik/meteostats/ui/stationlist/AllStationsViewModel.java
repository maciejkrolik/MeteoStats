package com.maciejkrolik.meteostats.ui.stationlist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.maciejkrolik.meteostats.data.model.Station;
import com.maciejkrolik.meteostats.data.repository.StationRepository;

import java.util.List;

public class AllStationsViewModel extends ViewModel {

    private final LiveData<List<Station>> allStations;

    public AllStationsViewModel() {
        allStations = StationRepository.getInstance().getAllStations();
    }

    public LiveData<List<Station>> getAllStations() {
        return allStations;
    }
}
