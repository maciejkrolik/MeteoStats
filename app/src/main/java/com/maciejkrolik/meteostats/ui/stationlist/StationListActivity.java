package com.maciejkrolik.meteostats.ui.stationlist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.maciejkrolik.meteostats.R;

public class StationListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DialogInterface.OnDismissListener {

    private FragmentManager fragmentManager;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);

        AllStationsListFragment allStationsListFragment = new AllStationsListFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.content_station_list, allStationsListFragment, "station_list_fragment")
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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.station_list, menu);
        MenuItem item = menu.findItem(R.id.action_search_list);
        searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                StationListBaseFragment fragment = (StationListBaseFragment) fragmentManager
                        .findFragmentByTag("station_list_fragment");
                fragment.searchStationList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                StationListBaseFragment fragment = (StationListBaseFragment) fragmentManager
                        .findFragmentByTag("station_list_fragment");
                fragment.searchStationList(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_dialog) {
            showFilterDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        DialogFragment fragment = new FilterStationsDialogFragment();
        fragment.show(fragmentManager, "sorting_dialog_fragment");
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        StationListBaseFragment fragment = (StationListBaseFragment) fragmentManager
                .findFragmentByTag("station_list_fragment");
        fragment.setupChosenStations();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_favorite_stations) {
            FavoriteStationsListFragment fragment = new FavoriteStationsListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_station_list, fragment, "station_list_fragment")
                    .commit();

        } else if (id == R.id.nav_all_stations_list) {
            AllStationsListFragment fragment = new AllStationsListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_station_list, fragment, "station_list_fragment")
                    .commit();

        } else if (id == R.id.nav_about) {
            AboutFragment fragment = new AboutFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_station_list, fragment)
                    .commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
