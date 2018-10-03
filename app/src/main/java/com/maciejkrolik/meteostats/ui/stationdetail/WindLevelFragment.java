package com.maciejkrolik.meteostats.ui.stationdetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Paint;
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

import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.data.model.StationMeasurementsList;
import com.maciejkrolik.meteostats.ui.stationdetail.viewmodel.WeatherDataViewModel;
import com.maciejkrolik.meteostats.ui.stationdetail.viewmodel.WeatherDataViewModelFactory;
import com.maciejkrolik.meteostats.ui.stationlist.StationListBaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WindLevelFragment extends Fragment {

    private ProgressBar progressBar;
    private LinearLayout weatherDataLayout;
    private LineChartView lineChart;

    private int stationNumber;
    private String date;
    private RecyclerView.Adapter adapter;

    private final List<String> measurementValues = new ArrayList<>();
    private final List<String> measurementTimes = new ArrayList<>();

    public WindLevelFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wind_level_data, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.wind_level_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MeasurementsListAdapter(measurementValues, measurementTimes);
        recyclerView.setAdapter(adapter);

        weatherDataLayout = rootView.findViewById(R.id.weather_data_layout);
        progressBar = rootView.findViewById(R.id.wind_level_fragment_progress_bar);
        lineChart = rootView.findViewById(R.id.wind_level_line_chart);

        stationNumber = getArguments().getInt(StationListBaseFragment.STATION_NUMBER_MESSAGE);
        date = getArguments().getString(StationDetailsActivity.DATE);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        WeatherDataViewModel viewModel = ViewModelProviders
                .of(this, new WeatherDataViewModelFactory(
                        stationNumber,
                        "windlevel",
                        date))
                .get(WeatherDataViewModel.class);

        viewModel.getMeasurementsList().observe(this, new Observer<StationMeasurementsList>() {
            @Override
            public void onChanged(@Nullable StationMeasurementsList measurementsList) {
                if (measurementsList != null) {
                    LineSet lineSet = new LineSet();

                    measurementValues.add("[m/s]");
                    measurementTimes.add("[h]");

                    for (Map.Entry<String, Float> measurement : measurementsList.getData().entrySet()) {
                        String time = measurement.getKey().substring(10, 13);
                        if (measurement.getValue() != null) {
                            float value = measurement.getValue();
                            lineSet.addPoint(time, value);

                            measurementValues.add(String.valueOf(value));
                            measurementTimes.add(time);
                        } else {
                            measurementValues.add("-");
                            measurementTimes.add(time);
                        }
                    }

                    adapter.notifyDataSetChanged();

                    Animation animation = new Animation();
                    Paint paint = new Paint();
                    lineChart.setGrid(0, lineSet.size() - 1, paint);
                    lineChart.addData(lineSet);
                    lineChart.show(animation);

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
