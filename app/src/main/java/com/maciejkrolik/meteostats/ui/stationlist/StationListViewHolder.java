package com.maciejkrolik.meteostats.ui.stationlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.maciejkrolik.meteostats.R;

class StationListViewHolder extends RecyclerView.ViewHolder {

    TextView stationTextView;

    StationListViewHolder(final View itemView, final OnStationClickListener listener) {
        super(itemView);
        stationTextView = itemView.findViewById(R.id.station_text_view);
        stationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStationClick(getAdapterPosition());
            }
        });
    }
}
