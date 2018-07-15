package com.weather.utils;


public final class AppConstants {

    private AppConstants() {
    }


    /**
     * Hold lat-lng precision value
     */
    public static final String LAT_LNG_PRECISION = "%.4f";

    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAILURE = 1;
    public static final String EXTRA_RESULT_MESSAGE = "result_message";
    public static final String EXTRA_RESULT_CODE = "result_code";
    public static final String EXTRA_ADDRESS_RESULT_RECEIVER = "com.weathercheck" + ".ADDRESS_RECEIVER";
    public static final String EXTRA_LAST_LOCATION = "last_location";
    public static final String ACTION_GET_SHORT_ADDRESS = "GET_SHORT_ADDRESS";
    public static final String TAG_DEV_LOCATION = "LOCATION";
}
