package com.example.fooddeuk.activity;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.example.fooddeuk.R;
import com.example.fooddeuk.object.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class LocationSettingByMapActivity extends BaseActivity implements OnMapReadyCallback {
    @BindView(R.id.currentLocationSettingByMapTitleLocation)
    TextView titleLocation;
    @BindView(R.id.currentLocationSettingByMapCancel)
    TextView cancelTextView;
    @BindView(R.id.currentLocationSettingByMapConfirmButton)
    TextView confirmTextView;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_location_map);
        ButterKnife.bind(this);
        View.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.currentLocationSettingByMapCancel:
                    break;
                case R.id.currentLocationSettingByMapConfirmButton:
                    Location.INSTANCE.setLat(latLng.latitude);
                    Location.INSTANCE.setLng(latLng.longitude);
                    Location.INSTANCE.setLocationName(titleLocation.getText().toString());
                    break;
            }
            finish();
        };
        cancelTextView.setOnClickListener(onClickListener);
        confirmTextView.setOnClickListener(onClickListener);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.currentLocationSettingByMap);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(35, 126), new LatLng(38, 128)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Location.INSTANCE.getLat(),Location.INSTANCE.getLng())));
        googleMap.setMinZoomPreference(12.0f);
        googleMap.setMaxZoomPreference(21.0f);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
        googleMap.getUiSettings().isMyLocationButtonEnabled();
        googleMap.getUiSettings().isZoomControlsEnabled();
        googleMap.setOnCameraIdleListener(() -> {
            latLng=googleMap.getCameraPosition().target;
            Location.INSTANCE.getLocationName(latLng.latitude, latLng.longitude, new Function1<String, Unit>() {
                @Override
                public Unit invoke(String s) {
                    titleLocation.setText(s);
                    return null;
                }
            });
        });
    }







}
