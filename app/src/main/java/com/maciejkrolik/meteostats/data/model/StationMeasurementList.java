package com.maciejkrolik.meteostats.data.model;

import java.util.LinkedHashMap;

public class StationMeasurementList {

    private String status;
    private String message;
    private LinkedHashMap<String, Float> data;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LinkedHashMap<String, Float> getData() {
        return data;
    }
}
