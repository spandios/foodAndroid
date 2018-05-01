package com.example.fooddeuk.restaurant

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.fooddeuk.menu.RestMenuFragment
import com.example.fooddeuk.restaurant.detail.detail.DetailRestaurantMoreDetailFragment
import com.example.fooddeuk.restaurant.detail.review.DetailRestaurantReviewFragment

/**
 * Created by heo on 2018. 4. 29..
 */

class Pager(fm: FragmentManager, private val rest_id: String) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // 해당하는 page의 Fragment를 생성합니다.
        return if (position == 0) {
            RestMenuFragment.newInstance()
        } else if (position == 1) {
            DetailRestaurantMoreDetailFragment.newInstance()
        } else {
            DetailRestaurantReviewFragment.newInstance(rest_id)
        }
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) {
            "메뉴"
        } else if (position == 1) {
            "상세정보"
        } else {
            "리뷰"
        }
    }
}
