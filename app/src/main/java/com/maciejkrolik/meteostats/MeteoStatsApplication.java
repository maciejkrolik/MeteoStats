package com.maciejkrolik.meteostats;

import android.app.Application;

import com.maciejkrolik.meteostats.di.ApplicationComponent;
import com.maciejkrolik.meteostats.di.ContextModule;
import com.maciejkrolik.meteostats.di.DaggerApplicationComponent;

public class MeteoStatsApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent
                .builder()
                .contextModule(new ContextModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
