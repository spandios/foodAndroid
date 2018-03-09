package com.example.fooddeuk.`object`

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import com.example.fooddeuk.util.LayoutUtil
import com.orhanobut.logger.Logger

/**
 * Created by heo on 2018. 3. 3..
 */
object Util {
    fun stringFormat(context: Context,resource : Int , origin : String) : String =
            String.format(context.getString(resource),origin)

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

}