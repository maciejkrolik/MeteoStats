package com.maciejkrolik.meteostats.ui.stationdetail.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class WeatherDataViewModelFactory implements ViewModelProvider.Factory {

    private final int stationNumber;
    private final String measurementSymbol;
    private final String date;

    public WeatherDataViewModelFactory(int stationNumber,
                                       String measurementSymbol,
                                       String date) {
        this.stationNumber = stationNumber;
        this.measurementSymbol = measurementSymbol;
        this.date = date;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WeatherDataViewModel(stationNumber, measurementSymbol, date);
    }
}
