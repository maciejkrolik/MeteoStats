package com.maciejkrolik.meteostats.data.service;

import com.maciejkrolik.meteostats.data.model.StationList;
import com.maciejkrolik.meteostats.data.model.StationMeasurementsList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GdanskWatersClient {

    @GET("/rest/stations")
    Call<StationList> listStations();

    @GET("/rest/measurements/{stationNumber}/{measurementSymbol}/{date}")
    Call<StationMeasurementsList> getMeasurements(@Path("stationNumber") int stationNumber,
                                                  @Path("measurementSymbol") String measurementSymbol,
                                                  @Path("date") String date);
}
