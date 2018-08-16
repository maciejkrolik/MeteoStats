package com.maciejkrolik.meteostats.ui.stationdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.ui.stationlist.AllStationsListFragment;

public class StationDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);

        Intent intent = getIntent();
        String stationName = intent.getStringExtra(AllStationsListFragment.STATION_NAME_MESSAGE);
        int stationNumber = intent.getIntExtra(AllStationsListFragment.STATION_NUMBER_MESSAGE, -1);
        final boolean[] stationData = intent.getBooleanArrayExtra(AllStationsListFragment.STATION_DATA);

        Bundle bundle = new Bundle();
        bundle.putInt("stationNumber", stationNumber);

        RainFragment rainFragment = new RainFragment();
        rainFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.rain_fragment_layout, rainFragment)
                .commit();

        setTitle(stationName);
    }
}
