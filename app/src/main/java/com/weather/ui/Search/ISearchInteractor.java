package com.weather.ui.Search;

import com.weather.data.network.model.CityWeatherResponse;
import com.weather.ui.base.IBaseInteractor;

import io.reactivex.Observable;

public interface ISearchInteractor extends IBaseInteractor {

    Observable<CityWeatherResponse> getDailyWeatherForecast(String city, int daysCount);
}
