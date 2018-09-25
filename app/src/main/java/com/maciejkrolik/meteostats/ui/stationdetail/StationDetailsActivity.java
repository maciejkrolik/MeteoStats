package com.maciejkrolik.meteostats.ui.stationdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.ui.stationlist.StationListBaseFragment;

public class StationDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);

        Intent intent = getIntent();
        final String stationName = intent.getStringExtra(StationListBaseFragment.STATION_NAME_MESSAGE);
        final int stationNumber =
                intent.getIntExtra(StationListBaseFragment.STATION_NUMBER_MESSAGE, -1);
        final boolean[] stationData =
                intent.getBooleanArrayExtra(StationListBaseFragment.STATION_DATA_MESSAGE);

        setTitle(stationName);

        Bundle bundle = new Bundle();
        bundle.putInt(StationListBaseFragment.STATION_NUMBER_MESSAGE, stationNumber);

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
            WindDirectionFragment windDirectionFragment = new WindDirectionFragment();
            windDirectionFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.winddir_frame_layout, windDirectionFragment);
        }
        if (stationData[3]) {
            WindLevelFragment windLevelFragment = new WindLevelFragment();
            windLevelFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.windlevel_frame_layout, windLevelFragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.station_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_choose_date) {
            DialogFragment fragment = new DatePickerDialogFragment();
            fragment.show(getSupportFragmentManager(), "datePicker");
        }

        return super.onOptionsItemSelected(item);
    }
}
