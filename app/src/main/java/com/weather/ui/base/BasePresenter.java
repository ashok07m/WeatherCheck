package com.weather.ui.base;

import com.weather.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * onAttach() and onDetach().
 */
public class BasePresenter<V extends IBaseView, I extends IBaseInteractor>
        implements IBasePresenter<V, I> {

    private static final String TAG = "BasePresenter";

    private final SchedulerProvider mSchedulerProvider;
    private final CompositeDisposable mCompositeDisposable;

    private V mView;
    private I mInteractor;

    @Inject
    public BasePresenter(I interactor,
                         SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        mInteractor = interactor;
        mSchedulerProvider = schedulerProvider;
        mCompositeDisposable = compositeDisposable;
    }

    @Override
    public void onAttach(V view) {
        mView = view;
    }

    @Override
    public void onDetach() {
        mCompositeDisposable.dispose();
        mView = null;
        mInteractor = null;
    }

    @Override
    public V getView() {
        return mView;
    }

    @Override
    public I getInteractor() {
        return mInteractor;
    }

    @Override
    public boolean isViewAttached() {
        return mView != null;
    }

    @Override
    public void checkViewAttached() throws ViewNotAttachedException {
        if (!isViewAttached()) throw new ViewNotAttachedException();
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public static class ViewNotAttachedException extends RuntimeException {
        public ViewNotAttachedException() {
            super("Call Presenter.onAttach(View) before" +
                    " requesting data to the Presenter");
        }
    }
}
