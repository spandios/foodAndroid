package com.example.fooddeuk.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.Window
import com.example.fooddeuk.R
import com.example.fooddeuk.fragment.DanGolFragment
import com.example.fooddeuk.fragment.HomeFragment
import com.example.fooddeuk.fragment.OrderHistoryFragment
import com.example.fooddeuk.staticval.StaticVal
import com.example.fooddeuk.util.NetworkUtil
import com.example.fooddeuk.util.SettingActivityUtil
import com.example.fooddeuk.util.replaceFragmentToActivity
import com.facebook.login.LoginFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private var homeFragmentFlag = true
    private val homeContent = R.id.homeContent
    lateinit var fragment: Fragment
    private lateinit var navigationListener : BottomNavigationView.OnNavigationItemSelectedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        replaceFragmentToActivity(homeContent, HomeFragment())
        setNavigation()
    }

    override fun onResume() {
        super.onResume()
        if (intent.extras != null) {
            if (intent.extras!!.getBoolean("isOrder")) {
                replaceFragmentToActivity(homeContent, OrderHistoryFragment())
                homeFragmentFlag = false
            }
        }


    }


    private fun setNavigation(){
        navigation.enableShiftingMode(false)
        navigation.enableItemShiftingMode(false)
        navigationListener=BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    homeFragmentFlag = true
                    fragment = HomeFragment()
                }
                R.id.navigation_dangol -> {
                    homeFragmentFlag = false
                    fragment = DanGolFragment()

                }
                R.id.navigation_cart -> {
                    homeFragmentFlag = false
                    fragment = OrderHistoryFragment()

                }
                R.id.navigation_user -> {
                    homeFragmentFlag = false
                    fragment = LoginFragment()
                }
            }
            replaceFragmentToActivity(homeContent, fragment)
            true
        }

        navigation.onNavigationItemSelectedListener = navigationListener
    }



    override fun onBackPressed() {
        if (homeFragmentFlag) {
            //Finish Activity
            super.onBackPressed()
        } else {
            homeFragmentFlag = true
            replaceFragmentToActivity(homeContent, HomeFragment())
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
}


