package com.example.fooddeuk.activity

import android.annotation.SuppressLint
import android.content.IntentSender
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.orhanobut.logger.Logger
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS


/**
 * Created by heojuyeong on 2017. 10. 9..
 */

open class BaseActivity : AppCompatActivity() {
    var lat: Double = 0.toDouble()
    var lng: Double = 0.toDouble()
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var locationManager: LocationManager
    lateinit var googleApiClient: GoogleApiClient
    val locationRequest = LocationRequest()
    lateinit var locationCallback: LocationCallback


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.lastLocation
                .addOnSuccessListener(this) { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        Logger.d(location.latitude)
                        Logger.d(location.longitude)
                    }
                }
        createLocationRequest()


//        initLocationManager()
//        initGoogleAPI()


    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    @SuppressLint("MissingPermission")
    protected fun createLocationRequest() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 5000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener(this) {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    Logger.d(locationResult.locations[0].longitude)
                    Logger.d(locationResult.locations[0].latitude)
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                    super.onLocationAvailability(locationAvailability)
                }
            }

            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    locationCallback,
                    null /* Looper */);

        }

        task.addOnFailureListener(this) { e ->
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(this@BaseActivity,
                            REQUEST_CHECK_SETTINGS)
                }
                catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }

            }
        }

    }
}
