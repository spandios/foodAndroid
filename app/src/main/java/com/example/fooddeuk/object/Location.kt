package com.example.fooddeuk.`object`

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import com.example.fooddeuk.GlobalApplication
import com.example.fooddeuk.GlobalApplication.httpService
import com.example.fooddeuk.network.HTTP.Single
import com.google.android.gms.location.*
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
    val mFusedLocationClient: FusedLocationProviderClient =LocationServices.getFusedLocationProviderClient(GlobalApplication.getInstance())
    val geocoder : Geocoder =Geocoder(GlobalApplication.getInstance(),Locale.KOREA)



    var locationRequest: LocationRequest = LocationRequest().apply {
        interval = baseInterval
        fastestInterval = baseFastestInterval
        priority = basePriority
    }

    @SuppressLint("MissingPermission")
    fun getLocation(callback : (lat : Double, lng : Double)->Unit) {
        val getLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                lat = locationResult.locations[0].latitude
                lng = locationResult.locations[0].longitude
                getLocationName(lat, lng,{
                    mFusedLocationClient.removeLocationUpdates(this)
                    locationName=it
                    callback(lat, lng)
                })

            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                super.onLocationAvailability(locationAvailability)
            }
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest, getLocationCallback, null)
    }


    fun getLocationName(lat: Double = Location.lat, lng: Double = Location.lng,callback : (gudong : String)->Unit){
        try {
            //좌표->주소
            Single(httpService.getLocationNameByNaver("$lng,$lat")).subscribe({

                if(it.gudong == "error"){
                    val addresses: Address = geocoder.getFromLocation(lat, lng, 1)[0]
                    //구 없으면 시
                    val subLocality = if (addresses.subLocality != null) addresses.subLocality else addresses.locality
                    //동 없으면 리?
                    val thoroughFare = if (addresses.thoroughfare != null) addresses.thoroughfare else addresses.subThoroughfare


                    if ((subLocality != null) and (thoroughFare != null)) {
                        callback(subLocality + " " + thoroughFare)
                    } else {
                        callback(addresses.getAddressLine(0).substring(12))
                    }
                }else{
                    callback(it.gudong)
                }
            },{it.printStackTrace()})


        }
        catch (e: Exception) {
            callback("주소 오류")
            e.printStackTrace()
        }
    }
}
