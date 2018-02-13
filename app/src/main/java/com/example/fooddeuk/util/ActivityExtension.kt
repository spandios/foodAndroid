package com.example.fooddeuk.util

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by heo on 2018. 2. 11..
 */
//ADD FRAGMENT WITH TAG
//fun AppCompatActivity.findFragmentByTag(tag : String) : Fragment {
//    supportFragmentManager.findFragmentByTag(tag)
//}
//FRAGMENT REPLACE
fun AppCompatActivity.replaceFragmentToActivity(frameId: Int,fragment: Fragment) {
    val transaction = this.supportFragmentManager.beginTransaction()
    transaction.replace(frameId, fragment)
    transaction.commit()
}

fun AppCompatActivity.StartActivity( t: Class<*>) {
    this.startActivity(Intent(this,t))
}

fun AppCompatActivity.StartActivity(t: Class<*>, extra: Bundle) {
    this.startActivity(Intent(this,t).apply { putExtras(extra) })
}



