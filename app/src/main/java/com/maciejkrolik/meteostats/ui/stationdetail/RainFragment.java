package com.maciejkrolik.meteostats.ui.stationdetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;
import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.data.model.StationMeasurementsList;
import com.maciejkrolik.meteostats.ui.stationdetail.viewmodel.RainViewModel;
import com.maciejkrolik.meteostats.ui.stationdetail.viewmodel.StationDetailViewModelFactory;
import com.maciejkrolik.meteostats.ui.stationlist.StationListBaseFragment;
import com.maciejkrolik.meteostats.util.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RainFragment extends Fragment {

    private ProgressBar progressBar;
    private LinearLayout weatherDataLayout;
    private BarChartView barChart;

    private int stationNumber;
    private RecyclerView.Adapter adapter;

    private final List<String> measurementValues = new ArrayList<>();
    private final List<String> measurementTimes = new ArrayList<>();

    public RainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rain_data, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.rain_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MeasurementsListAdapter(measurementValues, measurementTimes);
        recyclerView.setAdapter(adapter);

        weatherDataLayout = rootView.findViewById(R.id.weather_data_layout);
        progressBar = rootView.findViewById(R.id.rain_fragment_progress_bar);
        barChart = rootView.findViewById(R.id.rain_bar_chart);

        stationNumber = getArguments().getInt(StationListBaseFragment.STATION_NUMBER_MESSAGE);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RainViewModel viewModel = ViewModelProviders
                .of(this, new StationDetailViewModelFactory(
                        stationNumber,
                        "rain",
                        DateUtils.getTodayDateAsString()))
                .get(RainViewModel.class);

        viewModel.getMeasurementsList().observe(this, new Observer<StationMeasurementsList>() {
            @Override
            public void onChanged(@Nullable StationMeasurementsList measurementsList) {
                if (measurementsList != null) {
                    BarSet barSet = new BarSet();

                    measurementValues.add("[mm]");
                    measurementTimes.add("[h]");

                    for (Map.Entry<String, Float> measurement : measurementsList.getData().entrySet()) {
                        String time = measurement.getKey().substring(10, 13);
                        if (measurement.getValue() != null) {
                            float value = measurement.getValue();
                            barSet.addBar(time, value);

                            measurementValues.add(String.valueOf(value));
                            measurementTimes.add(time);
                        } else {
                            measurementValues.add("-");
                            measurementTimes.add(time);
                        }
                    }

                    adapter.notifyDataSetChanged();

                    barChart.addData(barSet);
                    barChart.show();

                } else {
                    Toast.makeText(getActivity(),
                            R.string.retrofit_data_error_message,
                            Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);
                weatherDataLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
