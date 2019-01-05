package com.maciejkrolik.meteostats.ui.stationlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.maciejkrolik.meteostats.R;

public class StationListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;

    private static final String ALL_STATIONS_FRAGMENT_TAG = "ALL_STATIONS_FRAGMENT_TAG";
    private static final String FAVORITE_STATIONS_FRAGMENT_TAG = "FAVORITE_STATIONS_FRAGMENT_TAG";
    private static final String ABOUT_FRAGMENT_TAG = "ABOUT_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            FavoriteStationsListFragment favoriteStationsListFragment = new FavoriteStationsListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_station_list, favoriteStationsListFragment, FAVORITE_STATIONS_FRAGMENT_TAG)
                    .commit();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_favorite_stations);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_favorite_stations) {
            Fragment fragment = fragmentManager.findFragmentByTag(FAVORITE_STATIONS_FRAGMENT_TAG);
            if (fragment == null) {
                FavoriteStationsListFragment favoriteStationsFragment = new FavoriteStationsListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_station_list, favoriteStationsFragment, FAVORITE_STATIONS_FRAGMENT_TAG)
                        .commit();
            }

        } else if (id == R.id.nav_all_stations_list) {
            Fragment fragment = fragmentManager.findFragmentByTag(ALL_STATIONS_FRAGMENT_TAG);
            if (fragment == null) {
                AllStationsListFragment allStationsFragment = new AllStationsListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_station_list, allStationsFragment, ALL_STATIONS_FRAGMENT_TAG)
                        .commit();
            }

        } else if (id == R.id.nav_about) {
            Fragment fragment = fragmentManager.findFragmentByTag(ABOUT_FRAGMENT_TAG);
            if (fragment == null) {
                AboutFragment aboutFragment = new AboutFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_station_list, aboutFragment, ABOUT_FRAGMENT_TAG)
                        .commit();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}
