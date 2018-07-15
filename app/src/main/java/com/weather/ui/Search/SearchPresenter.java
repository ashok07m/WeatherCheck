package com.weather.ui.Search;

import android.util.Log;

import com.weather.R;
import com.weather.data.network.model.CityWeatherResponse;
import com.weather.ui.base.BasePresenter;
import com.weather.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class SearchPresenter<V extends ISearchView, I extends ISearchInteractor>
        extends BasePresenter<V, I> implements ISearchPresenter<V, I> {

    @Inject
    public SearchPresenter(I interactor, SchedulerProvider schedulerProvider,
                           CompositeDisposable compositeDisposable) {
        super(interactor, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onSearchClick(String cityName, int forecastCount) {

        //validate city name
        if (cityName == null || cityName.isEmpty()) {
            getView().onError(R.string.empty_city_name);
            return;
        }

        // validate count
        if (forecastCount <= 0) {
            getView().onError(R.string.invalid_days_count);
            return;
        }

        String[] cityNames = cityName.split(",");


        getView().showLoading();

        for (int i = 0; i < cityNames.length; i++) {

            getCompositeDisposable().add(getInteractor()
                    .getDailyWeatherForecast(cityNames[i], forecastCount)
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

}
