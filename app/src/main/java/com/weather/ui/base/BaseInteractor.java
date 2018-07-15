package com.weather.ui.base;

import com.weather.data.network.IApiService;

import javax.inject.Inject;

public class BaseInteractor implements IBaseInteractor {

    private final IApiService mApiService;

    @Inject
    public BaseInteractor(IApiService apiService) {
        mApiService = apiService;
    }


    @Override
    public IApiService getApiService() {
        return mApiService;
    }

}
