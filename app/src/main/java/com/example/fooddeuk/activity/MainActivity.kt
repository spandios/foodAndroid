package com.example.fooddeuk.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.GlobalVariable.recentRestaurant
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.`object`.Util.isGpsPossible
import com.example.fooddeuk.cart.CartFragment
import com.example.fooddeuk.home.HomeFragment
import com.example.fooddeuk.pick.PickFragment
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.restaurant.near.NearRestaurantParentFragment
import com.example.fooddeuk.user.UserFragment
import com.example.fooddeuk.util.SettingActivityUtil
import com.example.fooddeuk.util.setValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iwedding.app.helper.RecentPref
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : BaseActivity() {
    private var homeFragmentFlag = true
    private var fragments: ArrayList<Fragment> = ArrayList()
    lateinit var fragmentPager: FragmentStatePagerAdapter

    companion object {
        private const val GpsSettingActivityRequestCode = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setFragment()
        setNavigation()
        setViewPager()
        stopLoading()
        getRecentRestaurant()

    }

    override fun onResume() {
        super.onResume()
        if (intent.extras != null) { }
    }

    override fun onDestroy() {
        setRecentRestaurant()
        setRecentLocation()
        super.onDestroy()
    }

    private fun setFragment() {
        fragments.add(HomeFragment())
        fragments.add(PickFragment())
        fragments.add(NearRestaurantParentFragment())
        fragments.add(CartFragment())
        fragments.add(UserFragment())
    }

    private fun setNavigation() {
        navigation.apply {
            enableShiftingMode(false)
            enableItemShiftingMode(false)
        }
    }

    private fun setViewPager() {
        fragmentPager = MainViewPagerAdapter(supportFragmentManager)
        home_viewpager.adapter = fragmentPager
        home_viewpager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when(position){
                    0->{
                        (fragments[0] as HomeFragment).clearView()
                    }
                    1->{}
                    2->{}
                    3->{
                        (fragments[3] as CartFragment).setView()
                    }
                    4->{}
                }
            }
        })
        navigation.setupWithViewPager(home_viewpager)
    }


    override fun onBackPressed() {
        if (homeFragmentFlag) {
            //Finish Activity
            super.onBackPressed()
        } else {
            homeFragmentFlag = true

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GpsSettingActivityRequestCode) {
            if (!isGpsPossible(this)) {
                SettingActivityUtil.settingGPS(this)
            }
        }
    }


    private inner class MainViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getCount(): Int = fragments.size

        override fun getItem(position: Int): Fragment? = when (position) {
            0 -> fragments[0]
            1 -> fragments[1]
            2 -> fragments[2]
            3 -> fragments[3]
            4 -> fragments[4]
            else -> null
        }
    }

    private fun getRecentRestaurant() {
        val recentRestaurantString = RecentPref.getValue("recentRestaurant", "")
        if (recentRestaurantString != "") {
            recentRestaurant = Gson().fromJson(recentRestaurantString, object : TypeToken<ArrayList<Restaurant>>() {}.type)
        }
    }

    private fun setRecentRestaurant() {
        if (recentRestaurant.size > 0) {
            val recentRestaurantGson = Gson().toJson(recentRestaurant)
            RecentPref.setValue("recentRestaurant", recentRestaurantGson)
        }
    }

    private fun setRecentLocation(){
        RecentPref.recentPref.setValue("lat",Location.lat.toString())
        RecentPref.recentPref.setValue("lng",Location.lng.toString())
        RecentPref.recentPref.setValue("locationName", Location.locationName)

    }
}


