package com.maciejkrolik.meteostats.model;

import java.util.List;
import java.util.Map;

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
