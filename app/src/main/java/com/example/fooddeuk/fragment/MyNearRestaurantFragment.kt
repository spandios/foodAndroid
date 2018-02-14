package com.example.fooddeuk.fragment

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.fooddeuk.R
import com.example.fooddeuk.activity.LocationSettingByMapActivity
import com.example.fooddeuk.activity.MainActivity
import com.example.fooddeuk.activity.MapActivity
import com.example.fooddeuk.location.Location
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.DialogUtil
import com.example.fooddeuk.util.StartActivity
import com.example.fooddeuk.util.SwipeViewPager
import com.jakewharton.rxbinding2.view.RxView
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.fragment_near.*
import java.util.*


class MyNearRestaurantFragment : Fragment() {
    lateinit var locationSettingDialog: MaterialDialog
    var filter = "distance"
    var maxDistance = 1000
    private lateinit var restaurantMenuType: String

    /**
     * 음식 종류에 따른 식당목록리스트 가져오기
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        //현재 위치 정보 db에서 가져옴
        super.onCreate(savedInstanceState)
        restaurantMenuType = context!!.resources.getString(R.string.restaurant_menu_type1)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_near, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        txt_near_current_location.text = Location.locationName
        setLocationSettingDialog()
        setRestaurantMenuTablayout()

        RxView.clicks(btgn_near_filter)
                .bindToLifecycle(this)
                .subscribe({
                    DialogUtil.setRestaurantFilter(activity as Activity, object : DialogUtil.FilterCallback{
                override fun filterResult(distance: Int, filterValue: String) {
                    if (distance > 0) {
                        maxDistance = distance
                    } else {
                        filter = filterValue
                    }
                    updateViewPager()
                }
            })})


    }

    fun setRestaurantMenuTablayout(){
        val parentViewPager = (activity as MainActivity).findViewById<SwipeViewPager>(R.id.home_viewpager)
        rest_list_view_pager.adapter = NearRestaurantStatePagerAdapter(childFragmentManager)
        rest_list_view_pager.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {parentViewPager.setIsSlide(false) }
                MotionEvent.ACTION_UP -> {parentViewPager.setIsSlide(true)}
            }
            false
        }
        tab_rest_list.setViewPager(rest_list_view_pager)
    }

    override fun onResume() {
        super.onResume()
        //지도로 수동 위치를 지정하면 그 위치를 현재위치로 설정한뒤 근처 식당 재검색
        RxBus.subscribe { o ->
            if ((o as HashMap<*, *>)[getString(R.string.rx_location_setting)] as Boolean) {
                //                locationItems = RealmUtil.findDataAll(LocationItem.class).get(0);
                //                Logger.d(locationItems);
                //                currentLocationTextView.setText(locationItems.getLocationName());
                //                getCurLocationRestaurant();
            }
        }

    }

    fun goToNearMapActivity() {
        StartActivity(MapActivity::class.java,Bundle().apply {
            putDouble("lat", Location.lat)
            putDouble("lng", Location.lng)
            putString("filter", filter)
            putInt("maxDistance", maxDistance)
            putString("menuType", restaurantMenuType)
        })
    }

    //위치 셋팅 다이아로그
    private fun setLocationSettingDialog() {
        locationSettingDialog = MaterialDialog.Builder(activity!!).customView(R.layout.dialog_current_location, true).build()
        
        val dialogView = locationSettingDialog.view
        val locationReloadButton = dialogView.findViewById<Button>(R.id.btn_set_location_by_gps)
        val locationMapButton = dialogView.findViewById<Button>(R.id.btn_set_location_by_map)
        val locationCancel = dialogView.findViewById<TextView>(R.id.btn_location_cancel)
        
        val onClickListener = { v : View ->
            when (v.id) {
                R.id.txt_near_current_location -> locationSettingDialog.show()
                R.id.btn_set_location_by_gps -> { 
                    locationSettingDialog.dismiss() 
                }
                R.id.btn_set_location_by_map -> {
                    locationSettingDialog.dismiss()
                    StartActivity(LocationSettingByMapActivity::class.java)
                }
                R.id.btn_location_cancel -> locationSettingDialog.dismiss()
            }
        }
        
        txt_near_current_location!!.setOnClickListener(onClickListener)
        locationReloadButton.setOnClickListener(onClickListener)
        locationMapButton.setOnClickListener(onClickListener)
        locationCancel.setOnClickListener(onClickListener)
    }


    /**
     * 식당 리스트 뷰 Param 위치(locationItem) 최대거리(maxDistance) 식당메뉴타입(menuType)
     */


    inner class NearRestaurantStatePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


        override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE

        override fun getCount(): Int = 8

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> Rest_list_fragment.newInstance(Location.lat, Location.lng, maxDistance, getString(R.string.restaurant_menu_type1), filter, "")

                1 -> Rest_list_fragment.newInstance(Location.lat, Location.lng, maxDistance, "치킨", filter, "")

                2 -> Rest_list_fragment.newInstance(Location.lat, Location.lng, maxDistance, "", filter, "")

                3 -> Rest_list_fragment.newInstance(Location.lat, Location.lng, maxDistance, "중식", filter, "")

                else -> Rest_list_fragment.newInstance(Location.lat, Location.lng, maxDistance, "일식", filter, "")
            }

        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return getString(R.string.restaurant_menu_type1)
                1 -> return getString(R.string.restaurant_menu_type2)
                2 -> return getString(R.string.restaurant_menu_type3)
                3 -> return getString(R.string.restaurant_menu_type4)
                4 -> return getString(R.string.restaurant_menu_type5)
                5 -> return getString(R.string.restaurant_menu_type6)
                6 -> return "HTTP"
                7 -> return "HTTP"

                else -> return null
            }
        }
    }

    fun updateViewPager() {
        rest_list_view_pager.adapter?.notifyDataSetChanged()
    }

}
