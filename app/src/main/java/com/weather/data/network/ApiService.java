package com.weather.data.network;

import com.weather.data.network.model.CityWeatherResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import retrofit2.Retrofit;

@Singleton
public final class ApiService implements IApiService {

    private Retrofit mRetrofitClient;

    @Inject
    public ApiService(Retrofit retrofitClient) {
        mRetrofitClient = retrofitClient;
    }


    @Override
    public Observable<CityWeatherResponse> getDailyWeatherForecast(String city, int daysCount) {
        return mRetrofitClient.create(IApiService.class).getDailyWeatherForecast(city, daysCount);
    }

    @Override
    public Observable<CityWeatherResponse> getDailyWeatherForecast(double lat, double lon, int daysCount) {
        return mRetrofitClient.create(IApiService.class).getDailyWeatherForecast(lat, lon, daysCount);
    }


}
