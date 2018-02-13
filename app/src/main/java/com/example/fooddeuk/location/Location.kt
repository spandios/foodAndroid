package com.example.fooddeuk.location

import android.annotation.SuppressLint
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
    var buzy = false
    var lat: Double = 0.toDouble()
    var lng: Double = 0.toDouble()
    lateinit var locationName : String
    val baseInterval: Long = 100
    val baseFastestInterval: Long = 100
    val basePriority = LocationRequest.PRIORITY_HIGH_ACCURACY
    @SuppressLint("StaticFieldLeak")
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var getLocationCallback: LocationCallback
    var restartLocationCallback: LocationCallback =object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            lat = locationResult.locations[0].latitude
            lng = locationResult.locations[0].longitude

        }

        override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
            super.onLocationAvailability(locationAvailability)
        }
    }
    val geocoder : Geocoder =Geocoder(CommonValueApplication.getInstance(),Locale.KOREA)



    var locationRequest: LocationRequest = LocationRequest().apply {
        interval = baseInterval
        fastestInterval = baseFastestInterval
        priority = basePriority
    }

    fun getLocation(callback : (lat : Double, lng : Double)->Unit) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(CommonValueApplication.getInstance())
        getLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                lat = locationResult.locations[0].latitude
                lng = locationResult.locations[0].longitude

//                getCallback(lat,lng)
                callback(lat,lng)
                mFusedLocationClient.removeLocationUpdates(this)
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

//    @SuppressLint("MissingPermission")
//    fun persistLocationUpdate() {
//        mFusedLocationClient.requestLocationUpdates(locationRequest, restartLocationCallback, null)
//    }
//
//    fun stopLocationUpdate() {
//        mFusedLocationClient.removeLocationUpdates(getLocationCallback)
//    }

    fun getLocationName(lat: Double = Location.lat, lng: Double = Location.lng): String? {
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
                return addresses.getAddressLine(0).substring(12)
            }
        }
        catch (e: Exception) {
            Logger.d(e)
        }
        return null

    }
}
