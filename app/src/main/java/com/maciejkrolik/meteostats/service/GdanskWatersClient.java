package com.maciejkrolik.meteostats.service;

import com.maciejkrolik.meteostats.model.StationList;
import com.maciejkrolik.meteostats.model.StationMeasurementList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GdanskWatersClient {

    @GET("/rest/stations")
    Call<StationList> listStations();

    @GET("/rest/measurments/{stationNumber}/{measurementSymbol}/{date}")
    Call<StationMeasurementList> getMeasurement(@Path("stationNumber") int stationNumber,
                                                @Path("measurementSymbol") String measurementSymbol,
                                                @Path("date") String date);
}
