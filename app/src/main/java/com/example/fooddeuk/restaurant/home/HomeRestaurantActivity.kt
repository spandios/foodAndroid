package com.example.fooddeuk.restaurant.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.GlobalVariable.distance
import com.example.fooddeuk.home.HomeFragment
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.gone
import com.example.fooddeuk.util.visible
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_home_restaurant.*


class HomeRestaurantActivity : AppCompatActivity(), View.OnClickListener {

    private var menu_category = "" // Default == 아무거나
    private var filter = distance //Default Filter == Distance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_restaurant)
        menu_category = getString(R.string.restaurant_menu_type0)

        RxBus.intentSubscribe(RxBus.HomeMenuActivityData, this::class.java, Consumer {
            if (it is Int) {
                if (it == HomeFragment.menu_anything) {

                }
            }
        })

        renderView()

    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.intentUnregister(this::class.java)
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.home_restaurant_back -> finish()

            R.id.home_restaurant_menu_category -> {
                header_option.visible()
                header_option_address.gone()
                header_option_menu_category.visible()
            }

            R.id.home_restaurant_address -> {
                header_option.visible()
                header_option_menu_category.gone()
                header_option_address.visible()
            }

            R.id.home_restaurant_map -> {
            }

        }

    }


    private fun renderView() {
        home_restaurant_back.setOnClickListener(this)
        home_restaurant_menu_category.setOnClickListener(this)
        clickHeaderOptionForMenu()
        clickHeaderOptionForAddress()
    }


    private fun clickHeaderOptionForMenu() {
        val parentLayout = header_option_menu_category.getChildAt(0) as LinearLayout

        for (i in 0 until parentLayout.childCount) {
            val menuOption = parentLayout.getChildAt(i) as TextView
            menuOption.setOnClickListener {

                menuOption.let {
                    home_restaurant_menu_category.text = it.text
                    menu_category = it.tag as String
                }

                header_option_menu_category.gone()
                header_option.gone()
            }
        }
    }

    private fun clickHeaderOptionForAddress() {
        val parentLayout = header_option_address.getChildAt(0) as LinearLayout

        for (i in 0 until parentLayout.childCount) {
            val addressOption = parentLayout.getChildAt(i) as TextView

            //address
            addressOption.setOnClickListener {
                when (i) {
                    0 -> {
                        //TODO 내근처
                    }
                    1 -> {
                        //TODO 지도
                    }
                    2 -> {
                        //TODO 지하철
                    }
                    3 -> {
                        //TODO 대학교
                    }
                }
                header_option_menu_category.gone()
                header_option.gone()

            }
        }
    }
}
