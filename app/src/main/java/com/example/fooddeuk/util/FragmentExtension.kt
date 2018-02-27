package com.example.fooddeuk.util

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.fooddeuk.activity.MainActivity


/**
 * Created by heo on 2018. 2. 12..
 */
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

fun Fragment.startLoading(){
    (activity as MainActivity).startLoading()
}

fun Fragment.stopLoading(){
    (activity as MainActivity).stopLoading()
}
