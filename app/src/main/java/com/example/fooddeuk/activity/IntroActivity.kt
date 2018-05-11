package com.example.fooddeuk.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.`object`.Login
import com.example.fooddeuk.util.NetworkUtil
import com.iwedding.app.helper.RecentPref
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions


class IntroActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        basicInit()
    }

    private fun basicInit() {
        NetworkUtil.CheckNetGps(this)
        val rxPermissions = RxPermissions(this)
        rxPermissions
                .request(Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe { granted ->
                    if (granted) {
                        Login.checkUser({
                            getRecentLocation()
                        })

                    } else {
                        Toast.makeText(this@IntroActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
    }


    private fun nextActivity() {
        Handler().postDelayed({
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            val intent = Intent(this@IntroActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 100)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun getRecentLocation() {
        val lat = (RecentPref.recentPref.getString("lat", ""))
        val lng = (RecentPref.recentPref.getString("lng", ""))
        val locationName = RecentPref.recentPref.getString("locationName", "")

        if (lat == "" || lng == "" || locationName == "") {
            Location.getLocation { lat, lng, locationName ->
                nextActivity()
            }
        }else{

            Location.lat=lat.toDouble()
            Location.lng=lng.toDouble()
            Location.locationName=locationName
            Logger.d("lat, lng, locationName  : ${Location.lat} , ${Location.lng} , ${Location.locationName}")
            nextActivity()
            Logger.d("second Location")
        }


    }
}



