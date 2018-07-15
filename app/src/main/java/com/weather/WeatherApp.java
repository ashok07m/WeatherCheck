package com.weather;

import android.app.Application;

import com.weather.di.component.ApplicationComponent;
import com.weather.di.component.DaggerApplicationComponent;
import com.weather.di.module.ApplicationModule;

public class WeatherApp extends Application {

    private ApplicationComponent mApplicationComponent;

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        mApplicationComponent.inject(this);



    }
}
