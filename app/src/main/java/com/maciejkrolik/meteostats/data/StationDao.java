package com.maciejkrolik.meteostats.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.maciejkrolik.meteostats.data.model.Station;

import java.util.List;

@Dao
public interface StationDao {

    @Query("SELECT * FROM Station")
    LiveData<List<Station>> getSavedStations();

    @Query("SELECT * FROM Station WHERE `no` = :no")
    LiveData<Station> getSavedStationById(int no);

    @Insert
    Long saveStation(Station station);

    @Delete
    void deleteStation(Station station);
}
