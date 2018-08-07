package com.maciejkrolik.meteostats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.maciejkrolik.meteostats.model.StationMeasurementList;
import com.maciejkrolik.meteostats.service.GdanskWatersClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StationDetailsActivity extends AppCompatActivity {

    private String stationName;
    private int stationNumber;

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);

        Intent intent = getIntent();
        stationName = intent.getStringExtra(StationListActivity.STATION_NAME_MESSAGE);
        stationNumber = intent.getIntExtra(StationListActivity.STATION_NUMBER_MESSAGE, -1);

        setTitle(stationName);

        resultTextView = findViewById(R.id.result_details_text_view);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://pomiary.gdanskiewody.pl")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        GdanskWatersClient client = retrofit.create(GdanskWatersClient.class);
        Call<StationMeasurementList> call = client.getMeasurement(stationNumber, "rain", "2018-08-07");

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
                    Toast.makeText(StationDetailsActivity.this, "Błąd pobranych danych!",
                            Toast.LENGTH_SHORT).show();
                }

                Log.d("TEST", "Downloaded data from the internet - DETAILS");
            }

            @Override
            public void onFailure(Call<StationMeasurementList> call, Throwable t) {
                Toast.makeText(StationDetailsActivity.this,
                        "Coś poszło nie tak!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
