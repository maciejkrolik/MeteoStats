package com.maciejkrolik.meteostats.ui.stationdetail.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.maciejkrolik.meteostats.data.StationRepository;
import com.maciejkrolik.meteostats.data.model.StationMeasurementsList;

public class WeatherDataViewModel extends ViewModel {

    private LiveData<StationMeasurementsList> stationMeasurementsList;

    private final int stationNumber;
    private final String measurementSymbol;
    private final String date;

    private final StationRepository stationRepository;

    WeatherDataViewModel(int stationNumber,
                         String measurementSymbol,
                         String date,
                         StationRepository stationRepository) {
        this.stationNumber = stationNumber;
        this.measurementSymbol = measurementSymbol;
        this.date = date;
        this.stationRepository = stationRepository;
    }

    public LiveData<StationMeasurementsList> getMeasurementsList() {
        if (stationMeasurementsList == null) {
            loadMeasurementsList();
        }
        return stationMeasurementsList;
    }

    private void loadMeasurementsList() {
        stationMeasurementsList =
                stationRepository.getMeasurementsList(stationNumber, measurementSymbol, date);
    }
}
