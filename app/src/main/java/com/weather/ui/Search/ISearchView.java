package com.weather.ui.Search;

import com.weather.data.network.model.CityWeatherResponse;
import com.weather.ui.base.IBaseView;

public interface ISearchView extends IBaseView {

    void updateWeatherList(CityWeatherResponse response);

}
