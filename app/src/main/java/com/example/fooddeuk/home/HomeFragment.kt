package com.example.fooddeuk.home


import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.GlobalVariable
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.custom.CustomFilterDialog
import com.example.fooddeuk.custom.ImageVPAdapter
import com.example.fooddeuk.map.LocationSettingByMapActivity
import com.example.fooddeuk.map.MapActivity
import com.example.fooddeuk.pick.PickAdapter
import com.example.fooddeuk.restaurant.home.HomeRestaurantByMenuActivity
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home_content.*


//, AppBarLayout.OnOffsetChangedListener
class HomeFragment : Fragment(), NestedScrollView.OnScrollChangeListener, HomeContract.View, View.OnClickListener {


    private lateinit var locationSettingDialog: CustomFilterDialog
    private lateinit var homePresenter: HomePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        renderView()
    }

    override fun onResume() {
        super.onResume()
        setAddressText(Location.locationName)
        homePresenter = HomePresenter().apply { view=this@HomeFragment }
        homePresenter.setAddress()
        homePresenter.setHomeEvent()
        homePresenter.getDangolRestaurant()
    }

    override fun onPause() {
        super.onPause()
        homePresenter.clear()
    }

    override fun setDangolRestaurant(dangolRestaurants: ArrayList<Restaurant>) {
        dangol_rest_recycle.layoutManager = GridLayoutManager(context, 2)
        dangol_rest_recycle.adapter = PickAdapter(context!!, dangolRestaurants)
    }

    override fun dangolError() {
        toast("dangol error")
    }


    private fun renderView() {
        setToolbar()
        with(home_scroll) {
            setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
            setOnScrollChangeListener(this@HomeFragment)
        }
        setLocationDialog()
        setClickHomeMenu()
        setClickHomeLocation()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.home_menu_anything -> context?.StartActivity(RxBus.HomeRestaurantActivityData, GlobalVariable.MENU.ANYTHING, HomeRestaurantByMenuActivity::class.java)
            R.id.home_menu_japan -> context?.StartActivity(RxBus.HomeRestaurantActivityData, GlobalVariable.MENU.JAPAN, HomeRestaurantByMenuActivity::class.java)
            R.id.home_menu_chicken -> context?.StartActivity(RxBus.HomeRestaurantActivityData, GlobalVariable.MENU.CHICKEN, HomeRestaurantByMenuActivity::class.java)
            R.id.home_menu_chinese -> context?.StartActivity(RxBus.HomeRestaurantActivityData, GlobalVariable.MENU.CHINESE, HomeRestaurantByMenuActivity::class.java)
            R.id.home_menu_korean -> context?.StartActivity(RxBus.HomeRestaurantActivityData, GlobalVariable.MENU.KOREAN, HomeRestaurantByMenuActivity::class.java)
            R.id.home_menu_cafe -> context?.StartActivity(RxBus.HomeRestaurantActivityData, GlobalVariable.MENU.CAFE, HomeRestaurantByMenuActivity::class.java)
            R.id.home_menu_thai -> context?.StartActivity(RxBus.HomeRestaurantActivityData, GlobalVariable.MENU.THAI, HomeRestaurantByMenuActivity::class.java)
            R.id.home_menu_franchise -> context?.StartActivity(RxBus.HomeRestaurantActivityData, GlobalVariable.MENU.FRANCHISE, HomeRestaurantByMenuActivity::class.java)
            R.id.home_menu_dessert -> context?.StartActivity(RxBus.HomeRestaurantActivityData, GlobalVariable.MENU.DESSERT, HomeRestaurantByMenuActivity::class.java)

            R.id.home_location_map -> context?.StartActivity(RxBus.MapActivityData,queryMap(),MapActivity::class.java)
//            R.id.home_location_metro ->context?.StartActivity(RxBus.MapActivityData,"",MapActivity::class.java)
//            R.id.home_location_univ ->context?.StartActivity(RxBus.MapActivityData,"",MapActivity::class.java)

        }
    }

    fun queryMap(): HashMap<String, String> {
        return HashMap<String, String>().apply {
            put("curLat", Location.lat.toString())
            put("curLng", Location.lng.toString())
            put("foodType", "")
            put("filter", "")
            put("restaurantName", "")
        }
    }

    private fun setClickHomeMenu() {
        home_menu_anything.setOnClickListener(this)
        home_menu_japan.setOnClickListener(this)
        home_menu_chicken.setOnClickListener(this)
        home_menu_chinese.setOnClickListener(this)
        home_menu_korean.setOnClickListener(this)
        home_menu_cafe.setOnClickListener(this)
        home_menu_thai.setOnClickListener(this)
        home_menu_franchise.setOnClickListener(this)
        home_menu_dessert.setOnClickListener(this)

    }

    private fun setClickHomeLocation(){
        home_location_map.setOnClickListener(this)
        home_location_metro.setOnClickListener(this)
        home_location_univ.setOnClickListener(this)
    }


    private fun setToolbar() {
        with(activity as AppCompatActivity) {
            setSupportActionBar(header)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        header.background.alpha = 0
        //검색 이미지 -> 색깔 흰색
        header_search.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_search)?.apply {
            setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        })
    }


    override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        home_event_viewpager.y = (scrollY / 2).toFloat()
        val alpha = (Math.min(1f, scrollY.toFloat() / (250.toPx - header.height))) * 255
        when (alpha) {
            in 0..70 -> {
                txt_home_location_name.setTextColor(Color.WHITE)
                header_search.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            }
            in 70..100 -> header_search.setColorFilter(R.color.white_1, PorterDuff.Mode.SRC_ATOP)

            in 100..150 -> {
                txt_home_location_name.setTextColor(ContextCompat.getColor(context!!, R.color.white_2))
                header_search.setColorFilter(R.color.white_2, PorterDuff.Mode.SRC_ATOP)
            }
            in 150..255 -> {
                txt_home_location_name.setTextColor(Color.BLACK)
                header_search.setColorFilter(R.color.black, PorterDuff.Mode.SRC_ATOP)
            }
        }
        header.background.alpha = alpha.toInt()
    }


    override fun setHomeEventAdapter(eventPictureList: HomeEventPictureResponse) {

        home_event_viewpager_indicator.setViewPager(home_event_viewpager.apply { adapter = ImageVPAdapter(context, eventPictureList.eventPictureList) })
        home_event_viewpager.startAutoScroll(4500)
        home_event_viewpager.interval = 4500
    }

    override fun setAddressText(locationName: String) {
        with(txt_home_location_name) {
            text = locationName
            setOnClickListener({ locationSettingDialog.show() })
        }
    }


    private fun setLocationDialog() {
        val dialogContent = arrayListOf("현재 위치 재설정", "vertical", "현재 위치 재검색", "지도에서 설정")
        locationSettingDialog = CustomFilterDialog.Builder(context!!).isClearText(false).contentTypeFace(Typeface.DEFAULT_BOLD).isFirstSelectColor(false).contentGravity(Gravity.CENTER).setFilter(dialogContent
                , { position, _ ->
            when (position) {
                0 -> {
                    //현재위치에서 재 검색
                    startLoading()
                    Location.buzy = true
                    Location.getLocation { _, _, locationName ->
                        Location.buzy = false
                        stopLoading()
                        homePresenter.setLocationName(locationName)
                    }
                }
                1 -> {
                    StartActivity(LocationSettingByMapActivity::class.java)
                }
            }
            locationSettingDialog.dismiss()
        }).build()
    }

    override fun showAddressError() {
        toast("주소 정보를 얻을 수 없습니다.")
    }

    override fun showEventError() {
        toast("이벤트를 불러 올 수 없습니다")
    }


}



