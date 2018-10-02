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

    public static final String MEASUREMENT_SYMBOL =
            "com.maciejkrolik.meteostats.ui.stationdetail.MEASUREMENT_SYMBOL";

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

        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();

        if (stationData[0]) {
            RainAndWaterFragment rainAndWaterFragment =
                    createRainAndWaterFragment(stationNumber, "rain");
            fragmentTransaction.replace(R.id.rain_frame_layout, rainAndWaterFragment);

        }
        if (stationData[1]) {
            RainAndWaterFragment rainAndWaterFragment =
                    createRainAndWaterFragment(stationNumber, "water");
            fragmentTransaction.replace(R.id.water_frame_layout, rainAndWaterFragment);

        }
        if (stationData[2]) {
            Bundle bundle = createBundle(stationNumber);
            WindDirectionFragment windDirectionFragment = new WindDirectionFragment();
            windDirectionFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.wind_direction_frame_layout, windDirectionFragment);

        }
        if (stationData[3]) {
            Bundle bundle = createBundle(stationNumber);
            WindLevelFragment windLevelFragment = new WindLevelFragment();
            windLevelFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.wind_level_frame_layout, windLevelFragment);
        }
        fragmentTransaction.commit();
    }

    private RainAndWaterFragment createRainAndWaterFragment(int stationNumber, String measurementSymbol) {
        Bundle bundle = createBundle(stationNumber);
        bundle.putString(MEASUREMENT_SYMBOL, measurementSymbol);
        RainAndWaterFragment rainAndWaterFragment = new RainAndWaterFragment();
        rainAndWaterFragment.setArguments(bundle);
        return rainAndWaterFragment;
    }

    private Bundle createBundle(int stationNumber) {
        Bundle bundle = new Bundle();
        bundle.putInt(StationListBaseFragment.STATION_NUMBER_MESSAGE, stationNumber);
        return bundle;
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
