package com.example.fooddeuk.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.example.fooddeuk.common.CommonValueApplication
import com.google.android.gms.location.*
import com.orhanobut.logger.Logger
import java.util.*

/**
 * Created by heo on 2018. 2. 11..
 */
object Location {
    //100 = 1seconds
    var lat: Double = 0.toDouble()
    var lng: Double = 0.toDouble()
    val baseInterval: Long = 100
    val baseFastestInterval: Long = 100
    val basePriority = LocationRequest.PRIORITY_HIGH_ACCURACY
    @SuppressLint("StaticFieldLeak")
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var getLocationCallback: LocationCallback
    lateinit var restartLocationCallback: LocationCallback
    lateinit var geocoder : Geocoder


    var locationRequest: LocationRequest = LocationRequest().apply {
        interval = baseInterval
        fastestInterval = baseFastestInterval
        priority = basePriority
    }

    private fun initGeocoder(context: Context){
        geocoder= Geocoder(context,Locale.KOREA)
    }

    //
    fun setLocation(context: Context,latLngCallback: () -> Unit) {
        initGeocoder(context)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(CommonValueApplication.getInstance())
        getLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                lat = locationResult.locations[0].latitude
                lng = locationResult.locations[0].longitude
                latLngCallback()
                mFusedLocationClient.removeLocationUpdates(this)
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                super.onLocationAvailability(locationAvailability)
            }
        }
        restartLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                lat = locationResult.locations[0].latitude
                lng = locationResult.locations[0].longitude
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                super.onLocationAvailability(locationAvailability)
            }
        }
        startLocationUpdate()
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdate() {
        mFusedLocationClient.requestLocationUpdates(locationRequest, getLocationCallback, null)
    }

    @SuppressLint("MissingPermission")
    fun persistLocationUpdate() {
        mFusedLocationClient.requestLocationUpdates(locationRequest, restartLocationCallback, null)
    }

    fun stopLocationUpdate() {
        mFusedLocationClient.removeLocationUpdates(getLocationCallback)
    }

    fun getLocationName(lat: Double = this.lat, lng: Double = this.lng): String? {
        try {
            //좌표->주소
            val addresses: Address
            addresses = geocoder.getFromLocation(lat, lng, 1)[0]
            //구 없으면 시
            val subLocality = if (addresses.subLocality != null) addresses.subLocality else addresses.locality
            //동 없으면 리?
            val thoroughFare = if (addresses.thoroughfare != null) addresses.thoroughfare else addresses.subThoroughfare


            if ((subLocality != null) and (thoroughFare != null)) {

                return subLocality + " " + thoroughFare

            } else {
                Logger.d(addresses.getAddressLine(0))
                return addresses.getAddressLine(0).substring(12)
            }


        }
        catch (e: Exception) {
            Logger.d(e)
        }

        return null

    }
}
