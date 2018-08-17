package com.maciejkrolik.meteostats.ui.stationdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
        final boolean[] stationData = intent.getBooleanArrayExtra(AllStationsListFragment.STATION_DATA_MESSAGE);

        setTitle(stationName);

        Bundle bundle = new Bundle();
        bundle.putInt(AllStationsListFragment.STATION_NUMBER_MESSAGE, stationNumber);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();

        if (stationData[0]) {
            RainFragment rainFragment = new RainFragment();
            rainFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.rain_frame_layout, rainFragment);
        }
        if (stationData[1]) {
            WaterFragment waterFragment = new WaterFragment();
            waterFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.water_frame_layout, waterFragment);
        }
        if (stationData[2]) {
            WinddirFragment winddirFragment = new WinddirFragment();
            winddirFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.winddir_frame_layout, winddirFragment);
        }
        if (stationData[3]) {
            WindlevelFragment windlevelFragment = new WindlevelFragment();
            windlevelFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.windlevel_frame_layout, windlevelFragment);
        }
        fragmentTransaction.commit();
    }
}
