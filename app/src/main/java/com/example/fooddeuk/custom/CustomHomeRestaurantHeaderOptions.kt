package com.example.fooddeuk.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.example.fooddeuk.R

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
        View.inflate(context, R.layout.custom_home_restaurant_menu_category, this)

    }
}