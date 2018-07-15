package com.weather.ui.main;

import com.weather.data.network.model.CityWeatherResponse;
import com.weather.ui.base.IBaseInteractor;

import io.reactivex.Observable;

public interface IMainInteractor extends IBaseInteractor {

    Observable<CityWeatherResponse> getDailyWeatherForecast(String city, int daysCount);
}
