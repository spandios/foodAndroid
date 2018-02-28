package com.example.fooddeuk.menu.listview

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.model.menu.MenuCategory
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.util.LayoutUtil
import java.util.*



/**
 * Created by heo on 2018. 2. 22..
 * ViewPager For MenuList
 */

class MenuListViewPagerAdapter(var context: Context, private var menuCategories: ArrayList<MenuCategory>, val restaurant: Restaurant, private val clickItemHeight : (position: Int, menuItemHeight : Int)->Unit) : PagerAdapter() {
    var layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = layoutInflater.inflate(R.layout.item_vp_menu_list, container, false)
        val menuRecyclerView = itemView.findViewById<RecyclerView>(R.id.recycle_menu)
        LayoutUtil.RecyclerViewSetting(context,menuRecyclerView)
        menuRecyclerView.isFocusable = false
        menuRecyclerView.isFocusableInTouchMode = false
        menuRecyclerView.isNestedScrollingEnabled=false

        menuRecyclerView.adapter = MenuListAdapter(context, menuCategories[position].menu_content, restaurant).apply {
            setOnItemClickListener { view, menuItemHeight ->
                clickItemHeight(view.adapterPosition,menuItemHeight)
            }
        }

        container.addView(itemView)
        return itemView
    }
    override fun getCount(): Int = menuCategories.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun getPageTitle(position: Int): CharSequence? = menuCategories[position].menu_category_name

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}