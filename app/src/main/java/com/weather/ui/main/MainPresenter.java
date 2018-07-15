package com.weather.ui.main;

import android.util.Log;

import com.weather.R;
import com.weather.data.network.model.CityWeatherResponse;
import com.weather.ui.base.BasePresenter;
import com.weather.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MainPresenter<V extends IMainView, I extends IMainInteractor>
        extends BasePresenter<V, I> implements IMainPresenter<V, I> {

    private static final String TAG = MainPresenter.class.getSimpleName();


    @Inject
    public MainPresenter(I interactor, SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        super(interactor, schedulerProvider, compositeDisposable);

    }

    @Override
    public void searchWeather(String city, int forecastCount) {

        //validate city name
        if (city == null || city.isEmpty()) {
            getView().onError(R.string.empty_city_name);
            return;
        }

        // validate count
        if (forecastCount <= 0) {
            getView().onError(R.string.invalid_days_count);
            return;
        }

        getView().showLoading();

        getCompositeDisposable().add(getInteractor()
                .getDailyWeatherForecast(city, forecastCount)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<CityWeatherResponse>() {
                    @Override
                    public void accept(CityWeatherResponse response) throws Exception {

                        Log.d("TAG", "CityWeatherResponse : " + response.toString());

                        if (!isViewAttached()) {
                            return;
                        }

                        getView().hideLoading();
                        getView().updateWeatherList(response);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        Log.d("TAG", "Exception : " + throwable.getLocalizedMessage());

                        if (!isViewAttached()) {
                            return;
                        }

                        getView().hideLoading();
                    }
                }));

    }


}
