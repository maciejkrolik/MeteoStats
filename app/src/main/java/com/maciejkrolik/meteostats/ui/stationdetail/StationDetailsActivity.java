package com.maciejkrolik.meteostats.ui.stationdetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.data.model.StationMeasurementList;
import com.maciejkrolik.meteostats.data.service.GdanskWatersClient;
import com.maciejkrolik.meteostats.ui.stationlist.AllStationsListFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StationDetailsActivity extends AppCompatActivity {

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);

        Intent intent = getIntent();
        String stationName = intent.getStringExtra(AllStationsListFragment.STATION_NAME_MESSAGE);
        int stationNumber = intent.getIntExtra(AllStationsListFragment.STATION_NUMBER_MESSAGE, -1);
        final boolean[] stationData = intent.getBooleanArrayExtra(AllStationsListFragment.STATION_DATA);

        setTitle(stationName);

        resultTextView = findViewById(R.id.result_details_text_view);

        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd");
        String dateString = simpleDateFormat.format(date);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://pomiary.gdanskiewody.pl")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        GdanskWatersClient client = retrofit.create(GdanskWatersClient.class);
        Call<StationMeasurementList> call = client.getMeasurement(stationNumber, "rain", dateString);

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
                    resultTextView.setText(stringBuilder.toString());

                } else {
                    Toast.makeText(StationDetailsActivity.this,
                            R.string.retrofit_data_error_message,
                            Toast.LENGTH_SHORT).show();
                }

                Log.d("TEST", "Downloaded data from the internet - DETAILS");
            }

            @Override
            public void onFailure(Call<StationMeasurementList> call, Throwable t) {
                Toast.makeText(StationDetailsActivity.this,
                        R.string.retrofit_error_message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
