package com.example.fooddeuk.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.`object`.Login
import com.example.fooddeuk.`object`.Util.checkNetworkAndGPS
import com.example.fooddeuk.util.StartActivity
import com.iwedding.app.helper.RecentPref
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions


class IntroActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        firstInit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun firstInit() {
        //네트워크 gps체크
        checkNetworkAndGPS(this)
        getRecentLocation()
        val rxPermissions = RxPermissions(this)
        rxPermissions
                .request(Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe { granted ->
                    if (granted) {
                        Login.checkUser({ nextActivity() })
                    } else {
                        Toast.makeText(this@IntroActivity, "권한을 설정해주세요", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
    }


    private fun getRecentLocation() {
        val lat = (RecentPref.recentPref.getString("lat", ""))
        val lng = (RecentPref.recentPref.getString("lng", ""))
        val locationName = RecentPref.recentPref.getString("locationName", "")

        //최근 저장된 위치정보가 없다면 새로고침
        if (lat == "" || lng == "" || locationName == "") {
            Location.getLocation { _, _, _ -> }
        }else {
            Logger.d("기존의 위치정보 lat, lng, locationName  : ${Location.lat} , ${Location.lng} , ${Location.locationName}")
            Location.lat = lat.toDouble()
            Location.lng = lng.toDouble()
            Location.locationName = locationName
        }
    }

    private fun nextActivity() {
        Handler().postDelayed({
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            StartActivity(MainActivity::class.java)
            finish()
        }, 100)
    }

}



