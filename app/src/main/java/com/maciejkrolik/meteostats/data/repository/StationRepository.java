package com.maciejkrolik.meteostats.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.maciejkrolik.meteostats.data.model.Station;
import com.maciejkrolik.meteostats.data.model.StationList;
import com.maciejkrolik.meteostats.data.service.GdanskWatersClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StationRepository {

    private GdanskWatersClient client;
    private static StationRepository stationRepository;

    private StationRepository() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://pomiary.gdanskiewody.pl")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        client = retrofit.create(GdanskWatersClient.class);
    }

    public synchronized static StationRepository getInstance() {
        //TODO Replace with Dagger 2
        if (stationRepository == null) {
            stationRepository = new StationRepository();

        }
        return stationRepository;
    }

    public LiveData<List<Station>> getAllStations() {
        final MutableLiveData<List<Station>> data = new MutableLiveData<>();
        client.listStations().enqueue(new Callback<StationList>() {
            @Override
            public void onResponse(Call<StationList> call, Response<StationList> response) {
                StationList stationList = response.body();
                data.setValue(stationList.getData());

                Log.d("TEST", "Downloaded data from the internet");
            }

            @Override
            public void onFailure(Call<StationList> call, Throwable t) {

            }
        });
        return data;
    }
}
