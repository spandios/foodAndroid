package com.example.fooddeuk.activity

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.example.fooddeuk.R
import com.example.fooddeuk.menu.listview.MenuListViewPagerAdapter
import com.example.fooddeuk.menu.model.MenuCategory
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.logger
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
                totalScrollHeight = (tab.y).toInt() - header.height
                scrollBaseHeight = (vp.y).toInt() - (header.height * 2)
                //리스너삭제
                scroll.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
        scroll.viewTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener)

        RxBus.intentSubscribe(3, this.javaClass, Consumer {
            if (it is Restaurant) {
                restaurant = it
                menuCategory = restaurant.menuCategory
                val vpAdapter = MenuListViewPagerAdapter(this, menuCategory, restaurant, { position: Int, menuItemHeight: Int ->
                    Logger.d(menuItemHeight)
                    logger("scroll", scrollBaseHeight + (position * menuItemHeight))
                    scroll.scrollTo(0, scrollBaseHeight + (position * menuItemHeight))
                })
                vp.adapter = vpAdapter

                tab.setViewPager(vp)
                tab2.setViewPager(vp)

                scroll.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                    //                    logger("totoal ${scroll.getChildAt(0).height} scroll : ${scroll.height} image : ${test_image.height}, toolbar : ${toolbar.height} tab : ${tab.y} viewpager : ${vp.y}")
                    logger(scrollY)
                    val alpha = (Math.min(1f, scrollY.toFloat() / totalScrollHeight)) * 255
                    if (alpha >= 255) {
                        tab2.visibility = View.VISIBLE
                    } else {
                        tab2.visibility = View.INVISIBLE
                    }
                    header.background.alpha = alpha.toInt()
                }
            }
        })


    }
}
