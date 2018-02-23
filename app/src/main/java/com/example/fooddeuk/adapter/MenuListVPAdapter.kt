package com.example.fooddeuk.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.model.menu.MenuCategory
import com.example.fooddeuk.model.restaurant.Restaurant
import java.util.*



/**
 * Created by heo on 2018. 2. 22..
 */

class MenuListVPAdapter(var context: Context, var menuCategories: ArrayList<MenuCategory>, val restaurant: Restaurant, val clickItemHeight : (position: Int, menuItemHeight : Int)->Unit) : PagerAdapter() {
    internal var layoutInflater: LayoutInflater

    init {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return menuCategories.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = layoutInflater.inflate(R.layout.item_vp_menu_list, container, false)
        val menuRecyclerView = itemView.findViewById<RecyclerView>(R.id.test_recyclerview)
        val nmLayoutManager = LinearLayoutManager(context)
        menuRecyclerView.setLayoutManager(nmLayoutManager)
        menuRecyclerView.setFocusable(false)
        menuRecyclerView.setFocusableInTouchMode(false)
        menuRecyclerView.isNestedScrollingEnabled=false

        menuRecyclerView.adapter = MenuListAdapter(context, menuCategories[position].menu_content, restaurant).apply {
            setOnItemClickListener { view, y, menuItemHeight ->
                clickItemHeight(view.adapterPosition,menuItemHeight)
            }
        }

//
//        menuRecyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
//            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
//
//                when (e.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        startClickTime = Calendar.getInstance().timeInMillis
//                    }
//                    MotionEvent.ACTION_UP -> {
//                        val clickDuration = Calendar.getInstance().timeInMillis - startClickTime
//                        if (clickDuration < MAX_CLICK_DURATION) {
//                                val reV = rv.findChildViewUnder(e.x, e.y)
//                                val itemHeight = reV.y
////                                Logger.d("item Height " +itemHeight)
//                                var originalPos = IntArray(2)
//                                reV.getLocationInWindow(originalPos)
//                                val y=originalPos[1]
//                                Logger.d("original pos = " + y)
//                                clickItemHeight(y)
//                                val position = rv.getChildAdapterPosition(reV)
//                        }
//                    }
//                }
//                return false
//                // RecyclerView로 전달된 TouchEvent를 받는다.
//
//            }
//
//            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
//                // onInterceptTouchEvent의 반환 값이 true일 경우 TouchEvent를 가로채어 동작한다.
//            }
//
//            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
//                // Item이 상위 RecyclerView가 TouchEvent를 가로채길 원치 않을 때 호출된다.
//            }
//        })
        container.addView(itemView)
        return itemView
    }


    override fun getPageTitle(position: Int): CharSequence? = menuCategories[position].menu_category_name

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}