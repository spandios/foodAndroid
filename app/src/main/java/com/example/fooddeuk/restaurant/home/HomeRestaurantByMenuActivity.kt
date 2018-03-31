package com.example.fooddeuk.restaurant.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.GlobalVariable

import com.example.fooddeuk.`object`.GlobalVariable.distance
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.custom.CustomFilterDialog
import com.example.fooddeuk.map.MapActivity
import com.example.fooddeuk.restaurant.detail.DetailRestaurantActivity
import com.example.fooddeuk.restaurant.home.presenter.HomeRestaurantContract
import com.example.fooddeuk.restaurant.home.presenter.HomeRestaurantPresenter
import com.example.fooddeuk.restaurant.list.RestaurantAdapter
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.*
import com.orhanobut.logger.Logger
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_home_restaurant.*


class HomeRestaurantByMenuActivity : AppCompatActivity(), View.OnClickListener, HomeRestaurantContract.View {
    private var isMenuCategoryOptionClicked = false
    private var isAddressOptionClicked = false
    private var menu_category = "" // Default == 아무거나
    private var filter = distance //Default Filter == Distance
    private var restaurantName = ""
    private var maxDistance = 9000
    private lateinit var presenter: HomeRestaurantPresenter
    private lateinit var adapter: RestaurantAdapter
    private lateinit var customFilterDialog: CustomFilterDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_restaurant)
        renderView()
        presenter = HomeRestaurantPresenter().apply { view = this@HomeRestaurantByMenuActivity }
        RxBus.intentSubscribe(RxBus.HomeRestaurantActivityData, this::class.java, Consumer {
            if (it is GlobalVariable.MENU) {
                when(it){
                    GlobalVariable.MENU.ANYTHING ->{
                        menu_category = ""
                        home_restaurant_menu_category.text = "#아무거나"
                        Logger.d("anything")
                        Logger.d(home_restaurant_menu_category.text.toString())
                    }
                    GlobalVariable.MENU.JAPAN ->{
                        menu_category = getString(R.string.restaurant_menu_type1)
                        home_restaurant_menu_category.text = "#일식"
                    }
                    GlobalVariable.MENU.CHICKEN ->{
                        menu_category = getString(R.string.restaurant_menu_type2)
                        home_restaurant_menu_category.text = "#치킨"
                    }
                    GlobalVariable.MENU.CHINESE ->{
                        menu_category = getString(R.string.restaurant_menu_type3)
                        home_restaurant_menu_category.text = "#중식"
                    }
                    GlobalVariable.MENU.KOREAN ->{
                        menu_category = getString(R.string.restaurant_menu_type4)
                        home_restaurant_menu_category.text = "#한식"
                    }
                    GlobalVariable.MENU.CAFE ->{
                        menu_category = getString(R.string.restaurant_menu_type5)
                        home_restaurant_menu_category.text = "#까페"
                    }
                    GlobalVariable.MENU.THAI ->{
                        menu_category = getString(R.string.restaurant_menu_type6)
                        home_restaurant_menu_category.text = "#타이"
                    }
                    GlobalVariable.MENU.FRANCHISE ->{
                        menu_category = getString(R.string.restaurant_menu_type7)
                        home_restaurant_menu_category.text = "#프랜차이즈"
                    }
                    GlobalVariable.MENU.DESSERT ->{
                        menu_category = getString(R.string.restaurant_menu_type8)
                        home_restaurant_menu_category.text = "#디저트"
                    }
                }
            }

            presenter.setRecyclerView(queryMap(menu_category))
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.intentUnregister(this::class.java)
    }

    override fun setAddressText(gudong: String) {
        justDong(gudong)
    }

    //Init First RecyclerView
    override fun setRestaurantRecyclerView(restaurantList: ArrayList<Restaurant>?) {
        Logger.d(restaurantList)
        restaurantList?.let {
            adapter = RestaurantAdapter(this, restaurantList).apply {
                restaurantItemClickListener = {
                    RxBus.intentPublish(RxBus.DetailRestaurantActivityData, it)
                    StartActivity(DetailRestaurantActivity::class.java)
                }
            }
            home_restaurant_list.setting(adapter, false, true, true)
        }?:restaurantNull()

        stopLoading()
    }

    override fun restaurantNull() {
        toast("매장이 없습니다.")
    }

    override fun errorAddress() {
        toast("error Address")
    }

    override fun errorRestaurant() {
        toast("error restaurant")
    }

    override fun updateRestaurant(restaurantList: ArrayList<Restaurant>?) {
        restaurantList?.let { adapter.updateRestaurant(it) } ?: adapter.updateRestaurant(null)
        stopLoading()
    }

    override fun startLoading() {
        home_restaurant_loading.visible()
    }

    override fun stopLoading() {
        home_restaurant_loading.gone()
    }

    fun queryMap(menuCategory: String): HashMap<String, String> {
        return HashMap<String, String>().apply {
            put("curLat", Location.lat.toString())
            put("curLng", Location.lng.toString())
            put("maxDistance", maxDistance.toString())
            put("foodType", menuCategory)
            put("filter", filter.toString())
            put("restaurantName", restaurantName)
        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.home_restaurant_back -> finish()

            R.id.home_restaurant_menu_category -> {
                isMenuCategoryOptionClicked = !isMenuCategoryOptionClicked

                if (isMenuCategoryOptionClicked) {
                    header_option_address.gone()

                    YoYo.with(Techniques.FadeInDown).duration(150).onStart {
                        header_option_menu_category.visible()
                        header_option.visible()
                    }.playOn(header_option)

                } else {
                    YoYo.with(Techniques.FadeOutUp).duration(150).onEnd {
                        header_option_menu_category.gone()
                        header_option.gone()
                    }.playOn(header_option)
                }
            }

            R.id.home_restaurant_address -> {
                isAddressOptionClicked = !isAddressOptionClicked


                if (isAddressOptionClicked) {
                    header_option_menu_category.gone()

                    YoYo.with(Techniques.FadeInDown).duration(150).onStart {
                        header_option_address.visible()
                        header_option.visible()
                    }.playOn(header_option)

                } else {
                    YoYo.with(Techniques.FadeOutUp).duration(150).onEnd {
                        header_option_address.gone()
                        header_option.gone()
                    }.playOn(header_option)

                }
            }


            R.id.home_restaurant_map -> {
                this.StatAcitivity(RxBus.MapActivityData, queryMap(menu_category), MapActivity::class.java)
            }


            R.id.home_restaurant_filter -> {
                customFilterDialog.show()
//                home_restaurant_filter_text.visible()
            }

        }

    }


    private fun renderView() {
        setClickListener()
        justDong(Location.locationName)
        setFilter()
    }


    private fun setFilter() {
        val sortList = java.util.ArrayList<String>().apply {
            add("정렬순")
            add("vertical")
            add(getString(R.string.filter_sort0))
            add(getString(R.string.filter_sort1))
            add(getString(R.string.filter_sort2))
            add(getString(R.string.filter_sort3))
            add(getString(R.string.filter_sort4))
        }

        val sortListener = { position: Int, contentTextView: TextView ->
            filter = when (position) {
                distance -> distance
                GlobalVariable.rating -> GlobalVariable.rating
                GlobalVariable.discount -> GlobalVariable.discount
                GlobalVariable.dangol -> GlobalVariable.dangol
                R.string.review -> R.string.review
                else -> distance
            }
            presenter.updateRestaurant(queryMap(menu_category))
            customFilterDialog.hide()
        }

        val distanceList = java.util.ArrayList<String>().apply {
            add("최대거리")
            add("horizontal")
            add(getString(R.string.filter_distance_3km))
            add(getString(R.string.filter_distance_6km))
            add(getString(R.string.filter_distance_9km))
        }

        val distanceListener = { position: Int, contentTextView: TextView ->
            maxDistance = when (position) {
                0 -> GlobalVariable.distance3km
                1 -> GlobalVariable.distance6km
                2 -> GlobalVariable.distance9km
                else -> GlobalVariable.distance3km
            }
            presenter.updateRestaurant(queryMap(menu_category))
            customFilterDialog.hide()
        }

        customFilterDialog = CustomFilterDialog.Builder(this)
                .isClearText(true)
                .setFilter(sortList, sortListener)
                .setFilter(distanceList, distanceListener)
                .SetClearCallback {
                    filter = 0
                    maxDistance = GlobalVariable.distance3km
                    customFilterDialog.hide()
                }
                .build()
    }


    private fun setClickListener() {
        home_restaurant_back.setOnClickListener(this)
        home_restaurant_menu_category.setOnClickListener(this)
        home_restaurant_address.setOnClickListener(this)
        home_restaurant_map.setOnClickListener(this)
        home_restaurant_filter.setOnClickListener(this)
        clickHeaderOptionForMenu()
        clickHeaderOptionForAddress()
    }


    private fun clickHeaderOptionForMenu() {
        val parentLayout = header_option_menu_category.getChildAt(0) as LinearLayout
        for (i in 0 until parentLayout.childCount) {
            val menuOption = parentLayout.getChildAt(i) as TextView
            menuOption.setOnClickListener {
                menuOption.let {
                    home_restaurant_menu_category.text = it.text
                    menu_category = it.tag as String
                    presenter.updateRestaurantByMenuCategory(queryMap(menu_category))

                }
                header_option_menu_category.gone()
                header_option.gone()
            }
        }
    }

    private fun clickHeaderOptionForAddress() {
        val parentLayout = header_option_address.getChildAt(0) as LinearLayout
        for (i in 0 until parentLayout.childCount) {
            val addressOption = parentLayout.getChildAt(i) as TextView
            //address
            addressOption.setOnClickListener {
                when (i) {
                    0 -> {
                        Location.buzy = true
                        Location.getLocation { lat, lng, locationName ->
                            Location.buzy = false
                            presenter.updateRestaurantByCurrentLocation(queryMap(menu_category), locationName)
                        }
                    }
                    1 -> {
                        //TODO 지도
                    }
                    2 -> {
                        //TODO 지하철
                    }
                    3 -> {
                        //TODO 대학교
                    }
                }
                header_option_menu_category.gone()
                header_option.gone()
            }
        }
    }

    private fun justDong(gudong: String) {
        var index = gudong.indexOf("구")
        home_restaurant_address.text = "#" + gudong.substring(index + 2)
    }


}
