package com.weather.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.weather.data.network.model.ListItemType;
import com.weather.di.ActivityContext;
import com.weather.di.PerActivity;
import com.weather.ui.Search.ISearchInteractor;
import com.weather.ui.Search.ISearchPresenter;
import com.weather.ui.Search.ISearchView;
import com.weather.ui.Search.SearchInteractor;
import com.weather.ui.Search.SearchPresenter;
import com.weather.ui.adapter.WeatherAdapter;
import com.weather.ui.main.IMainInteractor;
import com.weather.ui.main.IMainPresenter;
import com.weather.ui.main.IMainView;
import com.weather.ui.main.MainInteractor;
import com.weather.ui.main.MainPresenter;
import com.weather.utils.rx.AppSchedulerProvider;
import com.weather.utils.rx.SchedulerProvider;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class ActivityModule {

    private AppCompatActivity mAppCompatActivity;

    public ActivityModule(AppCompatActivity appCompatActivity) {
        mAppCompatActivity = appCompatActivity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mAppCompatActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mAppCompatActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @PerActivity
    ISearchPresenter<ISearchView, ISearchInteractor> provideSearchPresenter(
            SearchPresenter<ISearchView, ISearchInteractor> searchPresenter) {
        return searchPresenter;
    }

    @Provides
    @PerActivity
    ISearchInteractor provideSearchInteractor(SearchInteractor searchInteractor) {
        return searchInteractor;
    }


    @Provides
    @PerActivity
    IMainPresenter<IMainView, IMainInteractor> provideMainPresenter(
            MainPresenter<IMainView, IMainInteractor> mainPresenter) {
        return mainPresenter;
    }

    @Provides
    @PerActivity
    IMainInteractor provideMainInteractor(MainInteractor mainInteractor) {
        return mainInteractor;
    }

    @Provides
    WeatherAdapter provideWeatherAdapter() {
        return new WeatherAdapter(new ArrayList<ListItemType>());
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity);
    }


}
