package com.maciejkrolik.meteostats.ui.stationdetail.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.maciejkrolik.meteostats.data.model.StationMeasurementsList;
import com.maciejkrolik.meteostats.di.ApplicationComponent;
import com.maciejkrolik.meteostats.di.DaggerApplicationComponent;

public class WeatherDataViewModel extends ViewModel {

    private LiveData<StationMeasurementsList> stationMeasurementsList;

    private final int stationNumber;
    private final String measurementSymbol;
    private final String date;

    WeatherDataViewModel(int stationNumber,
                         String measurementSymbol,
                         String date) {
        this.stationNumber = stationNumber;
        this.measurementSymbol = measurementSymbol;
        this.date = date;
    }

    public LiveData<StationMeasurementsList> getMeasurementsList() {
        if (stationMeasurementsList == null) {
            loadMeasurementsList();
        }
        return stationMeasurementsList;
    }

    private void loadMeasurementsList() {
        ApplicationComponent applicationComponent = DaggerApplicationComponent.create();
        stationMeasurementsList = applicationComponent
                .getStationRepository()
                .getMeasurementsList(stationNumber, measurementSymbol, date);
    }
}
