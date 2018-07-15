package com.weather.data.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model class to map the weather api response
 */
public class CityWeatherResponse {

    @SerializedName("city")
    private City city;

    @SerializedName("cod")
    private String cod;

    @SerializedName("message")
    private String message;

    @SerializedName("cnt")
    private String cnt;

    @SerializedName("list")
    private List<DaysWeatherList> list;

    public City getCity() {
        return city;
    }

    public String getCod() {
        return cod;
    }

    public String getMessage() {
        return message;
    }

    public String getCnt() {
        return cnt;
    }

    public List<DaysWeatherList> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "CityWeatherResponse{" +
                "city=" + city +
                ", cod='" + cod + '\'' +
                ", message='" + message + '\'' +
                ", cnt='" + cnt + '\'' +
                ", list=" + list +
                '}';
    }

    public static class City implements ListItemType {

        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("coord")
        private Coordinates coord;

        @SerializedName("country")
        private String country;

        @SerializedName("population")
        private String population;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Coordinates getCoord() {
            return coord;
        }

        public String getCountry() {
            return country;
        }

        public String getPopulation() {
            return population;
        }

        @Override
        public String toString() {
            return "City{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", coord=" + coord +
                    ", country='" + country + '\'' +
                    ", population='" + population + '\'' +
                    '}';
        }

        @Override
        public int getItemViewType() {
            return ListItemType.VIEW_CITY;
        }

        public static class Coordinates {

            @SerializedName("lon")
            private String lon;

            @SerializedName("lat")
            private String lat;

            public String getLon() {
                return lon;
            }

            public String getLat() {
                return lat;
            }

            @Override
            public String toString() {
                return "Coordinates{" +
                        "lon='" + lon + '\'' +
                        ", lat='" + lat + '\'' +
                        '}';
            }
        }
    }

    public static class DaysWeatherList implements ListItemType {

        @SerializedName("dt")
        private String dt;

        @SerializedName("temp")
        private Temperature temp;

        @SerializedName("pressure")
        private String pressure;

        @SerializedName("humidity")
        private String humidity;

        @SerializedName("weather")
        private List<Weather> weather;

        @SerializedName("speed")
        private String speed;

        @SerializedName("deg")
        private String deg;

        @SerializedName("clouds")
        private String clouds;

        @SerializedName("rain")
        private String rain;

        public String getDt() {
            return dt;
        }

        public Temperature getTemp() {
            return temp;
        }

        public String getPressure() {
            return pressure;
        }

        public String getHumidity() {
            return humidity;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public String getSpeed() {
            return speed;
        }

        public String getDeg() {
            return deg;
        }

        public String getClouds() {
            return clouds;
        }

        public String getRain() {
            return rain;
        }

        @Override
        public String toString() {
            return "DaysWeatherList{" +
                    "dt='" + dt + '\'' +
                    ", temp=" + temp +
                    ", pressure='" + pressure + '\'' +
                    ", humidity='" + humidity + '\'' +
                    ", weather=" + weather +
                    ", speed='" + speed + '\'' +
                    ", deg='" + deg + '\'' +
                    ", clouds='" + clouds + '\'' +
                    ", rain='" + rain + '\'' +
                    '}';
        }

        @Override
        public int getItemViewType() {
            return ListItemType.VIEW_WEATHER;
        }

        public static class Temperature {

            @SerializedName("day")
            private String day;

            @SerializedName("min")
            private String min;

            @SerializedName("max")
            private String max;

            @SerializedName("night")
            private String night;

            @SerializedName("eve")
            private String eve;

            @SerializedName("morn")
            private String morn;

            public String getDay() {
                return day;
            }

            public String getMin() {
                return min;
            }

            public String getMax() {
                return max;
            }

            public String getNight() {
                return night;
            }

            public String getEve() {
                return eve;
            }

            public String getMorn() {
                return morn;
            }

            @Override
            public String toString() {
                return "Temperature{" +
                        "day='" + day + '\'' +
                        ", min='" + min + '\'' +
                        ", max='" + max + '\'' +
                        ", night='" + night + '\'' +
                        ", eve='" + eve + '\'' +
                        ", morn='" + morn + '\'' +
                        '}';
            }
        }


        public static class Weather {

            @SerializedName("id")
            private String id;

            @SerializedName("main")
            private String main;

            @SerializedName("description")
            private String description;

            @SerializedName("icon")
            private String icon;

            public String getId() {
                return id;
            }

            public String getMain() {
                return main;
            }

            public String getDescription() {
                return description;
            }

            public String getIcon() {
                return icon;
            }

            @Override
            public String toString() {
                return "Weather{" +
                        "id='" + id + '\'' +
                        ", main='" + main + '\'' +
                        ", description='" + description + '\'' +
                        ", icon='" + icon + '\'' +
                        '}';
            }
        }

    }


}
