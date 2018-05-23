package com.example.fooddeuk.`object`

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import com.example.fooddeuk.util.LayoutUtil
import com.example.fooddeuk.util.NetworkUtil
import com.example.fooddeuk.util.SettingActivityUtil
import com.orhanobut.logger.Logger

/**
 * Created by heo on 2018. 3. 3..
 */
object Util {
    fun StringFormat(context: Context, resource : Int, origin : String) : String =
            String.format(context.getString(resource),origin)

    fun StringFormat(context: Context, resource: Int, origin: String, origin2: String): String =
            String.format(context.getString(resource), origin, origin2)

    fun logger(tag : String ="", obj : Any){
        Logger.d(tag+":"+" "+obj)
    }

    fun logger(str : String){
        Logger.d(str)
    }

    fun logger(any : Any){
        Logger.d(any)
    }

    fun startActivity(context: Context, t: Class<*>) {
        val intent = Intent(context, t)
        context.startActivity(intent)
    }

    fun startActivity(context: Context, t: Class<*>, extra: Bundle) {
        val intent = Intent(context, t)
        intent.putExtras(extra)
        context.startActivity(intent)
    }


    fun convertDpToPixel(dp: Float, context: Context): Float {
        val r = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun getDivider(context: Context): View {
        val division = View(context)
        division.setBackgroundColor(Color.parseColor("#817e7e"))
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Math.round(LayoutUtil.convertDpToPixel(0.3.toFloat(), context)))
        return division
    }

    fun globalLayout(view : View , callback : ()->Unit){
        val mGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                callback()
                //리스너삭제
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener)
    }


    fun checkNetworkAndGPS(activity: Activity) {
        //check network
        if (isNetworkConnected(activity)) {
            //위치기능 비활성하이면
            if (!isGpsPossible(activity)) {
                SettingActivityUtil.settingGPS(activity)
            }
        } else {
            SettingActivityUtil.checkNetwork(activity)
        }
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun isGpsPossible(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //GpsUtil, network불가능하다면
        return !(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

}