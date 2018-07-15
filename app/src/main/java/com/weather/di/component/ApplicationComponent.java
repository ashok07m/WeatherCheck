package com.weather.di.component;


import android.app.Application;
import android.content.Context;

import com.weather.WeatherApp;
import com.weather.data.network.IApiService;
import com.weather.di.ApplicationContext;
import com.weather.di.module.ApplicationModule;
import com.weather.di.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(WeatherApp weatherApp);

    @ApplicationContext
    Context context();

    Application application();

    IApiService apiService();
}
