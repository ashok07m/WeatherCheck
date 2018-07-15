package com.weather.ui.Search;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.weather.R;
import com.weather.data.network.model.CityWeatherResponse;
import com.weather.data.network.model.ListItemType;
import com.weather.ui.adapter.WeatherAdapter;
import com.weather.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements ISearchView {

    @Inject
    SearchPresenter<ISearchView, ISearchInteractor> mSearchPresenter;

    @Inject
    WeatherAdapter mWeatherAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;

    private final int forecastDaysCount = 14;

    @BindView(R.id.et_city)
    EditText mEtCity;
    @BindView(R.id.btn_search)
    Button mBtnSearch;
    @BindView(R.id.tv_output)
    TextView mTvOutput;
    @BindView(R.id.search_recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mSearchPresenter.onAttach(this);

        setUpView();
    }

    @OnClick(R.id.btn_search)
    public void onViewClicked() {
        if (isNetworkConnected()) {
            mWeatherAdapter.clearListItem();
            mSearchPresenter.onSearchClick(mEtCity.getText().toString(), forecastDaysCount);
        } else {
            onError(R.string.no_network);
        }
    }


    @Override
    protected void onDestroy() {
        mSearchPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void updateWeatherList(CityWeatherResponse response) {

        if (response != null) {
            List<ListItemType> itemList = new ArrayList<>();
            itemList.add(response.getCity());
            itemList.addAll(response.getList());

            mWeatherAdapter.addItems(itemList);
        }
    }


    /**
     * prepares recycler view
     */
    void setUpView() {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mWeatherAdapter);
    }
}
