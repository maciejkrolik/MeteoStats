package com.maciejkrolik.meteostats.ui.stationdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maciejkrolik.meteostats.R;

public class WaterFragment extends Fragment {

    public WaterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rain_weather_data, container, false);
        TextView textView = rootView.findViewById(R.id.data_text_view);
        textView.setText("WaterFragment");
        return rootView;
    }
}
