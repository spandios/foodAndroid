package com.example.fooddeuk.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.Window
import com.example.fooddeuk.R
import com.example.fooddeuk.fragment.*
import com.example.fooddeuk.staticval.StaticVal
import com.example.fooddeuk.util.NetworkUtil
import com.example.fooddeuk.util.SettingActivityUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private var homeFragmentFlag = true
    private lateinit var fragments: Array<Fragment>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        fragments= arrayOf(HomeFragment(),DanGolFragment(),CurLocationRestaurantFragment(),OrderHistoryFragment(),UserFragment())
        setNavigation()
        setViewPager()



    }

    override fun onResume() {
        super.onResume()
        if (intent.extras != null) {
            if (intent.extras!!.getBoolean("isOrder")) {
//                replaceFragmentToActivity(homeContent, OrderHistoryFragment())
                homeFragmentFlag = false
            }
        }
    }


    private fun setNavigation(){
        navigation.enableShiftingMode(false)
        navigation.enableItemShiftingMode(false)
    }

    private fun setViewPager(){
        home_viewpager.adapter=MyPagerAdapter(supportFragmentManager)
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
        if (requestCode == StaticVal.gpsSettingActivityRequestCode) {
            if (!NetworkUtil.isGpsPossible(this)) {
                SettingActivityUtil.settingGPS(this)
            }
        }
    }


    //TODO FRAGMENT PAGER ADAPTER
   private inner class MyPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
       // Returns total number of pages
       override fun getCount(): Int = fragments.size

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

