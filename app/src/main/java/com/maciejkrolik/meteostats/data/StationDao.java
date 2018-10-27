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
    LiveData<List<Station>> getFavoriteStations();

    @Insert
    Long addToFavoriteStations(Station station);

    @Delete
    void deleteFromFavoriteStations(Station station);
}
