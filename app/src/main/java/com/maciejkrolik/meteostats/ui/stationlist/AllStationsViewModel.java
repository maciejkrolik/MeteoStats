package com.maciejkrolik.meteostats.ui.stationlist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.maciejkrolik.meteostats.data.model.Station;
import com.maciejkrolik.meteostats.di.ApplicationComponent;
import com.maciejkrolik.meteostats.di.DaggerApplicationComponent;

import java.util.List;

public class AllStationsViewModel extends ViewModel {

    private LiveData<List<Station>> allStations;

    public LiveData<List<Station>> getAllStations() {
        if (allStations == null) {
            loadAllStations();
        }
        return allStations;
    }

    private void loadAllStations() {
        ApplicationComponent applicationComponent = DaggerApplicationComponent.create();
        allStations = applicationComponent.getStationRepository().getAllStations();
    }
}
