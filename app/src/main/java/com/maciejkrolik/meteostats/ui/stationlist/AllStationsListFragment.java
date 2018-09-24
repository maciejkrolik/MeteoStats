package com.maciejkrolik.meteostats.ui.stationlist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.data.model.Station;
import com.maciejkrolik.meteostats.ui.stationdetail.StationDetailsActivity;
import com.maciejkrolik.meteostats.util.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllStationsListFragment extends Fragment
        implements OnStationClickListener {

    public static final String STATION_NAME_MESSAGE =
            "com.maciejkrolik.meteostats.ui.stationlist.STATION_NAME_MESSAGE";
    public static final String STATION_NUMBER_MESSAGE =
            "com.maciejkrolik.meteostats.ui.stationlist.STATION_NUMBER_MESSAGE";
    public static final String STATION_DATA_MESSAGE =
            "com.maciejkrolik.meteostats.ui.stationlist.STATION_DATA_MESSAGE";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private TextView infoTextView;
    private ProgressBar progressBar;

    private List<Station> allStations = new ArrayList<>();
    private List<Station> visibleStations = new ArrayList<>();

    public AllStationsListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_stations_list, container, false);

        progressBar = rootView.findViewById(R.id.allStationsProgressBar);
        infoTextView = rootView.findViewById(R.id.info_text_view);
        recyclerView = rootView.findViewById(R.id.stations_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StationListAdapter(visibleStations, this);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AllStationsViewModel viewModel =
                ViewModelProviders.of(this).get(AllStationsViewModel.class);
        viewModel.getAllStations().observe(this, new Observer<List<Station>>() {
            @Override
            public void onChanged(@Nullable List<Station> stations) {
                allStations = stations;
                progressBar.setVisibility(View.GONE);
                setChosenStations();
            }
        });
    }

    void setChosenStations() {
        visibleStations.clear();
        visibleStations.addAll(getChosenStations(allStations));
        if (visibleStations.isEmpty()) hideList();
        else {
            showList();
            adapter.notifyDataSetChanged();
        }
    }

    private List<Station> getChosenStations(List<Station> allStations) {
        List<Station> chosenStations = new ArrayList<>();

        boolean[] checkedItems = SharedPreferenceUtils.getCheckedItemsInfo(getContext());

        for (Station station : allStations) {
            boolean[] stationData = {
                    station.isRain(),
                    station.isWater(),
                    station.isWinddir(),
                    station.isWindlevel()
            };

            if (Arrays.equals(checkedItems, stationData)) {
                chosenStations.add(station);
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

    void filterStationsList(String newText) {
    }

    private void showList() {
        infoTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void hideList() {
        recyclerView.setVisibility(View.GONE);
        infoTextView.setVisibility(View.VISIBLE);
    }
}
