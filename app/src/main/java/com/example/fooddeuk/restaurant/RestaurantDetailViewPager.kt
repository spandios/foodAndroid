package com.example.fooddeuk.restaurant

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R


/**
 * Created by heo on 2018. 2. 22..
 * ViewPager For MenuList
 */

class RestaurantDetailViewPager(var context: Context) : PagerAdapter() {
    var layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        when (position) {
            0 -> {}
            1 -> {}
            2 -> {}
        }
        val itemView = layoutInflater.inflate(R.layout.item_vp_menu_list, container, false)
        container.addView(itemView)
        return itemView
    }

    override fun getCount(): Int = 3

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun getPageTitle(position: Int): CharSequence?{
        return when (position) {
            0 -> "메뉴"
            1 -> "상세정보"
            else ->"리뷰"
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}