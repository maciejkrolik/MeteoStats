package com.maciejkrolik.meteostats.ui.stationlist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.maciejkrolik.meteostats.data.StationRepository;
import com.maciejkrolik.meteostats.data.model.Station;

import java.util.List;

public class FavoriteStationsViewModel extends ViewModel {

    private LiveData<List<Station>> favoriteStations;

    private final StationRepository stationRepository;

    public FavoriteStationsViewModel(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public LiveData<List<Station>> getFavoriteStations() {
        if (favoriteStations == null) {
            loadFavoriteStations();
        }
        return favoriteStations;
    }

    private void loadFavoriteStations() {
        favoriteStations = stationRepository.getListOfFavoriteStations();
    }
}
