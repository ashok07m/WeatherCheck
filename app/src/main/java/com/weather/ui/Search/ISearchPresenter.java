package com.weather.ui.Search;

import com.weather.ui.base.IBasePresenter;

/**
 * Actions from View -> Presenter
 */
public interface ISearchPresenter<V extends ISearchView, I extends ISearchInteractor>
        extends IBasePresenter<V, I> {

    void onSearchClick(String cityName, int forecastCount);

}
