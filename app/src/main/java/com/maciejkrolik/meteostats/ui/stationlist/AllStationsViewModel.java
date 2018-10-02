package com.maciejkrolik.meteostats.ui.stationlist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.maciejkrolik.meteostats.data.model.Station;
import com.maciejkrolik.meteostats.di.ApplicationComponent;
import com.maciejkrolik.meteostats.di.DaggerApplicationComponent;

import java.util.List;

public class AllStationsViewModel extends ViewModel {

    private final LiveData<List<Station>> allStations;

    public AllStationsViewModel() {
        ApplicationComponent applicationComponent = DaggerApplicationComponent.create();
        allStations = applicationComponent.getStationRepository().getAllStations();
    }

    public LiveData<List<Station>> getAllStations() {
        return allStations;
    }
}
