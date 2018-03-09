package com.example.fooddeuk.util

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import android.widget.Toast
import com.orhanobut.logger.Logger
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import java.text.DecimalFormat

/**
 * Created by heo on 2018. 2. 11..
 */


fun RecyclerView.setting(adapter : RecyclerView.Adapter<*> ,overscroll : Boolean = false, verticalPadding : Boolean =false ,hasFixed : Boolean =false) {

    val nmLayoutManager = LinearLayoutManager(context)
    this.layoutManager = nmLayoutManager
    this.itemAnimator = DefaultItemAnimator()

    this.adapter=adapter
    if(overscroll){
        OverScrollDecoratorHelper.setUpOverScroll(this, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)
    }

    if(hasFixed){
        this.setHasFixedSize(true)
    }
    if(verticalPadding){
        this.addItemDecoration(VerticalSpaceItemDecoration(12.toPx))
    }
}

fun String.getTime() : Pair<Int,Int>{
    val endHour = this.substring(0, 2).toInt()
    val endMinutes = this.substring(3, 5).toInt()
    return Pair(endHour,endMinutes)
}
fun View.afterView(callback : ()->Unit){
    val mGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {

            callback()
            this@afterView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            //리스너삭제
        }
    }
    this.viewTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener)
}

fun AppCompatActivity.toast(content : String){
    Toast.makeText(this,content,Toast.LENGTH_LONG).show()
}

fun Fragment.toast(content : String){
    Toast.makeText(this.activity,content,Toast.LENGTH_LONG).show()
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

fun TextView.textToString() : String = this.text.toString()

fun Int.toCommaWon() : String{
    val Commas = DecimalFormat("#,###")
    val result_int = Commas.format(this.toLong())
    return result_int + "원"
}

fun String.toCommaWon(): String {
    val Commas = DecimalFormat("#,###")
    val result_int = Commas.format(this.toLong())
    return result_int + "원"
}

fun String.toJustWon() : String{
    return this + "원"
}

fun Int.toJustWon() : String{
    return "{$this}원"
}


fun String.addPlus() : String{
    return this.getOriginalPrice().toOptionWon()
}

fun Int.toOptionWon(): String {
    val Commas = DecimalFormat("#,###")
    val result_int = Commas.format(this.toLong())
    return "+" + result_int + "원"
}
fun String.toOptionWon(): String {
    val Commas = DecimalFormat("#,###")
    val result_int = Commas.format(this.toLong())
    return "+" + result_int + "원"
}
fun CharSequence.getOriginalPrice(): Int {
    val resultPrice = StringBuilder(this)

    if (resultPrice.toString().contains("+")) {
        resultPrice.deleteCharAt(resultPrice.indexOf("+"))
    }

    if (resultPrice.toString().contains("원")) {
        resultPrice.deleteCharAt(resultPrice.indexOf("원"))

    }

    if (resultPrice.length >= 5) {
        while (true) {
            if (resultPrice.toString().contains(","))
                resultPrice.deleteCharAt(resultPrice.indexOf(","))
            else {
                break
            }
        }
    }

    return Integer.parseInt(resultPrice.toString())
}

fun String.getOriginalPrice(): Int {
    val resultPrice = StringBuilder(this)

    if (resultPrice.toString().contains("+")) {
        resultPrice.deleteCharAt(resultPrice.indexOf("+"))
    }

    if (resultPrice.toString().contains("원")) {
        resultPrice.deleteCharAt(resultPrice.indexOf("원"))

    }

    if (resultPrice.length >= 5) {
        while (true) {
            if (resultPrice.toString().contains(","))
                resultPrice.deleteCharAt(resultPrice.indexOf(","))
            else {
                break
            }
        }
    }

    return Integer.parseInt(resultPrice.toString())
}

fun TextView.text() : String = this.text.toString()







