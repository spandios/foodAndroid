package com.example.fooddeuk.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.fooddeuk.R
import kotlinx.android.synthetic.main.custom_home_restaurant_filter.view.*

/**
 * Created by heo on 2018. 3. 17..
 */
class CustomHomeRestaurantMenuCategory @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        View.inflate(context, R.layout.custom_home_restaurant_menu_category, this)

    }
}


class CustomHomeRestaurantAddress @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.custom_home_restaurant_address, this)
    }
}

class CustomHomeRestaurantFilter @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.custom_home_restaurant_filter, this)
    }

    fun setAllClickListener(clickListener: OnClickListener){
        val parentLayout = header_parent as LinearLayout
        for (i in 0 until parentLayout.childCount) {
            val filter = parentLayout.getChildAt(i) as TextView
            filter.setOnClickListener(clickListener)
        }
    }
}