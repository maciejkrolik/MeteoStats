package com.maciejkrolik.meteostats.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.maciejkrolik.meteostats.data.model.Station;

@Database(entities = {Station.class}, version = 1)
public abstract class StationDatabase extends RoomDatabase {

    public abstract StationDao stationDao();
}
