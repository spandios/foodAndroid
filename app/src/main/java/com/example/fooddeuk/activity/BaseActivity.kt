package com.example.fooddeuk.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.fooddeuk.`object`.Location
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Created by heojuyeong on 2017. 10. 9..
 */

open class BaseActivity : AppCompatActivity() {
    var lat: Double = 0.toDouble()
    var lng: Double = 0.toDouble()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    fun stopLoading(){
        main_progressbar.visibility= View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun startLoading(){
        main_progressbar.visibility= View.VISIBLE
        main_progressbar.isClickable=false
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun onBackPressed() {
        if(Location.buzy){
            return
        }
        super.onBackPressed()
    }

}
