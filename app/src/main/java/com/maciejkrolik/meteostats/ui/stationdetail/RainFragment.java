package com.maciejkrolik.meteostats.ui.stationdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;
import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.data.model.StationMeasurementList;
import com.maciejkrolik.meteostats.data.service.GdanskWatersClient;
import com.maciejkrolik.meteostats.ui.stationlist.AllStationsListFragment;
import com.maciejkrolik.meteostats.util.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RainFragment extends Fragment {

    private ProgressBar progressBar;
    private LinearLayout weatherDataLayout;
    private BarChartView barChart;

    private List<String> measurementValues = new ArrayList<>();
    private List<String> measurementTimes = new ArrayList<>();

    public RainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rain_weather_data, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.rain_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView.setLayoutManager(layoutManager);
        final RecyclerView.Adapter adapter = new MeasurementListAdapter(measurementValues, measurementTimes);
        recyclerView.setAdapter(adapter);

        weatherDataLayout = rootView.findViewById(R.id.weather_data_layout);
        progressBar = rootView.findViewById(R.id.rain_fragment_progress_bar);
        barChart = rootView.findViewById(R.id.rain_bar_chart);

        int stationNumber = getArguments().getInt(AllStationsListFragment.STATION_NUMBER_MESSAGE);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://pomiary.gdanskiewody.pl")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        GdanskWatersClient client = retrofit.create(GdanskWatersClient.class);
        Call<StationMeasurementList> call = client.getMeasurement(stationNumber,
                "rain", DateUtils.getTodayDateAsString());

        call.enqueue(new Callback<StationMeasurementList>() {
            @Override
            public void onResponse(Call<StationMeasurementList> call, Response<StationMeasurementList> response) {
                StationMeasurementList stationMeasurementList = response.body();

                if (stationMeasurementList != null) {
                    BarSet barSet = new BarSet();

                    measurementValues.add("[mm]");
                    measurementTimes.add("[h]");

                    for (Map.Entry<String, Float> measurement : stationMeasurementList.getData().entrySet()) {
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

                Log.d("TEST", "Downloaded data from the internet - DETAILS");
            }

            @Override
            public void onFailure(Call<StationMeasurementList> call, Throwable t) {
                Toast.makeText(getActivity(),
                        R.string.retrofit_error_message, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
