package com.example.fooddeuk.map


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.activity.BaseActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.android.synthetic.main.activity_setting_location_map.*


class LocationSettingByMapActivity : BaseActivity(), OnMapReadyCallback {
    lateinit var latLng: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_location_map)
        val onClickListener = { v : View ->
            when (v.id) {
                R.id.back -> {
                    finish()
                }
                R.id.confirm-> {
                    Location.lat = latLng.latitude
                    Location.lng = latLng.longitude
                    Location.locationName = location_title.text.toString()
                }
            }
            finish()
        }
        back.setOnClickListener(onClickListener)
        confirm.setOnClickListener(onClickListener)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.currentLocationSettingByMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setLatLngBoundsForCameraTarget(LatLngBounds(LatLng(35.0, 126.0), LatLng(38.0, 128.0)))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(Location.lat, Location.lng)))
        googleMap.setMinZoomPreference(12.0f)
        googleMap.setMaxZoomPreference(21.0f)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        }
        googleMap.uiSettings.isMyLocationButtonEnabled
        googleMap.uiSettings.isZoomControlsEnabled
        googleMap.setOnCameraIdleListener {
            latLng = googleMap.cameraPosition.target
            Location.getLocationName(latLng.latitude, latLng.longitude) { s ->
                location_title!!.text = s
            }
        }
    }


}
