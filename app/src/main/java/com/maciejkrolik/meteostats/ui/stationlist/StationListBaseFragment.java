package com.maciejkrolik.meteostats.ui.stationlist;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class StationListBaseFragment extends Fragment
        implements OnStationClickListener {

    public static final String STATION_NAME_MESSAGE =
            "com.maciejkrolik.meteostats.ui.stationlist.STATION_NAME_MESSAGE";
    public static final String STATION_NUMBER_MESSAGE =
            "com.maciejkrolik.meteostats.ui.stationlist.STATION_NUMBER_MESSAGE";
    public static final String STATION_DATA_MESSAGE =
            "com.maciejkrolik.meteostats.ui.stationlist.STATION_DATA_MESSAGE";

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private TextView infoTextView;

    private List<Station> stations;
    private List<Station> visibleStations = new ArrayList<>();

    public StationListBaseFragment() {
    }

    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_station_list, container, false);

        progressBar = rootView.findViewById(R.id.stations_progress_bar);
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
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewModel();
    }

    abstract void setupViewModel();

    final void setupChosenStations() {
        progressBar.setVisibility(View.GONE);

        this.stations = setChosenStations();

        visibleStations.clear();
        visibleStations.addAll(filterChosenStations(this.stations));
        if (visibleStations.isEmpty()) hideList();
        else {
            showList();
            adapter.notifyDataSetChanged();
        }
    }

    @NonNull
    abstract List<Station> setChosenStations();

    private List<Station> filterChosenStations(List<Station> allStations) {
        Set<Station> chosenStations = new HashSet<>();

        boolean[] checkedItems = SharedPreferenceUtils.getCheckedItemsInfo(getContext());

        for (Station station : allStations) {
            boolean[] stationData = {
                    station.isRain(),
                    station.isWater(),
                    station.isWinddir(),
                    station.isWindlevel()
            };

            for (int i = 0; i < checkedItems.length; i++) {
                if (checkedItems[i] && stationData[i]) {
                    chosenStations.add(station);
                }
            }
        }

        return new ArrayList<>(chosenStations);
    }

    @Override
    public final void onStationClick(int position) {
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

    final void searchStationList(String text) {
        visibleStations.clear();

        List<Station> filteredStations = new ArrayList<>(filterChosenStations(stations));

        if (text.isEmpty()) {
            visibleStations.addAll(filteredStations);
        } else {
            text = text.toLowerCase();
            for (Station station : filteredStations) {
                if (station.getName().toLowerCase().contains(text)) {
                    visibleStations.add(station);
                }
            }
        }

        if (visibleStations.isEmpty()) hideList();
        else {
            showList();
            adapter.notifyDataSetChanged();
        }
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
