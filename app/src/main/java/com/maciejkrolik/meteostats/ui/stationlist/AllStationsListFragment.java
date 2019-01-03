package com.maciejkrolik.meteostats.ui.stationlist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.maciejkrolik.meteostats.MeteoStatsApplication;
import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.data.StationRepository;
import com.maciejkrolik.meteostats.data.model.Station;
import com.maciejkrolik.meteostats.ui.stationlist.viewmodel.AllStationsViewModel;
import com.maciejkrolik.meteostats.ui.stationlist.viewmodel.StationListViewModelFactory;
import com.maciejkrolik.meteostats.util.NetworkUtils;

import java.util.List;

public class AllStationsListFragment extends StationListBaseFragment {

    private List<Station> allStations;

    @Override
    void setActivityTitle() {
        if (getActivity() != null) {
            getActivity().setTitle(R.string.nav_all_stations);
        }
    }

    @Override
    boolean hasInternetConnectivity() {
        if (!NetworkUtils.isConnected(getContext())) {
            infoTextView.setText(R.string.internet_connection_error);
            progressBar.setVisibility(View.GONE);
            infoTextView.setVisibility(View.VISIBLE);
            checkButton.setVisibility(View.VISIBLE);
            return false;

        } else {
            return true;
        }
    }

    @Override
    void setupViewModel() {
        StationRepository stationRepository =
                ((MeteoStatsApplication) getActivity().getApplication())
                        .getApplicationComponent()
                        .getStationRepository();

        AllStationsViewModel viewModel = ViewModelProviders
                .of(this, new StationListViewModelFactory(stationRepository))
                .get(AllStationsViewModel.class);

        viewModel.getAllStations().observe(this, new Observer<List<Station>>() {
            @Override
            public void onChanged(@Nullable List<Station> stations) {
                allStations = stations;
                setupChosenStations();
            }
        });
    }

    @NonNull
    @Override
    List<Station> getChosenStations() {
        return allStations;
    }
}
