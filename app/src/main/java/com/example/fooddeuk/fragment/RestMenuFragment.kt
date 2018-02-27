package com.example.fooddeuk.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.activity.DetailRestaurantActivity
import com.example.fooddeuk.activity.MenuPager
import com.example.fooddeuk.listview.menu.MenuListViewPagerAdapter
import com.example.fooddeuk.model.menu.MenuCategory
import com.example.fooddeuk.model.restaurant.Restaurant
import com.southernbox.springscrollview.SpringScrollView
import kotlinx.android.synthetic.main.activity_detail_restaurant.*
import kotlinx.android.synthetic.main.fragment_rest_menu_category.*
import java.util.*


/**
 * Created by heo on 2017. 11. 5..
 */

//메뉴카테고리 ->  tab layout  , 메뉴본문 -> viewpager
class RestMenuFragment() : Fragment() {

    lateinit var menuCategory: ArrayList<MenuCategory>
    lateinit var restaurant: Restaurant

    lateinit var vp_menu_lsit : MenuPager
    lateinit var restaurantScrollView : SpringScrollView
    companion object {
        fun newInstance(restaurant: Restaurant): RestMenuFragment {
            val restMenuCategoryFragment = RestMenuFragment()
            return restMenuCategoryFragment.apply {
                arguments=Bundle().apply { putSerializable("restaurant",restaurant) }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            restaurant= arguments!!.getSerializable("restaurant") as Restaurant
            menuCategory = restaurant.menuCategory
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_rest_menu_category, container, false)
        vp_menu_lsit=root.findViewById(R.id.vp_menu_list)
        restaurantScrollView=(this@RestMenuFragment.activity as DetailRestaurantActivity).scroll_rest_detail
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //액티비티쪽에서 base scrollview 포지션과 item의 포지션과 크기를 더해 아이템이 클릭됐을 시 스크롤 자동이동한다.

        val menuListVPAdapter = MenuListViewPagerAdapter(context!!, menuCategory, restaurant) { position, menuItemHeight ->
            restaurantScrollView.scrollTo(0, (this@RestMenuFragment.activity as DetailRestaurantActivity).scrollSecondToolbar + position * menuItemHeight)
        }
        vp_menu_list.adapter = menuListVPAdapter
        tab_rest_menu_real.setViewPager(vp_menu_list)
        //fake 탭 설정
        (this@RestMenuFragment.activity as DetailRestaurantActivity).fakeTab.setViewPager(vp_menu_lsit)

    }

}
