package com.maciejkrolik.meteostats.ui.stationlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maciejkrolik.meteostats.R;
import com.maciejkrolik.meteostats.data.model.Station;

import java.util.List;

public class StationListAdapter extends RecyclerView.Adapter<StationListViewHolder> {

    private final List<Station> stations;
    private final OnStationClickListener listener;

    StationListAdapter(final List<Station> stations, final OnStationClickListener listener) {
        this.stations = stations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_station_row, parent, false);

        return new StationListViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StationListViewHolder holder, int position) {
        String rowText = stations.get(position).getName();
        holder.stationTextView.setText(rowText);
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }
}
