package com.weather.ui.base;

import android.support.annotation.StringRes;

/**
 * Base interface that any class that wants to act as a View must implement.
 */
public interface IBaseView {

    void showLoading();

    void hideLoading();

    void onError(@StringRes int resId);

    void onError(String message);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    boolean isNetworkConnected();

}
