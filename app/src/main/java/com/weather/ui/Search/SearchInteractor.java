package com.weather.ui.Search;

import com.weather.data.network.IApiService;
import com.weather.data.network.model.CityWeatherResponse;
import com.weather.ui.base.BaseInteractor;

import javax.inject.Inject;

import io.reactivex.Observable;

public class SearchInteractor extends BaseInteractor implements ISearchInteractor {


    @Inject
    public SearchInteractor(IApiService apiService) {
        super(apiService);
    }

    @Override
    public Observable<CityWeatherResponse> getDailyWeatherForecast(String city, int daysCount) {
        return getApiService().getDailyWeatherForecast(city, daysCount);
    }

}
