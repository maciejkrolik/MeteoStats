package com.maciejkrolik.meteostats.ui.stationdetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maciejkrolik.meteostats.R;

import java.util.List;

public class MeasurementsListAdapter extends RecyclerView.Adapter<MeasurementsListViewHolder> {

    private final List<String> measurementValues;
    private final List<String> measurementTimes;

    MeasurementsListAdapter(final List<String> measurementValues, final List<String> measurementTimes) {
        this.measurementValues = measurementValues;
        this.measurementTimes = measurementTimes;
    }

    @NonNull
    @Override
    public MeasurementsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_station_measurement_row, parent, false);

        return new MeasurementsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeasurementsListViewHolder holder, int position) {
        String rowValue = measurementValues.get(position);
        String rowTime = measurementTimes.get(position);
        holder.valueTextView.setText(rowValue);
        holder.timeTextView.setText(rowTime);
    }

    @Override
    public int getItemCount() {
        return measurementValues.size();
    }
}
