package com.maciejkrolik.meteostats.ui.stationlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maciejkrolik.meteostats.R;

public class AboutFragment extends Fragment {

    public AboutFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() != null) {
            getActivity().setTitle(R.string.nav_about);
        }

        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}
