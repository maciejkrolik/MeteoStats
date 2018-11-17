package com.maciejkrolik.meteostats.ui.stationlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        implements OnStationClickListener, DialogInterface.OnDismissListener {

    public static final String EXTRA_STATION =
            "com.maciejkrolik.meteostats.ui.stationlist.EXTRA_STATION";
    private static final String FILTER_DIALOG_TAG = "FILTER_DIALOG_TAG";

    ProgressBar progressBar;
    TextView infoTextView;
    Button checkButton;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<Station> stations;
    private final List<Station> visibleStations = new ArrayList<>();

    public StationListBaseFragment() {
    }

    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_station_list, container, false);

        setHasOptionsMenu(true);

        progressBar = rootView.findViewById(R.id.connectivity_progress_bar);
        infoTextView = rootView.findViewById(R.id.connectivity_text_view);
        checkButton = rootView.findViewById(R.id.check_internet_button);
        recyclerView = rootView.findViewById(R.id.stations_recycler_view);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StationListAdapter(visibleStations, this);
        recyclerView.setAdapter(adapter);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasInternetConnectivity()) {
                    checkButton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    setupViewModel();
                }
            }
        });

        return rootView;
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (hasInternetConnectivity()) {
            setupViewModel();
        }
    }

    abstract boolean hasInternetConnectivity();

    abstract void setupViewModel();

    final void setupChosenStations() {
        stations = getChosenStations();

        if (stations != null) {
            progressBar.setVisibility(View.GONE);

            visibleStations.clear();
            visibleStations.addAll(filterChosenStations(stations));

            setVisibilityOfTheList();
        }
    }

    abstract List<Station> getChosenStations();

    private List<Station> filterChosenStations(List<Station> allStations) {
        Set<Station> chosenStations = new HashSet<>();

        boolean[] checkedItems = SharedPreferenceUtils.getCheckedItemsInfo(getContext());

        for (Station station : allStations) {
            boolean[] stationData = {
                    station.hasRain(),
                    station.hasWater(),
                    station.hasWindDir(),
                    station.hasWindLevel()
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.station_list, menu);
        MenuItem item = menu.findItem(R.id.action_search_list);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchStationList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchStationList(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_dialog) {
            showFilterDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        FragmentManager fragmentManager = getChildFragmentManager();
        DialogFragment dialogFragment = new FilterStationsDialogFragment();
        dialogFragment.show(fragmentManager, FILTER_DIALOG_TAG);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        setupChosenStations();
    }

    @Override
    public final void onStationClick(int position) {
        Station station = visibleStations.get(position);
        Intent intent = new Intent(getActivity(), StationDetailsActivity.class);
        intent.putExtra(EXTRA_STATION, station);
        startActivity(intent);
    }

    final void searchStationList(String text) {
        if (stations != null) {
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

            setVisibilityOfTheList();
        }
    }

    private void setVisibilityOfTheList() {
        if (visibleStations.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            infoTextView.setText(R.string.info_no_stations);
            infoTextView.setVisibility(View.VISIBLE);
        } else {
            infoTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }
}
