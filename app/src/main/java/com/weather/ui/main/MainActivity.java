package com.weather.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.weather.BuildConfig;
import com.weather.R;
import com.weather.data.network.model.CityWeatherResponse;
import com.weather.data.network.model.ListItemType;
import com.weather.data.network.service.FetchCityIntentService;
import com.weather.ui.Search.SearchActivity;
import com.weather.ui.adapter.WeatherAdapter;
import com.weather.ui.base.BaseActivity;
import com.weather.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements IMainView {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    MainPresenter<IMainView, IMainInteractor> mMainPresenter;

    @Inject
    WeatherAdapter mWeatherAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;

    private final int forecastDaysCount = 14;
    private static final int REQUEST_CHECK_SETTINGS = 100;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;


    @BindView(R.id.search_recycler_view)
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToSearchPage();
            }
        });


        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mMainPresenter.onAttach(this);

        setUpView();

        init();
    }

    @Override
    public void updateWeatherList(CityWeatherResponse response) {

        if (response != null) {
            mWeatherAdapter.clearListItem();

            List<ListItemType> itemList = new ArrayList<>();
            itemList.add(response.getCity());
            itemList.addAll(response.getList());

            mWeatherAdapter.addItems(itemList);
        }
    }


    public void onLocationSettingsError(Exception e) {
        int statusCode = ((ApiException) e).getStatusCode();
        switch (statusCode) {
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: {
                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                        "location settings ");
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the
                    // result in onActivityResult().
                    ResolvableApiException rae = (ResolvableApiException) e;
                    rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sie) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            }
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: {
                String errorMessage = "Location settings are inadequate, and cannot be " +
                        "fixed here. Fix in Settings.";
                Log.e(TAG, errorMessage);

                onError(errorMessage);
                break;
            }
        }

    }

    public void onLocationSearched(double lat, double lon) {
        fetchAddressFromLatLng(lat, lon);
        stopLocationUpdates();
    }

    /**
     * Starts this service to perform action getAddress with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @param latitude
     * @param longitude
     */
    public void fetchAddressFromLatLng(double latitude, double longitude) {

        AddressResultReceiver mAddressResultReceiver = new AddressResultReceiver(null);

        Location location = new Location(AppConstants.TAG_DEV_LOCATION);
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        Intent intent = new Intent(this, FetchCityIntentService.class);
        intent.setAction(AppConstants.ACTION_GET_SHORT_ADDRESS);
        intent.putExtra(AppConstants.EXTRA_ADDRESS_RESULT_RECEIVER, mAddressResultReceiver);
        intent.putExtra(AppConstants.EXTRA_LAST_LOCATION, location);
        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            startLocationSearch();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        onError(R.string.permission_denied);
                        break;
                }
                break;
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


    public void startLocationSearch() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(
                            PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void moveToSearchPage() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }


    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                if (mCurrentLocation != null) {
                    onLocationSearched(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                } else {
                    onError(R.string.location_not_found);
                }
            }
        };

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(MainActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                    }
                })
                .addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onLocationSettingsError(e);
                    }
                });
    }

    private void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }


    /**
     * Class to receive the address service result
     */
    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            // show the address output
            final String city = resultData.getString(AppConstants.EXTRA_RESULT_MESSAGE);
            Log.d(TAG, "city :" + city);

            if (isNetworkConnected()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMainPresenter.searchWeather(city, forecastDaysCount);
                    }
                });

            } else {
                onError(R.string.no_network);
            }

        }
    }


}
