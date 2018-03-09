package com.example.fooddeuk.restaurant.near

import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.custom.CustomFilterDialog
import com.example.fooddeuk.map.LocationSettingByMapActivity
import com.example.fooddeuk.map.MapActivity
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.*
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_near.*
import java.util.ArrayList
import kotlin.collections.HashMap


class NearRestaurantParentFragment : Fragment(), NearRestaurantContract.View {
    private lateinit var locationSettingDialog: CustomFilterDialog
    private var filter = distance
    private var maxDistance = distance3km
    private var restaurantMenuType: String = "아무거나"
    private var restaurantName = ""
    private lateinit var customFilterDialog: CustomFilterDialog
    private lateinit var nearRestaurantPresenter: NearRestaurantPresenter


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
        renderView()
        setRestaurantListWithViewPager()
    }

    override fun onResume() {
        super.onResume()
        nearRestaurantPresenter= NearRestaurantPresenter().apply {
            view=this@NearRestaurantParentFragment
        }
        btn_near_restaurant_search.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_search)?.apply {
            setColorFilter(ContextCompat.getColor(context!!, R.color.black), PorterDuff.Mode.SRC_ATOP)
        })

        setAddressText(Location.locationName)
    }

    override fun onPause() {
        super.onPause()
        nearRestaurantPresenter.clear()
    }

    private fun renderView(){
        setFilter()
        setLocationDialog()
        btn_near_map.setOnClickListener({
            RxBus.intentPublish(RxBus.MapActivityData,queryMap(restaurantMenuType))
            StartActivity(MapActivity::class.java)
        })
    }

    private fun setFilter() {
        btn_near_filter.setOnClickListener { customFilterDialog.show() }
        val sortList = ArrayList<String>().apply {
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
                distance -> distance
                rating -> rating
                discount -> discount
                dangol -> dangol
                review -> review
                else -> distance
            }

            Logger.d("filter"+filter)
            nearRestaurantPresenter.refreshRestaurant()
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
            nearRestaurantPresenter.refreshRestaurant()
            customFilterDialog.hide()
        }

        customFilterDialog = CustomFilterDialog.Builder(context!!)
                .isClearText(true)
                .setFilter(sortList, sortListener)
                .setFilter(distanceList,distanceListener)
                .SetClearCallback {
                    filter=0
                    maxDistance= distance3km
                    customFilterDialog.hide()
                }
                .build()
    }


    private fun setRestaurantListWithViewPager() {
        vp_rest_list.adapter = NearRestaurantStatePagerAdapter(childFragmentManager)
        tab_rest_list.setViewPager(vp_rest_list)
//        tab_rest_list.setOnTabSelectListener(object : OnTabSelectListener {
//            override fun onTabSelect(position: Int) {
//                when (position) {
//                    0 -> restaurantMenuType = getString(R.string.restaurant_menu_type0)
//                    1 -> restaurantMenuType = getString(R.string.restaurant_menu_type1)
//                    2 -> restaurantMenuType = getString(R.string.restaurant_menu_type2)
//                    3 -> restaurantMenuType = getString(R.string.restaurant_menu_type3)
//                    4 -> restaurantMenuType = getString(R.string.restaurant_menu_type4)
//                    5 -> restaurantMenuType = getString(R.string.restaurant_menu_type5)
//                    6 -> restaurantMenuType = getString(R.string.restaurant_menu_type6)
//                }
//            }
//
//            override fun onTabReselect(position: Int) {
//                return
//            }
//        })


    }


    //위치 셋팅 다이아로그
    private fun setLocationDialog() {
        txt_near_location_name.setOnClickListener{locationSettingDialog.show()}
        val dialogContent = arrayListOf("현재 위치 재설정", "vertical", "현재 위치 재검색", "지도에서 설정")
        locationSettingDialog = CustomFilterDialog.Builder(context!!).isClearText(false).contentTypeFace(Typeface.DEFAULT_BOLD).isFirstSelectColor(false).contentGravity(Gravity.CENTER).setFilter(dialogContent
                , { position, _ ->
            when (position) {
                0 -> {
                    //현재위치에서 재 검색
                    startLoading()
                    Location.buzy = true
                    Location.getLocation { lat, lng ->
                        Location.buzy = false
                        stopLoading()
                        nearRestaurantPresenter.getLocation(lat, lng)
                        nearRestaurantPresenter.refreshRestaurant()
                    }
                }
                1 -> {
                    StartActivity(LocationSettingByMapActivity::class.java)
                }
            }
            locationSettingDialog.dismiss()
        }).build()
    }

    override fun setAddressText(gudong: String) {
        txt_near_location_name.text=gudong
    }

    override fun showAddressError() {
        toast("주소를 가져올수 없습니다.")
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
                0 -> NearRestaurantListFragment.newInstance(queryMap(""))

                1 -> NearRestaurantListFragment.newInstance(queryMap(getString(R.string.restaurant_menu_type1)))

                2 -> NearRestaurantListFragment.newInstance(queryMap(getString(R.string.restaurant_menu_type2)))

                3 -> NearRestaurantListFragment.newInstance(queryMap(getString(R.string.restaurant_menu_type3)))

                4 -> NearRestaurantListFragment.newInstance(queryMap(getString(R.string.restaurant_menu_type4)))

                5 -> NearRestaurantListFragment.newInstance(queryMap(getString(R.string.restaurant_menu_type5)))

                6 -> NearRestaurantListFragment.newInstance(queryMap(getString(R.string.restaurant_menu_type6)))

                else -> NearRestaurantListFragment.newInstance(queryMap(getString(R.string.restaurant_menu_type0)))
            }

        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)

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

    override fun updateViewPager() {
        vp_rest_list.adapter?.notifyDataSetChanged()
    }

}
