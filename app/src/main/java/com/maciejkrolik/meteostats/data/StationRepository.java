package com.maciejkrolik.meteostats.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.maciejkrolik.meteostats.data.model.Station;
import com.maciejkrolik.meteostats.data.model.StationList;
import com.maciejkrolik.meteostats.data.model.StationMeasurementsList;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class StationRepository {

    private final GdanskWatersClient stationClient;
    private final StationDao stationDao;

    @Inject
    StationRepository(GdanskWatersClient stationClient, StationDao stationDao) {
        this.stationClient = stationClient;
        this.stationDao = stationDao;
    }

    public LiveData<List<Station>> getAllStations() {
        final MutableLiveData<List<Station>> data = new MutableLiveData<>();

        stationClient.listStations().enqueue(new Callback<StationList>() {

            @Override
            public void onResponse(Call<StationList> call, Response<StationList> response) {
                data.setValue(response.body().getData());

                Log.d("TEST", "Downloaded data from the internet");
            }

            @Override
            public void onFailure(Call<StationList> call, Throwable t) {

            }

        });
        return data;
    }

    public LiveData<StationMeasurementsList> getMeasurementsList(int stationNumber,
                                                                 String measurementSymbol,
                                                                 String date) {
        final MutableLiveData<StationMeasurementsList> data = new MutableLiveData<>();

        stationClient.getMeasurements(stationNumber, measurementSymbol, date).enqueue(new Callback<StationMeasurementsList>() {
            @Override
            public void onResponse(Call<StationMeasurementsList> call, Response<StationMeasurementsList> response) {
                data.setValue(response.body());

                Log.d("TEST", "Downloaded data from the internet - DETAILS");
            }

            @Override
            public void onFailure(Call<StationMeasurementsList> call, Throwable t) {

            }
        });
        return data;
    }

    public LiveData<List<Station>> getListOfFavoriteStations() {
        return stationDao.getFavoriteStations();
    }

    public void saveStation(Station station) {
        stationDao.addToFavoriteStations(station);
    }

    public void deleteStation(Station station) {
        stationDao.deleteFromFavoriteStations(station);
    }
}
