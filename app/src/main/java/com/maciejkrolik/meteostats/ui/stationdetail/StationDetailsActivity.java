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
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maciejkrolik.meteostats.MeteoStatsApplication;
import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.data.StationRepository;
import com.maciejkrolik.meteostats.data.model.Station;
import com.maciejkrolik.meteostats.ui.stationlist.StationListBaseFragment;
import com.maciejkrolik.meteostats.util.NetworkUtils;
import com.maciejkrolik.meteostats.util.StringUtils;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class StationDetailsActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_MEASUREMENT_SYMBOL =
            "com.maciejkrolik.meteostats.ui.stationdetail.EXTRA_MEASUREMENT_SYMBOL";
    public static final String EXTRA_DATE =
            "com.maciejkrolik.meteostats.ui.stationdetail.EXTRA_DATE";
    public static final String EXTRA_STATION_NUMBER =
            "com.maciejkrolik.meteostats.ui.stationdetail.EXTRA_STATION_NUMBER";

    private static final String RAIN_FRAGMENT_TAG = "RAIN_FRAGMENT_TAG";
    private static final String WATER_FRAGMENT_TAG = "WATER_FRAGMENT_TAG";
    private static final String WIND_DIRECTION_FRAGMENT_TAG = "WIND_DIRECTION_FRAGMENT_TAG";
    private static final String WIND_LEVEL_FRAGMENT_TAG = "WIND_LEVEL_FRAGMENT_TAG";

    private StationRepository stationRepository;
    private Station station;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);

        View connectivityLayout = findViewById(R.id.connectivity_layout);
        ProgressBar connectivityProgressBar = findViewById(R.id.connectivity_progress_bar);
        TextView connectivityTextView = findViewById(R.id.connectivity_text_view);

        Intent intent = getIntent();
        station = intent.getParcelableExtra(StationListBaseFragment.EXTRA_STATION);
        final int stationNumber = station.getNo();
        date = intent.getStringExtra(EXTRA_DATE) != null ? intent.getStringExtra(EXTRA_DATE) : StringUtils.getTodayDateAsString();

        setTitle(station.getName());

        stationRepository = ((MeteoStatsApplication) getApplication())
                .getApplicationComponent()
                .getStationRepository();

        if (NetworkUtils.isConnected(this)) {
            new CheckFavoriteTask(this).execute(stationNumber);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (station.hasRain()) {
                Fragment rainFragment = fragmentManager.findFragmentByTag(RAIN_FRAGMENT_TAG);
                if (rainFragment == null) {
                    RainAndWaterFragment rainAndWaterFragment =
                            createRainAndWaterFragment(stationNumber, "rain");
                    fragmentTransaction.replace(R.id.rain_frame_layout, rainAndWaterFragment, RAIN_FRAGMENT_TAG);
                }
            }
            if (station.hasWater()) {
                Fragment waterFragment = fragmentManager.findFragmentByTag(WATER_FRAGMENT_TAG);
                if (waterFragment == null) {
                    RainAndWaterFragment rainAndWaterFragment =
                            createRainAndWaterFragment(stationNumber, "water");
                    fragmentTransaction.replace(R.id.water_frame_layout, rainAndWaterFragment, WATER_FRAGMENT_TAG);
                }
            }
            if (station.hasWindDir()) {
                Fragment fragment = fragmentManager.findFragmentByTag(WIND_DIRECTION_FRAGMENT_TAG);
                if (fragment == null) {
                    Bundle bundle = createBundle(stationNumber);
                    WindDirectionFragment windDirectionFragment = new WindDirectionFragment();
                    windDirectionFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.wind_direction_frame_layout, windDirectionFragment, WIND_DIRECTION_FRAGMENT_TAG);
                }
            }
            if (station.hasWindLevel()) {
                Fragment fragment = fragmentManager.findFragmentByTag(WIND_LEVEL_FRAGMENT_TAG);
                if (fragment == null) {
                    Bundle bundle = createBundle(stationNumber);
                    WindLevelFragment windLevelFragment = new WindLevelFragment();
                    windLevelFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.wind_level_frame_layout, windLevelFragment, WIND_LEVEL_FRAGMENT_TAG);
                }
            }
            fragmentTransaction.commit();

        } else {
            connectivityProgressBar.setVisibility(View.GONE);
            connectivityLayout.setVisibility(View.VISIBLE);
            connectivityTextView.setVisibility(View.VISIBLE);
        }
    }

    private RainAndWaterFragment createRainAndWaterFragment(int stationNumber, String measurementSymbol) {
        Bundle bundle = createBundle(stationNumber);
        bundle.putString(EXTRA_MEASUREMENT_SYMBOL, measurementSymbol);
        RainAndWaterFragment rainAndWaterFragment = new RainAndWaterFragment();
        rainAndWaterFragment.setArguments(bundle);
        return rainAndWaterFragment;
    }

    private Bundle createBundle(int stationNumber) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_STATION_NUMBER, stationNumber);
        bundle.putString(EXTRA_DATE, date);
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
                new ManageFavoriteTask(this).execute(station);
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
        intent.putExtra(EXTRA_DATE, chosenDate);
        startActivity(intent);
        finish();
    }

    private static class CheckFavoriteTask extends AsyncTask<Integer, Void, Boolean> {

        private final WeakReference<StationDetailsActivity> activityWeakReference;

        CheckFavoriteTask(StationDetailsActivity context) {
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Boolean doInBackground(Integer... ints) {
            int stationNumber = ints[0];

            StationDetailsActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return false;
            } else {
                return activity.stationRepository.getStationByNumber(stationNumber) != null;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            activityWeakReference.get().station.setSaved(result);
        }
    }

    private static class ManageFavoriteTask extends AsyncTask<Station, Void, Void> {

        private final WeakReference<StationDetailsActivity> activityWeakReference;

        ManageFavoriteTask(StationDetailsActivity context) {
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Station... stations) {
            Station station = stations[0];

            StationDetailsActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            } else {
                if (station.isSaved()) {
                    activity.stationRepository.saveStation(station);
                } else {
                    activity.stationRepository.deleteStation(station);
                }
            }
            return null;
        }
    }
}
