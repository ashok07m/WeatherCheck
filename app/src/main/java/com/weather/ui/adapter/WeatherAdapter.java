package com.weather.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weather.BuildConfig;
import com.weather.R;
import com.weather.data.network.model.CityWeatherResponse;
import com.weather.data.network.model.ListItemType;
import com.weather.ui.base.BaseViewHolder;
import com.weather.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherAdapter extends RecyclerView.Adapter<BaseViewHolder> {


    private List<ListItemType> mItemList;

    public WeatherAdapter(List<ListItemType> weatherList) {
        mItemList = weatherList;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case ListItemType.VIEW_CITY:
                return new CityViewHolder(
                        LayoutInflater.from(parent.getContext()).
                                inflate(R.layout.view_list_item_city, parent, false));

            case ListItemType.VIEW_WEATHER:
                return new WeatherViewHolder(
                        LayoutInflater.from(parent.getContext()).
                                inflate(R.layout.view_list_item_weather, parent, false));
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).
                                inflate(R.layout.view_list_item_empty, parent, false));

        }

    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position).getItemViewType();
    }


    public void addItems(List<ListItemType> itemList) {
        mItemList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void clearListItem() {
        mItemList.clear();
    }

    public class WeatherViewHolder extends BaseViewHolder {

        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_humidity)
        TextView tvHumidity;
        @BindView(R.id.tv_current_temp)
        TextView tvCurrentTemp;
        @BindView(R.id.tv_min_temp)
        TextView tvMinTemp;
        @BindView(R.id.tv_max_temp)
        TextView tvMaxTemp;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
            imgIcon.setImageDrawable(null);
            tvDescription.setText("");
            tvDate.setText("");
            tvHumidity.setText("");
            tvMinTemp.setText("");
            tvMaxTemp.setText("");
            tvCurrentTemp.setText("");
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            String tempUnit = imgIcon.getContext().getResources().getString(R.string.unit_celsius);

            CityWeatherResponse.DaysWeatherList daysWeatherList =
                    (CityWeatherResponse.DaysWeatherList) mItemList.get(position);
            if (daysWeatherList != null) {

                tvDate.setText(AppUtils.getDayFromDate(daysWeatherList.getDt()));

                if (daysWeatherList.getTemp() != null) {
                    int temp = (int) Double.parseDouble(daysWeatherList.getTemp().getDay());
                    tvCurrentTemp.setText(temp + " " + tempUnit);
                    temp = (int) Double.parseDouble(daysWeatherList.getTemp().getMin());
                    tvMinTemp.setText(temp + " " + tempUnit);
                    temp = (int) Double.parseDouble(daysWeatherList.getTemp().getMax());
                    tvMaxTemp.setText(temp + " " + tempUnit);
                }

                tvHumidity.setText(daysWeatherList.getHumidity());

                if (daysWeatherList.getWeather() != null) {

                    tvDescription.setText(daysWeatherList.getWeather().get(0).getDescription());

                    String iconId = daysWeatherList.getWeather().get(0).getIcon();
                    String imageUrl = BuildConfig.IMG_URL + iconId + ".png";
                    Glide.with(imgIcon.getContext())
                            .load(imageUrl)
                            .into(imgIcon);
                }
            }

        }
    }


    public class CityViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_city)
        TextView tvCity;

        public CityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
            tvCity.setText("");
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            CityWeatherResponse.City city = (CityWeatherResponse.City) mItemList.get(position);
            if (city != null) {
                tvCity.setText(city.getName() + ", " + city.getCountry());
            }
        }
    }

    public class EmptyViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_empty)
        TextView tvEmpty;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
        }
    }

}
