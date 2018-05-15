package com.example.fooddeuk.menu.listview

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.restaurant.detail.DetailRestaurantActivity
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.util.WrapPager
import com.google.android.gms.maps.SupportMapFragment
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_detail_restaurant.*


/**
 * Created by heo on 2018. 2. 22..
 * ViewPager For MenuList
 */

class DetailRestaurantVP(var context: Context, val restaurant: Restaurant, val mainTabChangeCallback: () -> Unit) : PagerAdapter() {
    private val restaurantActivity: DetailRestaurantActivity = context as DetailRestaurantActivity
    private val restaurantScrollView = (context as DetailRestaurantActivity).scroll_rest_detail
    private var layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewPager: WrapPager
        when (position) {
            0 -> {
                val itemView = layoutInflater.inflate(R.layout.item_detail_restaurant_menu, container, false)
                viewPager = itemView.findViewById(R.id.vp_menu_list)
                viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {

                    }

                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                    }

                    override fun onPageSelected(position: Int) {
                        viewPager.onRefresh()
                        mainTabChangeCallback()
                    }
                })

                //MenuListViewPager
                val menuListVPAdapter = MenuListViewPagerAdapter(context, restaurant.menuCategory, restaurant) { position, menuItemHeight ->
                    restaurantScrollView.post { restaurantScrollView.scrollTo(0, (context as DetailRestaurantActivity).vpMenuHeight + (position * menuItemHeight)) }
                }

                viewPager.adapter = menuListVPAdapter
                itemView.findViewById<SmartTabLayout>(R.id.tab_rest_menu_real).setViewPager(viewPager)
                //DetailRestaurant Activity 페이크 탭 설정
                (context as DetailRestaurantActivity).fakeTab.setViewPager(viewPager)
                container.addView(itemView)
                return itemView
            }
            1 -> {
                val itemView = layoutInflater.inflate(R.layout.detail_restaurant_info, container, false)
                ((context as DetailRestaurantActivity).supportFragmentManager.findFragmentById(R.id.more_detail_map) as SupportMapFragment).getMapAsync(restaurantActivity)
                container.addView(itemView)
                return itemView

            }
            else -> return View(context)
        }
    }

    override fun getCount(): Int = 3

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "메뉴"
            1 -> "상세정보"
            else -> "리뷰"
        }


    }

    override fun getItemPosition(`object`: Any): Int {

        return super.getItemPosition(`object`)
    }


}