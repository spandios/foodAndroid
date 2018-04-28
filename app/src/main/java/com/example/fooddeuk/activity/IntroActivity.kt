package com.example.fooddeuk.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Location.getLocation
import com.example.fooddeuk.`object`.Login
import com.example.fooddeuk.util.NetworkUtil
import com.tbruyelle.rxpermissions2.RxPermissions


class IntroActivity : BaseActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        basicInit()
    }

    private fun basicInit() {
//        Picasso.with(this).load(R.drawable.rv2).into(introImageView)
        NetworkUtil.CheckNetGps(this)
        val rxPermissions = RxPermissions(this)
        rxPermissions
                .request(Manifest.permission.READ_CONTACTS, Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe { granted ->
                    if (granted) {
                        Login.checkUser({
                            getLocation({ _, _,_ ->  nextActivity()})
                        })

                    } else {
                        Toast.makeText(this@IntroActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
    }


    private fun nextActivity(){
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
}



