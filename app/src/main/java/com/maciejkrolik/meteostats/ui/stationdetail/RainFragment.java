package com.maciejkrolik.meteostats.ui.stationdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;
import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.data.model.StationMeasurementList;
import com.maciejkrolik.meteostats.data.service.GdanskWatersClient;
import com.maciejkrolik.meteostats.ui.stationlist.AllStationsListFragment;
import com.maciejkrolik.meteostats.util.DateUtils;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RainFragment extends Fragment {

    private ProgressBar progressBar;
    private LinearLayout weatherDataLayout;
    private BarChartView barChart;
    private TextView textView;
    private TableRow tableValueRow;
    private TableRow tableTimeRow;

    public RainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rain_weather_data, container, false);

        weatherDataLayout = rootView.findViewById(R.id.weather_data_layout);
        textView = rootView.findViewById(R.id.data_text_view);
        progressBar = rootView.findViewById(R.id.rain_fragment_progress_bar);
        barChart = rootView.findViewById(R.id.rain_bar_chart);
        tableValueRow = rootView.findViewById(R.id.rain_table_value_row);
        tableTimeRow = rootView.findViewById(R.id.rain_table_time_row);

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
                        stringBuilder.append(measurement.get(0).substring(10, 13));
                        stringBuilder.append(" - ");
                        String value = measurement.get(1) == null ? "-" : measurement.get(1);
                        stringBuilder.append(value);
                        stringBuilder.append("\n");
                    }
                    textView.setText(stringBuilder.toString());

                    BarSet barSet = new BarSet();
                    Context context = getContext();

                    for (List<String> measurement : stationMeasurementList.getData()) {
                        if (measurement.get(1) != null) {
                            float value = Float.parseFloat(measurement.get(1));
                            String time = measurement.get(0).substring(10, 13);
                            barSet.addBar(time, value);

                            TextView valueTextView = new TextView(context);
                            valueTextView.setText(String.valueOf(value));
                            TextView timeTextView = new TextView(context);
                            timeTextView.setText(time);

                            tableValueRow.addView(valueTextView);
                            tableTimeRow.addView(timeTextView);
                        }
                    }

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
