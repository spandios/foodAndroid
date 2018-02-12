package com.example.fooddeuk.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast


/**
 * Created by heojuyeong on 2017. 10. 9..
 */

open class BaseActivity : AppCompatActivity() {
    var lat: Double = 0.toDouble()
    var lng: Double = 0.toDouble()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}
