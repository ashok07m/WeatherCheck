package com.weather.data.network;

import com.weather.data.network.model.CityWeatherResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IApiService {

    @GET(ApiEndPoint.ENDPOINT_WEATHER_FORECAST_DAILY)
    Observable<CityWeatherResponse> getDailyWeatherForecast(@Query("q") String city,
                                                            @Query("cnt") int daysCount);

    @GET(ApiEndPoint.ENDPOINT_WEATHER_FORECAST_DAILY)
    Observable<CityWeatherResponse> getDailyWeatherForecast(@Query("lat") double lat,
                                                            @Query("lon") double lon,
                                                            @Query("cnt") int daysCount);

}
