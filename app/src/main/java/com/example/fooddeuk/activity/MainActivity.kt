package com.example.fooddeuk.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.fooddeuk.R
import com.example.fooddeuk.cart.CartFragment
import com.example.fooddeuk.home.HomeFragment
import com.example.fooddeuk.pick.PickFragment
import com.example.fooddeuk.restaurant.near.NearRestaurantParentFragment
import com.example.fooddeuk.user.UserFragment
import com.example.fooddeuk.util.NetworkUtil
import com.example.fooddeuk.util.SettingActivityUtil
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : BaseActivity() {
    private var homeFragmentFlag = true
    private var fragments: ArrayList<Fragment> = ArrayList()
    lateinit var fragmentPager : FragmentStatePagerAdapter

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
    }


    override fun onResume() {
        super.onResume()
        if (intent.extras != null) {

        }
    }

    private fun setFragment(){
        fragments.add(HomeFragment())
        fragments.add(PickFragment())
        fragments.add(NearRestaurantParentFragment())
        fragments.add(CartFragment())
        fragments.add(UserFragment())
    }
    private fun setNavigation(){
        navigation.enableShiftingMode(false)
        navigation.enableItemShiftingMode(false)
    }

    private fun setViewPager(){
        fragmentPager=MyPagerAdapter(supportFragmentManager)
        home_viewpager.adapter=fragmentPager
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
            if (!NetworkUtil.isGpsPossible(this)) {
                SettingActivityUtil.settingGPS(this)
            }
        }
    }


    //TODO FRAGMENT PAGER ADAPTER
   private inner class MyPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
       // Returns total number of pages
       override fun getCount(): Int = fragments.size

        override fun getItemPosition(`object`: Any): Int {

            if(`object` is CartFragment){
                `object`.setView()
            }
            return super.getItemPosition(`object`)
        }

       // Returns the fragment to display for that page
        override fun getItem(position: Int): Fragment? = when (position) {

           0 -> fragments[0]
           1 -> fragments[1]
           2 -> fragments[2]
           3 -> fragments[3]
           4 -> fragments[4]
           else -> null
       }
    }



}


