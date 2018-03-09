package com.example.fooddeuk.custom

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import com.example.fooddeuk.R
import com.example.fooddeuk.util.toPx

/**
 * Created by heo on 2018. 3. 5..
 */
class CustomTimeButton(context: Context,timePair: Pair<Int,Int>,clickListener : (button : Button)->Unit) : CustomButton(context){

    init {

        this.textSize=18f
        this.gravity=Gravity.CENTER
        this.background=ContextCompat.getDrawable(context, R.drawable.custom_time_button_selector)
        this.tag=timePair
        val hour=timePair.first
        val minute=timePair.second
        setOnClickListener {

            clickListener(this)
        }
        val startHourString : String = if(hour<10){
            "0$hour"
        }else{
            hour.toString()
        }
        val startMinuteString : String = if(minute<10){
            "0$minute"
        }else{
            minute.toString()
        }

        this.text="$startHourString:$startMinuteString"
    }
    fun setMargin(){
        val params= LinearLayout.LayoutParams(60.toPx, 40.toPx)
        val margin = 12.toPx
        params.setMargins(margin,margin,margin,margin)
        this.layoutParams=params

    }

}