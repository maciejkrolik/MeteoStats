package com.maciejkrolik.meteostats.di;

import com.maciejkrolik.meteostats.data.repository.StationRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = NetworkModule.class)
public interface ApplicationComponent {

    StationRepository getStationRepository();
}
