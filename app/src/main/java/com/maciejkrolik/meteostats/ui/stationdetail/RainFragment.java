package com.maciejkrolik.meteostats.ui.stationdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.data.model.StationMeasurementList;
import com.maciejkrolik.meteostats.data.service.GdanskWatersClient;
import com.maciejkrolik.meteostats.ui.stationlist.AllStationsListFragment;
import com.maciejkrolik.meteostats.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RainFragment extends Fragment {

    private LinearLayout weatherDataLayout;
    private TextView rainDataTextView;
    private ProgressBar progressBar;
    private BarChart barChart;

    public RainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rain_weather_data, container, false);
        weatherDataLayout = rootView.findViewById(R.id.weather_data_layout);
        rainDataTextView = rootView.findViewById(R.id.data_text_view);
        progressBar = rootView.findViewById(R.id.rainFragmentProgressBar);
        barChart = rootView.findViewById(R.id.barChart);
        barChart.setNoDataText("Loading chart...");
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

                    StringBuilder stringBuilder = new StringBuilder();
                    for (List<String> measurement : stationMeasurementList.getData()) {
                        stringBuilder.append(measurement.get(0));
                        stringBuilder.append(" - ");
                        stringBuilder.append(measurement.get(1));
                        stringBuilder.append("\n");
                    }
                    rainDataTextView.setText(stringBuilder.toString());

                    List<BarEntry> entries = new ArrayList<>();
                    int counter = 0;
                    for (List<String> measurement : stationMeasurementList.getData()) {
                        float value;
                        if (measurement.get(1) != null) {
                            value = Float.parseFloat(measurement.get(1));
                        } else {
                            value = 0;
                        }
                        entries.add(new BarEntry(counter, value));
                        counter++;
                    }
                    BarDataSet dataSet = new BarDataSet(entries, "Rain");

                    BarData barData = new BarData(dataSet);
                    barChart.setData(barData);
                    barChart.invalidate();

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
