package com.maciejkrolik.meteostats.ui.stationlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.data.model.Station;
import com.maciejkrolik.meteostats.data.model.StationList;
import com.maciejkrolik.meteostats.data.service.GdanskWatersClient;
import com.maciejkrolik.meteostats.ui.stationdetail.StationDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StationListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnStationClickListener, DialogInterface.OnDismissListener {

    public static final String STATION_NAME_MESSAGE =
            "com.maciejkrolik.meteostats.ui.stationlist.STATION_NAME_MESSAGE";
    public static final String STATION_NUMBER_MESSAGE =
            "com.maciejkrolik.meteostats.ui.stationlist.STATION_NUMBER_MESSAGE";
    public static final String STATION_DATA =
            "com.maciejkrolik.meteostats.ui.stationlist.STATION_DATA";

    private RecyclerView.Adapter adapter;

    private List<Station> allStations = new ArrayList<>();
    private List<Station> visibleStations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);

        AllStationsListFragment allStationsListFragment = new AllStationsListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.content_station_list, allStationsListFragment)
                .commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_all_stations_list);

        RecyclerView recyclerView = findViewById(R.id.stations_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StationListAdapter(visibleStations, this);
        recyclerView.setAdapter(adapter);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://pomiary.gdanskiewody.pl")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        GdanskWatersClient client = retrofit.create(GdanskWatersClient.class);
        Call<StationList> call = client.listStations();

        call.enqueue(new Callback<StationList>() {
            @Override
            public void onResponse(Call<StationList> call, Response<StationList> response) {
                StationList stationList = response.body();

                if (stationList != null) {
                    allStations.addAll(stationList.getData());
                    setChosenStations();
                } else {
                    Toast.makeText(StationListActivity.this,
                            R.string.retrofit_data_error_message,
                            Toast.LENGTH_SHORT).show();
                }

                Log.d("TEST", "Downloaded data from the internet");
            }

            @Override
            public void onFailure(Call<StationList> call, Throwable t) {
                Toast.makeText(StationListActivity.this,
                        R.string.retrofit_error_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setChosenStations() {
        visibleStations.clear();
        visibleStations.addAll(getChosenStations(allStations));
        adapter.notifyDataSetChanged();
    }

    private List<Station> getChosenStations(List<Station> allStations) {
        List<Station> chosenStations = new ArrayList<>();

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getBoolean("show_rain", true)) {
            for (Station station : allStations) {
                if (station.isRain()) {
                    chosenStations.add(station);
                }
            }
        }
        if (sharedPreferences.getBoolean("show_water", true)) {
            for (Station station : allStations) {
                if (station.isWater()) {
                    chosenStations.add(station);
                }
            }
        }
        if (sharedPreferences.getBoolean("show_wind", true)) {
            for (Station station : allStations) {
                if (station.isWindlevel()) {
                    chosenStations.add(station);
                }
            }
        }
        return chosenStations;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.station_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_dialog) {
            showSortingDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSortingDialog() {
        DialogFragment fragment = new SortStationListDialogFragment();
        fragment.show(getSupportFragmentManager(), "sorting");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_favorite_stations) {

        } else if (id == R.id.nav_all_stations_list) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStationClick(int position) {
        String stationName = visibleStations.get(position).getName();
        int stationNumber = visibleStations.get(position).getNo();

        boolean[] availableStationData = {
                visibleStations.get(position).isRain(),
                visibleStations.get(position).isWater(),
                visibleStations.get(position).isWindlevel()
        };

        Intent intent = new Intent(this, StationDetailsActivity.class);
        intent.putExtra(STATION_NAME_MESSAGE, stationName);
        intent.putExtra(STATION_NUMBER_MESSAGE, stationNumber);
        intent.putExtra(STATION_DATA, availableStationData);
        startActivity(intent);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        setChosenStations();
    }
}
