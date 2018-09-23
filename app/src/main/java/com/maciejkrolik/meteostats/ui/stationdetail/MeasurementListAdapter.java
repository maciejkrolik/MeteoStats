package com.maciejkrolik.meteostats.ui.stationdetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maciejkrolik.meteostats.R;

import java.util.List;

public class MeasurementListAdapter extends RecyclerView.Adapter<MeasurementListViewHolder> {

    private final List<String> measurementValues;
    private final List<String> measurementTimes;

    MeasurementListAdapter(final List<String> measurementValues, final List<String> measurementTimes) {
        this.measurementValues = measurementValues;
        this.measurementTimes = measurementTimes;
    }

    @NonNull
    @Override
    public MeasurementListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_station_measurement_row, parent, false);

        return new MeasurementListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeasurementListViewHolder holder, int position) {
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
