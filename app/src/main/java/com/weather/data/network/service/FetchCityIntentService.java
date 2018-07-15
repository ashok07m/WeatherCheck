package com.weather.data.network.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.weather.utils.AppConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Handles requests asynchronously to get the lat/long location address
 */
public class FetchCityIntentService extends IntentService {
    /**
     * Hold class TAG
     */
    public final static String TAG = FetchCityIntentService.class.getName();

    /**
     * Hold Receiver instance
     */
    private ResultReceiver mResultReceiver;

    private final int MAX_RESULTS = 1;

    /**
     * public default constructor
     */
    public FetchCityIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (AppConstants.ACTION_GET_SHORT_ADDRESS.equals(action)) {

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());

                // initialise local receiver with activity receiver
                mResultReceiver = intent.getParcelableExtra(AppConstants.EXTRA_ADDRESS_RESULT_RECEIVER);

                //get location
                Location location = intent.getParcelableExtra(AppConstants.EXTRA_LAST_LOCATION);

                List<Address> addressList = null;

                try {
                    addressList = geocoder.getFromLocation(location.getLatitude(),
                            location.getLongitude(), MAX_RESULTS);
                } catch (IOException | IllegalArgumentException e) {
                    Log.e(TAG, "IntentServiceException :" + e.getMessage());
                }

                Address address = addressList.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();

                //AppLogger.debugLog(TAG, "onHandleIntent()", "Address ::" + addressList.get(0));
                if (AppConstants.ACTION_GET_SHORT_ADDRESS.equals(action)) {
                    String city = addressList.get(0).getLocality();
                    addressFragments.add(city);
                }
                String addressOutput = TextUtils.join(", ", addressFragments);
                // send result
                sendResultToAddressReceiver(AppConstants.RESULT_SUCCESS, addressOutput);

            }
        }
    }

    /**
     * Method to send the result to address receiver
     *
     * @param resultCode
     * @param addressOutput
     */
    private void sendResultToAddressReceiver(int resultCode, String addressOutput) {
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.EXTRA_RESULT_CODE, resultCode);
        bundle.putString(AppConstants.EXTRA_RESULT_MESSAGE, addressOutput);
        mResultReceiver.send(resultCode, bundle);
    }

}
