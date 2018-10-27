package com.maciejkrolik.meteostats.ui.stationdetail;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.maciejkrolik.meteostats.MeteoStatsApplication;
import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.data.StationRepository;
import com.maciejkrolik.meteostats.data.model.Station;
import com.maciejkrolik.meteostats.ui.stationlist.StationListBaseFragment;
import com.maciejkrolik.meteostats.util.StringUtils;

import java.util.Locale;

public class StationDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String MEASUREMENT_SYMBOL =
            "com.maciejkrolik.meteostats.ui.stationdetail.MEASUREMENT_SYMBOL";
    public static final String DATE =
            "com.maciejkrolik.meteostats.ui.stationdetail.DATE";
    public static final String STATION_NUMBER_MESSAGE =
            "com.maciejkrolik.meteostats.ui.stationdetail.STATION_NUMBER_MESSAGE";

    private StationRepository stationRepository;
    private Station station;

    private static final String rainFragmentTag = "rain_fragment";
    private static final String waterFragmentTag = "water_fragment";
    private static final String windDirectionFragmentTag = "wind_direction_fragment";
    private static final String windLevelFragmentTag = "wind_level_fragment";

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);

        Intent intent = getIntent();
        station = intent.getParcelableExtra(StationListBaseFragment.STATION_MESSAGE);
        final int stationNumber = station.getNo();
        date = intent.getStringExtra(DATE) != null ? intent.getStringExtra(DATE) : StringUtils.getTodayDateAsString();

        setTitle(station.getName());

        stationRepository = ((MeteoStatsApplication) getApplication())
                .getApplicationComponent()
                .getStationRepository();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (station.isRain()) {
            Fragment rainFragment = fragmentManager.findFragmentByTag(rainFragmentTag);
            if (rainFragment == null) {
                RainAndWaterFragment rainAndWaterFragment =
                        createRainAndWaterFragment(stationNumber, "rain");
                fragmentTransaction.replace(R.id.rain_frame_layout, rainAndWaterFragment, rainFragmentTag);
            }
        }
        if (station.isWater()) {
            Fragment waterFragment = fragmentManager.findFragmentByTag(waterFragmentTag);
            if (waterFragment == null) {
                RainAndWaterFragment rainAndWaterFragment =
                        createRainAndWaterFragment(stationNumber, "water");
                fragmentTransaction.replace(R.id.water_frame_layout, rainAndWaterFragment, waterFragmentTag);
            }
        }
        if (station.isWinddir()) {
            Fragment fragment = fragmentManager.findFragmentByTag(windDirectionFragmentTag);
            if (fragment == null) {
                Bundle bundle = createBundle(stationNumber);
                WindDirectionFragment windDirectionFragment = new WindDirectionFragment();
                windDirectionFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.wind_direction_frame_layout, windDirectionFragment, windDirectionFragmentTag);
            }
        }
        if (station.isWindlevel()) {
            Fragment fragment = fragmentManager.findFragmentByTag(windLevelFragmentTag);
            if (fragment == null) {
                Bundle bundle = createBundle(stationNumber);
                WindLevelFragment windLevelFragment = new WindLevelFragment();
                windLevelFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.wind_level_frame_layout, windLevelFragment, windLevelFragmentTag);
            }
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
        bundle.putInt(STATION_NUMBER_MESSAGE, stationNumber);
        bundle.putString(DATE, date);
        return bundle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.station_detail, menu);
        if (station.isSaved()) {
            menu.findItem(R.id.action_add_favorite).setIcon(R.drawable.ic_nav_favorite_white);
        } else {
            menu.findItem(R.id.action_add_favorite).setIcon(R.drawable.ic_nav_favorite_white_border);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_choose_date:
                DialogFragment fragment = new DatePickerDialogFragment();
                fragment.show(getSupportFragmentManager(), "datePicker");
                return true;

            case R.id.action_add_favorite:
                if (!station.isSaved()) {
                    item.setIcon(R.drawable.ic_nav_favorite_white);
                    station.setSaved(true);
                } else {
                    item.setIcon(R.drawable.ic_nav_favorite_white_border);
                    station.setSaved(false);
                }
                new ManageFavoriteTask().execute(station);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String chosenDate = year + "-"
                + String.format(Locale.US, "%02d", month + 1) + "-"
                + String.format(Locale.US, "%02d", dayOfMonth);

        Intent intent = getIntent();
        intent.putExtra(DATE, chosenDate);
        startActivity(intent);
        finish();
    }

    private class ManageFavoriteTask extends AsyncTask<Station, Void, Void> {

        @Override
        protected Void doInBackground(Station... stations) {
            if (stations[0].isSaved()) {
                stationRepository.saveStation(stations[0]);
            } else {
                stationRepository.deleteStation(stations[0]);
            }
            return null;
        }
    }
}
