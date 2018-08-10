package com.maciejkrolik.meteostats.data.model;

import java.util.List;

public class StationMeasurementList {

    private String status;
    private String message;
    private List<List<String>> data;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<List<String>> getData() {
        return data;
    }
}
