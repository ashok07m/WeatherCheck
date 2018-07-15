package com.weather.ui.main;

import com.weather.ui.base.IBasePresenter;

/**
 * Actions from View -> Presenter
 */
public interface IMainPresenter<V extends IMainView, I extends IMainInteractor>
        extends IBasePresenter<V, I> {

    void searchWeather(String city, int forecastCount);


}
