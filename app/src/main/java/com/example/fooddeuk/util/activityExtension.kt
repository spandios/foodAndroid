package com.example.fooddeuk.util

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by heo on 2018. 2. 11..
 */

//FRAGMENT REPLACE
fun AppCompatActivity.replaceFragmentToActivity(frameId: Int,fragment: Fragment) {
    val transaction = this.supportFragmentManager.beginTransaction()
    transaction.replace(frameId, fragment)
    transaction.commit()
}



