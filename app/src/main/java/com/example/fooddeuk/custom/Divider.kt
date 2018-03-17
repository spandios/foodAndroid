package com.example.fooddeuk.custom

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.LinearLayout
import com.example.fooddeuk.R

/**
 * Created by heo on 2018. 3. 17..
 */

class Divider @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    init {
        this.setBackgroundColor(ContextCompat.getColor(this.context, R.color.silver))
    }

}
