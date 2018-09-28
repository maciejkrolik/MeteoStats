package com.maciejkrolik.meteostats.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.maciejkrolik.meteostats.data.model.Station;
import com.maciejkrolik.meteostats.data.model.StationList;
import com.maciejkrolik.meteostats.data.service.GdanskWatersClient;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class StationRepository {

    private final GdanskWatersClient client;

    @Inject
    StationRepository(GdanskWatersClient client) {
        this.client = client;
    }

    public LiveData<List<Station>> getAllStations() {
        final MutableLiveData<List<Station>> data = new MutableLiveData<>();

        client.listStations().enqueue(new Callback<StationList>() {

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
}
