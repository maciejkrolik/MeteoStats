package com.maciejkrolik.meteostats.ui.stationdetail.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.maciejkrolik.meteostats.data.model.StationMeasurementsList;
import com.maciejkrolik.meteostats.di.ApplicationComponent;
import com.maciejkrolik.meteostats.di.DaggerApplicationComponent;

public class WeatherDataViewModel extends ViewModel {

    private final LiveData<StationMeasurementsList> stationMeasurementsList;

    WeatherDataViewModel(int stationNumber,
                         String measurementSymbol,
                         String date) {
        ApplicationComponent applicationComponent = DaggerApplicationComponent.create();
        stationMeasurementsList = applicationComponent
                .getStationRepository()
                .getMeasurementsList(stationNumber, measurementSymbol, date);
    }

    public LiveData<StationMeasurementsList> getMeasurementsList() {
        return stationMeasurementsList;
    }
}
