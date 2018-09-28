package com.maciejkrolik.meteostats.di;

import com.maciejkrolik.meteostats.data.service.GdanskWatersClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
class NetworkModule {

    @Provides
    @Singleton
    GdanskWatersClient provideGdanskWatersClient(Retrofit retrofit) {
        return retrofit.create(GdanskWatersClient.class);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://pomiary.gdanskiewody.pl")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
