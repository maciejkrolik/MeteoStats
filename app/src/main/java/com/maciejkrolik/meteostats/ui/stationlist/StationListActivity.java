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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.ui.about.AboutFragment;

public class StationListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DialogInterface.OnDismissListener {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);

        AllStationsListFragment allStationsListFragment = new AllStationsListFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.content_station_list, allStationsListFragment, "allStationsFragment")
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
        fragment.show(fragmentManager, "sortingDialogFragment");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_favorite_stations) {


        } else if (id == R.id.nav_all_stations_list) {
            AllStationsListFragment fragment = new AllStationsListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_station_list, fragment)
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

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        AllStationsListFragment fragment = (AllStationsListFragment) fragmentManager
                .findFragmentByTag("allStationsFragment");
        fragment.setChosenStations();
    }
}
