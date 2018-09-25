package com.maciejkrolik.meteostats.ui.stationdetail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.maciejkrolik.meteostats.R;

class MeasurementsListViewHolder extends RecyclerView.ViewHolder {

    TextView valueTextView;
    TextView timeTextView;

    MeasurementsListViewHolder(View itemView) {
        super(itemView);
        valueTextView = itemView.findViewById(R.id.rain_value_text_view);
        timeTextView = itemView.findViewById(R.id.rain_time_text_view);
    }
}
