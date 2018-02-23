package com.example.fooddeuk.util

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.orhanobut.logger.Logger

/**
 * Created by heo on 2018. 2. 11..
 */

fun  RecyclerView.setLayout(context : Context){
    this.layoutManager= LinearLayoutManager(context)
    this.itemAnimator = DefaultItemAnimator()
//    setHasFixedSize(true); 고정된 아이템일 때 성능 최적화
}



val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Float.toPx : Float
    get()=(this*Resources.getSystem().displayMetrics.density)

val Float.toDP : Float
    get()=(this/Resources.getSystem().displayMetrics.density)

fun AppCompatActivity.logger(tag : String ="",obj : Any){
    Logger.d(tag+":"+" "+obj)
}

fun AppCompatActivity.logger(str : String){
    Logger.d(str)
}

fun AppCompatActivity.logger(any : Any){
    Logger.d(any)
}

fun View.findId(id: Int) : View = this.findViewById(id)

fun AppCompatActivity.addFragmentToActivity(frameId: Int, fragment: Fragment) {
    val transaction = this.supportFragmentManager.beginTransaction()
    transaction.add(frameId, fragment)
    transaction.commit()
}

fun AppCompatActivity.replaceFragmentToActivity(frameId: Int, fragment: Fragment) {
    val transaction = this.supportFragmentManager.beginTransaction()
    transaction.replace(frameId, fragment)
    transaction.commit()
}

fun AppCompatActivity.showFragmentToActivity(fragment: Fragment) {
    val transaction = this.supportFragmentManager.beginTransaction()
    transaction.show(fragment)
    transaction.commit()
}

fun AppCompatActivity.hideFragmentToActivity(fragment: Fragment) {
    val transaction = this.supportFragmentManager.beginTransaction()
    transaction.hide(fragment)
    transaction.commit()
}

fun AppCompatActivity.StartActivity(t: Class<*>) {
    this.startActivity(Intent(this, t))
}

fun AppCompatActivity.StartActivity(t: Class<*>, extra: Bundle) {
    this.startActivity(Intent(this, t).apply { putExtras(extra) })
}

fun Fragment.StartActivity(t: Class<*>){
    activity?.let{
        it.startActivity(Intent(it,t))
    }

}

fun Fragment.StartActivity(t: Class<*>,extra : Bundle){
    activity?.let{
        it.startActivity(Intent(it,t).putExtras(extra))
    }
}




