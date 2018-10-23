package com.maciejkrolik.meteostats.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.maciejkrolik.meteostats.data.StationDao;
import com.maciejkrolik.meteostats.data.StationDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
class DatabaseModule {

    @Provides
    @Singleton
    StationDao provideStationDao(StationDatabase database) {
        return database.stationDao();
    }

    @Provides
    @Singleton
    StationDatabase provideStationDatabase(Context context) {
        return Room.databaseBuilder(context,
                StationDatabase.class,
                "StationDatabase.db")
                .build();
    }
}
