package com.example.fooddeuk.activity

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.example.fooddeuk.R
import com.example.fooddeuk.adapter.MenuVPAdapter
import com.example.fooddeuk.model.menu.MenuCategory
import com.example.fooddeuk.model.restaurant.Restaurant
import com.example.fooddeuk.rx.RxBus
import com.orhanobut.logger.Logger
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_serach.*


class SerachActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var menuCategory: ArrayList<MenuCategory>
    lateinit var restaurant: Restaurant
    var totalScrollHeight = 0
    var menuItemHeight = 0
    var scrollBaseHeight = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serach)
        menuItemHeight = resources.getDimensionPixelOffset(R.dimen.menu_item)
        val mGlobalLayoutListener = object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                //TODO View가 다 그려진 뒤에 값 구하기
                totalScrollHeight = (tab.y).toInt() -toolbar.height
                scrollBaseHeight = (vp.y).toInt() - (toolbar.height * 2)
                //리스너삭제
                scroll.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
        scroll.viewTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener)

        RxBus.intentSubscribe(3, this.javaClass, Consumer {
            if (it is Restaurant) {
                restaurant = it
                menuCategory = restaurant.menuCategory
                val vpAdapter = MenuVPAdapter(this, menuCategory, restaurant, { position: Int, menuItemHeight : Int->
                    Logger.d(menuItemHeight)
                    scroll.scrollTo(0, scrollBaseHeight + (position * menuItemHeight)-100)
                })
                vp.adapter = vpAdapter

                tab.setViewPager(vp)
                tab2.setViewPager(vp)

                scroll.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                    //                    logger("totoal ${scroll.getChildAt(0).height} scroll : ${scroll.height} image : ${test_image.height}, toolbar : ${toolbar.height} tab : ${tab.y} viewpager : ${vp.y}")
                    val alpha = (Math.min(1f, scrollY.toFloat() / totalScrollHeight)) * 255
                    if (alpha >= 255) {
                        tab2.visibility = View.VISIBLE
                    } else {
                        tab2.visibility = View.INVISIBLE
                    }
                    toolbar.background.alpha = alpha.toInt()
                }
            }
        })


    }
}
