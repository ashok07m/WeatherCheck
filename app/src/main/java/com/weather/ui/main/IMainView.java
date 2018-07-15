package com.weather.ui.main;

import com.weather.data.network.model.CityWeatherResponse;
import com.weather.ui.base.IBaseView;

public interface IMainView extends IBaseView {

    void updateWeatherList(CityWeatherResponse response);

}
