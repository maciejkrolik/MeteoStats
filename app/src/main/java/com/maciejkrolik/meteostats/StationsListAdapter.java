package com.maciejkrolik.meteostats;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maciejkrolik.meteostats.model.Station;

import java.util.List;

public class StationsListAdapter extends RecyclerView.Adapter<StationViewHolder> {

    private final List<Station> stations;
    private final OnStationClickListener listener;

    public StationsListAdapter(final List<Station> stations, final OnStationClickListener listener) {
        this.stations = stations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_station_row, parent, false);

        return new StationViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        String rowText = stations.get(position).getName();
        holder.stationTextView.setText(rowText);
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }
}
