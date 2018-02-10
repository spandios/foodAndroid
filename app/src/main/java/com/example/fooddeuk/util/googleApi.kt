package com.example.fooddeuk.util

import android.annotation.SuppressLint
import android.os.Bundle
import com.example.fooddeuk.common.CommonValueApplication
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.orhanobut.logger.Logger

@SuppressLint("StaticFieldLeak")
/**
 * Created by heo on 2018. 2. 11..
 */
object googleApi {

    val context = CommonValueApplication.getInstance()
    val api = GoogleApiAvailability.getInstance()

    val googleApiClient = GoogleApiClient.Builder(googleApi.context)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(
                    object : GoogleApiClient.ConnectionCallbacks {
                        override fun onConnected(bundle: Bundle?) {

                        }

                        override fun onConnectionSuspended(i: Int) {

                        }
                    })
            .addOnConnectionFailedListener { connectionResult ->
                Logger.d(connectionResult)
            }
            .build()

}

