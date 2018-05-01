package com.example.fooddeuk.menu.listview

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.restaurant.detail.DetailRestaurantActivity
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.util.WrapPager
import com.example.fooddeuk.util.gone
import com.ogaclejapan.smarttablayout.SmartTabLayout
import kotlinx.android.synthetic.main.activity_detail_restaurant.*


/**
 * Created by heo on 2018. 2. 22..
 * ViewPager For MenuList
 */

class DetailRestaurantVP(var context: Context, val restaurant: Restaurant) : PagerAdapter() {
    private var layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if(position==0){
            val itemView = layoutInflater.inflate(R.layout.fragment_rest_menu, container, false)
            val mMeuListViewPager =itemView.findViewById<WrapPager>(R.id.vp_menu_list)

            val restaurantScrollView=(context as DetailRestaurantActivity).scroll_rest_detail
            val menuListVPAdapter = MenuListViewPagerAdapter(context, restaurant.menuCategory, restaurant) { position, menuItemHeight ->
                restaurantScrollView.scrollTo(0, (context as DetailRestaurantActivity).vpMainHeight + position * menuItemHeight)
            }
            mMeuListViewPager.adapter = menuListVPAdapter
            itemView.findViewById<SmartTabLayout>(R.id.tab_rest_menu_real).let{
                it.setViewPager(mMeuListViewPager)
            }
            //DetailRestaurant Activity 페이크 탭 설정
            (context as DetailRestaurantActivity).fakeTab.setViewPager(mMeuListViewPager)
            container.addView(itemView)
            return itemView
        }else if(position==1){
            val itemView = layoutInflater.inflate(R.layout.fragment_detail_restaurant_more_detail, container, false)
            container.addView(itemView)
            return itemView

//            val itemView = layoutInflater.inflate(R.layout.fragment_detail_restaurant_review, container, false)
//            val rvReview= itemView.findViewById<RecyclerView>(R.id.review_rv)
//            HTTP.httpService.getReviewByRestaurant(restaurant._id).compose(HTTP.singleAsync()).subscribe({
//                if(it.success){
//                    rvReview.setting(DetailRestaurantReviewAdapter(context, it.result))
//                }
//            },{
//                it.printStackTrace()
//            })
//            container.addView(itemView)
//            return itemView

        }else{
            val itemView = layoutInflater.inflate(R.layout.fragment_detail_restaurant_more_detail, container, false)
            itemView.gone()
            return itemView
        }
    }

    override fun getCount(): Int = 3

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun getPageTitle(position: Int): CharSequence{
        return when(position){
            0->"메뉴"
            1->"상세정보"
            else->"리뷰"
        }
    }

}