package com.example.fooddeuk.menu

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.menu.listview.MenuListViewPagerAdapter
import com.example.fooddeuk.menu.model.MenuCategory
import com.example.fooddeuk.restaurant.detail.DetailRestaurantActivity
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.WrapPager
import com.southernbox.springscrollview.SpringScrollView
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_detail_restaurant.*
import kotlinx.android.synthetic.main.fragment_rest_menu_category.*
import java.util.*


/**
 * Created by heo on 2017. 11. 5..
 */

//메뉴카테고리 ->  tab layout  , 메뉴본문 -> viewpager
class RestMenuFragment : Fragment() {

    private lateinit var menuCategory: ArrayList<MenuCategory>
    private lateinit var restaurant: Restaurant

    private lateinit var mMeuListViewPager: WrapPager
    private lateinit var restaurantScrollView: SpringScrollView

    companion object {
        fun newInstance(): RestMenuFragment {
//            val restMenuCategoryFragment = RestMenuFragment()
//            return restMenuCategoryFragment.apply {
//                arguments=Bundle().apply { putSerializable("restaurant",restaurant) }
//            }
            return RestMenuFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBus.intentSubscribe(RxBus.RestMenuFragmentData, RestMenuFragment::class.java, Consumer {
            if (it is Restaurant) {
                restaurant = it
                menuCategory = restaurant.menuCategory
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_rest_menu_category, container, false)
        mMeuListViewPager =root.findViewById(R.id.vp_menu_list)
        restaurantScrollView=(this@RestMenuFragment.activity as DetailRestaurantActivity).scroll_rest_detail
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //1.메뉴 컨텐츠 뷰페이저 설정 2.뷰페이저 안에 있는 메뉴 리스트를 클릭했을 시 스크롤 이동하는 스크롤리스너 설정
        val menuListVPAdapter = MenuListViewPagerAdapter(context!!, menuCategory, restaurant) { position, menuItemHeight ->
            restaurantScrollView.scrollTo(0, (this@RestMenuFragment.activity as DetailRestaurantActivity).scrollMenuTabLayout + position * menuItemHeight)
        }

        vp_menu_list.adapter = menuListVPAdapter
        tab_rest_menu_real.setViewPager(vp_menu_list)

        //DetailRestaurant Activity 페이크 탭 설정
        (this@RestMenuFragment.activity as DetailRestaurantActivity).fakeTab.setViewPager(mMeuListViewPager)

    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.intentUnregister(RestMenuFragment::class.java)
    }

}
