package com.example.fooddeuk.util

import android.annotation.SuppressLint
import com.example.fooddeuk.common.CommonValueApplication
import com.google.android.gms.location.*

/**
 * Created by heo on 2018. 2. 11..
 */
object Location {
    //100 = 1seconds
    var lat :Double? =null
    var lng :Double? =null
    val baseInterval: Long = 100
    val baseFastestInterval: Long = 100
    val basePriority = LocationRequest.PRIORITY_HIGH_ACCURACY
    @SuppressLint("StaticFieldLeak")
    lateinit var mFusedLocationClient : FusedLocationProviderClient
    lateinit var getLocationCallback: LocationCallback
    lateinit var restartLocationCallback: LocationCallback


    var locationRequest: LocationRequest = LocationRequest().apply {
        interval = baseInterval
        fastestInterval = baseFastestInterval
        priority = basePriority
    }

    fun setLocation(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(CommonValueApplication.getInstance())
        getLocationCallback =  object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                lat = locationResult.locations[0].latitude
                lng = locationResult.locations[0].longitude
                mFusedLocationClient.removeLocationUpdates(this)
            }
            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                super.onLocationAvailability(locationAvailability)
            }
        }
        restartLocationCallback =  object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                lat = locationResult.locations[0].latitude
                lng = locationResult.locations[0].longitude
            }
            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                super.onLocationAvailability(locationAvailability)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdate(){
        mFusedLocationClient.requestLocationUpdates(locationRequest, getLocationCallback,null)
    }
    @SuppressLint("MissingPermission")
    fun persistLocationUpdate(){
        mFusedLocationClient.requestLocationUpdates(locationRequest, restartLocationCallback,null)
    }
    fun stopLocationUpdate(){
        mFusedLocationClient.removeLocationUpdates(getLocationCallback)
    }
}
