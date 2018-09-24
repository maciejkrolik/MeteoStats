package com.maciejkrolik.meteostats.ui.about;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.maciejkrolik.meteostats.R;

public class AboutFragment extends Fragment {

    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem actionSort = menu.findItem(R.id.action_sort_dialog);
        actionSort.setVisible(false);
        MenuItem actionSearch = menu.findItem(R.id.action_search_list);
        actionSearch.setVisible(false);
    }
}
