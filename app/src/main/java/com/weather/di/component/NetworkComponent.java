package com.weather.di.component;

import com.weather.di.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = NetworkModule.class)
public interface NetworkComponent {
    Retrofit getRetrofit();
}
