package com.weather.ui.base;

/**
 * Every presenter in the app must either implement this interface or extend IBasePresenter
 * indicating the MvpView type that wants to be attached with.
 */
public interface IBasePresenter<V extends IBaseView, I extends IBaseInteractor> {

    void onAttach(V view);

    void onDetach();

    V getView();

    I getInteractor();

    boolean isViewAttached();

    void checkViewAttached() throws BasePresenter.ViewNotAttachedException;
}
