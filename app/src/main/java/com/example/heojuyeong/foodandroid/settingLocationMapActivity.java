package com.example.heojuyeong.foodandroid;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.heojuyeong.foodandroid.common.CommonLocationApplication;

import com.example.heojuyeong.foodandroid.http.GeocodingService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import retrofit2.Call;


public class settingLocationMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private CommonLocationApplication commonLocationApplication;
    private TextView titleLocation;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapfragment);

        TextView cancleTextView = (TextView) findViewById(R.id.currentLocationSettingByMapCancle);
        titleLocation = (TextView) findViewById(R.id.currentLocationSettingByMapTitleLocation);
        TextView confirmTextview = (TextView) findViewById(R.id.currentLocationSettingByMapConfirmButton);
        commonLocationApplication = (CommonLocationApplication) getApplication();
        titleLocation.setText(commonLocationApplication.getLocationName());
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.currentLocationSettingByMapCancle:
                        finish();
                        break;
                    case R.id.currentLocationSettingByMapConfirmButton:
                        commonLocationApplication.setLocationName(titleLocation.getText().toString());
                        commonLocationApplication.setLatLng(latLng.latitude,latLng.longitude);
                        finish();
                        break;
                }
            }
        };

        cancleTextView.setOnClickListener(onClickListener);
        confirmTextview.setOnClickListener(onClickListener);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.currentLocationSettingByMap);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(35, 126), new LatLng(38, 128)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(getLatlng()));
        googleMap.setMinZoomPreference(15.0f);
        googleMap.setMaxZoomPreference(21.0f);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
        googleMap.getUiSettings().isMyLocationButtonEnabled();
        googleMap.getUiSettings().isZoomControlsEnabled();

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                latLng=googleMap.getCameraPosition().target;
                LocationAsyncTask asyncTask = new LocationAsyncTask();
                asyncTask.execute(latLng);
            }
        });


    }

    public LatLng getLatlng() {
        return new LatLng(commonLocationApplication.getLat(), commonLocationApplication.getLng());
    }

    private class LocationAsyncTask extends AsyncTask<LatLng, Void, String> {
        @Override
        protected String doInBackground(LatLng... params) {
            Call<GeocodingService> call=GeocodingService.Geolcation.getCall(params[0]);
            return GeocodingService.Geolcation.getLocationName(call);
        }

        @Override
        protected void onPostExecute(String locationName) {
            super.onPostExecute(locationName);
            titleLocation.setText(locationName);
        }
    }




}
