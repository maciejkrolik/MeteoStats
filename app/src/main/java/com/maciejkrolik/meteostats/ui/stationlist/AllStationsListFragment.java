package com.maciejkrolik.meteostats.ui.stationlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.data.model.Station;
import com.maciejkrolik.meteostats.data.model.StationList;
import com.maciejkrolik.meteostats.data.service.GdanskWatersClient;
import com.maciejkrolik.meteostats.ui.stationdetail.StationDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllStationsListFragment extends Fragment
        implements OnStationClickListener {

    public static final String STATION_NAME_MESSAGE =
            "com.maciejkrolik.meteostats.ui.stationlist.STATION_NAME_MESSAGE";
    public static final String STATION_NUMBER_MESSAGE =
            "com.maciejkrolik.meteostats.ui.stationlist.STATION_NUMBER_MESSAGE";
    public static final String STATION_DATA_MESSAGE =
            "com.maciejkrolik.meteostats.ui.stationlist.STATION_DATA_MESSAGE";

    private RecyclerView.Adapter adapter;

    private List<Station> allStations = new ArrayList<>();
    private List<Station> visibleStations = new ArrayList<>();

    public AllStationsListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_stations_list, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.stations_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StationListAdapter(visibleStations, this);
        recyclerView.setAdapter(adapter);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://pomiary.gdanskiewody.pl")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        GdanskWatersClient client = retrofit.create(GdanskWatersClient.class);
        Call<StationList> call = client.listStations();

        call.enqueue(new Callback<StationList>() {
            @Override
            public void onResponse(Call<StationList> call, Response<StationList> response) {
                StationList stationList = response.body();

                if (stationList != null) {
                    allStations.addAll(stationList.getData());
                    setChosenStations();
                } else {
                    Toast.makeText(getActivity(),
                            R.string.retrofit_data_error_message,
                            Toast.LENGTH_SHORT).show();
                }

                Log.d("TEST", "Downloaded data from the internet");
            }

            @Override
            public void onFailure(Call<StationList> call, Throwable t) {
                Toast.makeText(getActivity(),
                        R.string.retrofit_error_message, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    void setChosenStations() {
        visibleStations.clear();
        visibleStations.addAll(getChosenStations(allStations));
        adapter.notifyDataSetChanged();
    }

    private List<Station> getChosenStations(List<Station> allStations) {
        List<Station> chosenStations = new ArrayList<>();

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (sharedPreferences.getBoolean("show_rain", true)) {
            for (Station station : allStations) {
                if (station.isRain()) {
                    if (!chosenStations.contains(station))
                        chosenStations.add(station);
                }
            }
        }
        if (sharedPreferences.getBoolean("show_water", true)) {
            for (Station station : allStations) {
                if (station.isWater()) {
                    if (!chosenStations.contains(station))
                        chosenStations.add(station);
                }
            }
        }
        if (sharedPreferences.getBoolean("show_winddir", true)) {
            for (Station station : allStations) {
                if (station.isWinddir()) {
                    if (!chosenStations.contains(station))
                        chosenStations.add(station);
                }
            }
        }
        if (sharedPreferences.getBoolean("show_windlevel", true)) {
            for (Station station : allStations) {
                if (station.isWindlevel()) {
                    if (!chosenStations.contains(station))
                        chosenStations.add(station);
                }
            }
        }
        return chosenStations;
    }

    @Override
    public void onStationClick(int position) {
        String stationName = visibleStations.get(position).getName();
        int stationNumber = visibleStations.get(position).getNo();

        boolean[] availableStationData = {
                visibleStations.get(position).isRain(),
                visibleStations.get(position).isWater(),
                visibleStations.get(position).isWinddir(),
                visibleStations.get(position).isWindlevel()
        };

        Intent intent = new Intent(getActivity(), StationDetailsActivity.class);
        intent.putExtra(STATION_NAME_MESSAGE, stationName);
        intent.putExtra(STATION_NUMBER_MESSAGE, stationNumber);
        intent.putExtra(STATION_DATA_MESSAGE, availableStationData);
        startActivity(intent);
    }
}
