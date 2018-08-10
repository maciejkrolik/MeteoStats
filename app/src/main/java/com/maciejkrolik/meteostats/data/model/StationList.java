package com.maciejkrolik.meteostats.data.model;

import java.util.List;

public class StationList {

    private String status;
    private String message;
    private List<Station> data;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Station> getData() {
        return data;
    }
}
