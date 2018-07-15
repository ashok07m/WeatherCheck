package com.weather.di.component;

import com.weather.di.PerActivity;
import com.weather.di.module.ActivityModule;
import com.weather.ui.Search.SearchActivity;
import com.weather.ui.main.MainActivity;

import dagger.Component;


@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(SearchActivity searchActivity);

}
