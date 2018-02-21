package com.example.fooddeuk.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.fooddeuk.GlobalApplication
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.activity.LocationSettingByMapActivity
import com.example.fooddeuk.activity.MainActivity
import com.example.fooddeuk.activity.MapActivity
import com.example.fooddeuk.network.HTTP
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.CustomFilterDialog
import com.example.fooddeuk.util.StartActivity
import com.example.fooddeuk.util.SwipeViewPager
import com.flyco.tablayout.listener.OnTabSelectListener
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.fragment_near.*
import java.util.ArrayList
import kotlin.collections.HashMap


class NearFragment : Fragment() {
    private lateinit var locationSettingDialog: MaterialDialog
    private var filter = distance
    private var maxDistance = distance3km
    private var restaurantMenuType: String = "아무거나"
    private var restaurantName = ""
    private lateinit var customFilterDialog: CustomFilterDialog


    companion object {
        private const val distance = 0
        private const val rating = 1
        private const val discount = 2
        private const val dangol = 3
        private const val review =4
        private const val distance3km = 3000
        private const val distance6km = 6000
        private const val distance9km = 9000
    }

    /**
     * 음식 종류에 따른 식당목록리스트 가져오기
     */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_near, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setFilter()
        setLocation()

        setRestaurantListWithViewPager()
        btn_near_map.setOnClickListener({
            RxBus.intentPublish(RxBus.MapActivityData,queryMap(restaurantMenuType))
            StartActivity(MapActivity::class.java)
        })

    }

    override fun onResume() {
        btn_near_restaurant_search.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_search)?.apply {
            setColorFilter(ContextCompat.getColor(context!!, R.color.black), PorterDuff.Mode.SRC_ATOP)
        })

        txt_near_location_name.setText(Location.locationName)
        super.onResume()
    }

    private fun setFilter() {
        btn_near_filter.setOnClickListener { customFilterDialog.show() }
        val sortlist = ArrayList<String>().apply {
            add("정렬순")
            add("vertical")
            add(getString(R.string.filter_sort0))
            add(getString(R.string.filter_sort1))
            add(getString(R.string.filter_sort2))
            add(getString(R.string.filter_sort3))
            add(getString(R.string.filter_sort4))
        }

        val sortListener = { position: Int, contentTextView: TextView ->
            filter=when(position){
                distance-> distance
                rating-> rating
                discount-> discount
                dangol-> dangol
                review-> review
                else -> distance
            }

            Logger.d("filter"+filter)
            updateViewPager()
            customFilterDialog.hide()
        }

        val distanceList = ArrayList<String>().apply {
            add("거리순")
            add("horizontal")
            add(getString(R.string.filter_distance_3km))
            add(getString(R.string.filter_distance_6km))
            add(getString(R.string.filter_distance_9km))
        }

        val distanceListener = { position: Int, contentTextView: TextView ->
            maxDistance = when (position) {
                0 -> distance3km
                1 -> distance6km
                2 -> distance9km
                else-> distance3km
            }
            Logger.d("distance " +maxDistance)
            updateViewPager()
            customFilterDialog.hide()
        }

        customFilterDialog = CustomFilterDialog.Builder(context!!)
                .isClearText(true)
                .setFilter(sortlist, sortListener)
                .setFilter(distanceList,distanceListener)
                .SetClearCallback {
                    filter=0
                    maxDistance=distance3km
                    customFilterDialog.hide()
                }
                .build()
    }


    private fun setRestaurantListWithViewPager() {
        vp_rest_list.adapter = NearRestaurantStatePagerAdapter(childFragmentManager)
        //ParentViewPager
        val parentViewPager = (activity as MainActivity).findViewById<SwipeViewPager>(R.id.home_viewpager)
        parentViewPager.setIsSlide(true)
        tab_rest_list.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                when (position) {
                    0 -> restaurantMenuType = getString(R.string.restaurant_menu_type0)
                    1 -> restaurantMenuType = getString(R.string.restaurant_menu_type1)
                    2 -> restaurantMenuType = getString(R.string.restaurant_menu_type2)
                    3 -> restaurantMenuType = getString(R.string.restaurant_menu_type3)
                    4 -> restaurantMenuType = getString(R.string.restaurant_menu_type4)
                    5 -> restaurantMenuType = getString(R.string.restaurant_menu_type5)
                    6 -> restaurantMenuType = getString(R.string.restaurant_menu_type6)
                }
            }

            override fun onTabReselect(position: Int) {
                return
            }
        })

        tab_rest_list.setViewPager(vp_rest_list)
    }


    //위치 셋팅 다이아로그
    private fun setLocation() {
        txt_near_location_name.text = Location.locationName
        locationSettingDialog = MaterialDialog.Builder(activity!!).customView(R.layout.dialog_current_location, true).build()
        val dialogView = locationSettingDialog.view
        val locationReloadButton = dialogView.findViewById<Button>(R.id.btn_set_location_by_gps)
        val locationMapButton = dialogView.findViewById<Button>(R.id.btn_set_location_by_map)
        val locationCancel = dialogView.findViewById<TextView>(R.id.btn_location_cancel)

        val onClickListener = { v: View ->
            when (v.id) {
                R.id.txt_near_location_name -> locationSettingDialog.show()
                R.id.btn_set_location_by_gps -> {
                    Location.getLocation { lat, lng ->
                        Location.buzy = false
                        HTTP.Single(GlobalApplication.httpService.getLocationNameByNaver(lng.toString() + "," + lat.toString())).bindToLifecycle(this).subscribe({
                            txt_near_location_name.text = it.gudong
                            Location.locationName = it.gudong
                            locationSettingDialog.dismiss()
                        }, { it.printStackTrace() })
                    }
                }
                R.id.btn_set_location_by_map -> {
                    locationSettingDialog.dismiss()
                    StartActivity(LocationSettingByMapActivity::class.java)
                }
                R.id.btn_location_cancel -> locationSettingDialog.dismiss()
            }
        }

        txt_near_location_name!!.setOnClickListener(onClickListener)
        locationReloadButton.setOnClickListener(onClickListener)
        locationMapButton.setOnClickListener(onClickListener)
        locationCancel.setOnClickListener(onClickListener)
    }


    /**
     * 식당 리스트 뷰 Param 위치(locationItem) 최대거리(maxDistance) 식당메뉴타입(menuType)
     */

    fun queryMap(restaurantMenuType: String): HashMap<String, String> {
        return HashMap<String, String>().apply {
            put("curLat", Location.lat.toString())
            put("curLng", Location.lng.toString())
            put("maxDistance", maxDistance.toString())
            put("foodType", restaurantMenuType)
            put("filter", filter.toString())
            put("restaurantName", restaurantName)
        }
    }


    inner class NearRestaurantStatePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


        override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE

        override fun getCount(): Int = 7

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> NearRestaurantFragment.newInstance(queryMap(""))

                1 -> NearRestaurantFragment.newInstance(queryMap(getString(R.string.restaurant_menu_type1)))

                2 -> NearRestaurantFragment.newInstance(queryMap(getString(R.string.restaurant_menu_type2)))

                3 -> NearRestaurantFragment.newInstance(queryMap(getString(R.string.restaurant_menu_type3)))

                4 -> NearRestaurantFragment.newInstance(queryMap(getString(R.string.restaurant_menu_type4)))

                5 -> NearRestaurantFragment.newInstance(queryMap(getString(R.string.restaurant_menu_type5)))

                6 -> NearRestaurantFragment.newInstance(queryMap(getString(R.string.restaurant_menu_type6)))

                else -> NearRestaurantFragment.newInstance(queryMap(getString(R.string.restaurant_menu_type0)))
            }

        }


        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> getString(R.string.restaurant_menu_type0)
                1 -> getString(R.string.restaurant_menu_type1)
                2 -> getString(R.string.restaurant_menu_type2)
                3 -> getString(R.string.restaurant_menu_type3)
                4 -> getString(R.string.restaurant_menu_type4)
                5 -> getString(R.string.restaurant_menu_type5)
                6 -> getString(R.string.restaurant_menu_type6)
                else -> null
            }
        }
    }

    fun updateViewPager() {
        vp_rest_list.adapter?.notifyDataSetChanged()
    }


}
