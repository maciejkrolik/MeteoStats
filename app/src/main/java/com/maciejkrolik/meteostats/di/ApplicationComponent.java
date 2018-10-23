package com.maciejkrolik.meteostats.di;

import com.maciejkrolik.meteostats.data.StationRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class, DatabaseModule.class})
public interface ApplicationComponent {

    StationRepository getStationRepository();
}
