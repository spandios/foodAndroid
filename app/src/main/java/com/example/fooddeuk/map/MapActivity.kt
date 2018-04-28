package com.example.fooddeuk.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.activity.BaseActivity
import com.example.fooddeuk.network.HTTP.httpService
import com.example.fooddeuk.network.HTTP.singleAsync
import com.example.fooddeuk.restaurant.detail.DetailRestaurantActivity
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.restaurant.repository.RestaurantRepository
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_map.*


class MapActivity : BaseActivity(), View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnCameraMoveListener, ClusterManager.OnClusterItemClickListener<RestaurantClusterModel>, ClusterManager.OnClusterClickListener<RestaurantClusterModel> {
    private lateinit var filter: String
    private var maxDistance: Int = 0
    private lateinit var foodType: String
    private var restaurantList: ArrayList<Restaurant>? = null
    private val restaurantName: String = ""
    private lateinit var googleMap: GoogleMap
    private lateinit var mClusterManager: ClusterManager<RestaurantClusterModel>

    private var isToolbarVisible = true
    private var isBottomSheetWithListExpand = false
    private var isMenuCategoryOptionExpand = false
    private var isAddressOptionExpand = false
    private var isClusterBottomSheetExpand = false

    private lateinit var mBottomSheetDialog: BottomSheetDialog
    private lateinit var mBottomSheetWithList: BottomSheetBehavior<View>
    private lateinit var bottomSheetView: View
    private lateinit var imageView: ImageView
    private lateinit var name: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var reviewCnt: TextView
    private lateinit var discount: TextView
    private lateinit var feature: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        header_restaurant_map.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        mBottomSheetWithList = BottomSheetBehavior.from(map_bottom_sheet)


        val mapFragment = supportFragmentManager.findFragmentById(R.id.restaurant_list_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        RxBus.intentSubscribe(RxBus.MapActivityData, this::class.java, Consumer { queryMap ->
            with(queryMap as HashMap<String, String>) {
                foodType = get("foodType")!!
                filter = get("filter")!!
                maxDistance = 4000
                init()
            }
        })
        setBottomSheetDialog()
        setBottomSheetWithList()

    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.intentUnregister(this::class.java)
    }

    private fun init() {
        //restaurant
        map_back.setOnClickListener { finish() }
        lat = Location.lat
        lng = Location.lng
        RestaurantRepository.getRestaurantAndCached(queryMap())?.subscribe({
            restaurantList = it.restaurants
            bottom_all_restaurant_size.text = "총" + restaurantList?.size.toString() + "개"
            map_bottom_restaurant_all_list.setting(MapBottomRestaurantAdapter(this, restaurantList!!), overscroll = true, hasFixed = true)

            drawRestaurantCluster()
        }, {
            it.printStackTrace()
        })
        if (foodType == "") {
            map_restaurant_menu_category.text = "#아무거나"
        } else {
            map_restaurant_menu_category.text = "#" + foodType
        }

        justDong(Location.locationName)

        header_restaurant_map.setOnClickListener(this)
        map_restaurant_menu_category.setOnClickListener(this)
        map_restaurant_address.setOnClickListener(this)
        map_all_bottom_sheet.setOnClickListener(this)
        bottom_cluster_close.setOnClickListener(this)
        clickHeaderOptionForMenu()
    }


    override fun onClick(v: View) {
        when (v.id) {

            R.id.header_restaurant_map -> {

            }


            R.id.bottom_cluster_close -> {
                hiddenBottomSheetList()
            }

            R.id.map_all_bottom_sheet -> {

                //펼침
                if (!isBottomSheetWithListExpand) {
                    bottom_down.visible()
                    bottom_up.gone()
                    layout_bottom_all_restaurant.visible()
                    bottom_all_restaurant_introduce.gone()
                    map_all_bottom_sheet.layoutParams = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT).apply { setMargins(0, 46.toPx, 0, 0) }
                    map_bottom_restaurant_all_list.visible()
                } else {
                    //접음
                    bottom_down.gone()
                    bottom_up.visible()
                    layout_bottom_all_restaurant.gone()
                    bottom_all_restaurant_introduce.visible()
                    bottom_all_restaurant_size.text = "총" + restaurantList?.size.toString() + "개"
                    map_all_bottom_sheet.layoutParams = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT).apply {
                        gravity=Gravity.BOTTOM
                    }

                    map_bottom_restaurant_all_list.gone()
                }

                isBottomSheetWithListExpand = !isBottomSheetWithListExpand
            }

            R.id.map_restaurant_menu_category -> {

                isAddressOptionExpand = false
                header_option_address.gone()

                //접음
                if (isMenuCategoryOptionExpand) {
                    YoYo.with(Techniques.FadeOutUp).duration(150).onEnd {
                        header_option_menu_category.gone()
                        header_option.gone()
                    }.playOn(header_option)
                } else {
                    YoYo.with(Techniques.FadeInDown).duration(150).onStart {
                        header_option_menu_category.visible()
                        header_option.visible()
                    }.playOn(header_option)
                }

                isMenuCategoryOptionExpand = !isMenuCategoryOptionExpand
            }

            R.id.map_restaurant_address -> {
                isMenuCategoryOptionExpand = false
                header_option_menu_category.gone()

                //접음
                if (isAddressOptionExpand) {
                    YoYo.with(Techniques.FadeOutUp).duration(150).onEnd {
                        header_option_address.gone()
                        header_option.gone()
                    }.playOn(header_option)
                } else {
                    YoYo.with(Techniques.FadeInDown).duration(150).onStart {
                        header_option_address.visible()
                        header_option.visible()
                    }.playOn(header_option)


                }
                isAddressOptionExpand = !isAddressOptionExpand
            }
        }
    }

    private fun clickHeaderOptionForMenu() {
        val parentLayout = header_option_menu_category.getChildAt(0) as LinearLayout
        for (i in 0 until parentLayout.childCount) {
            val menuOption = parentLayout.getChildAt(i) as TextView
            menuOption.setOnClickListener {
                isMenuCategoryOptionExpand = false
                menuOption.let {
                    map_restaurant_menu_category.text = it.text
                    foodType = it.tag as String
                    getRestaurant()
                }
                header_option_menu_category.gone()
                header_option.gone()
            }
        }
    }

    private fun getRestaurant() {
        httpService.getCurrentLocationRestaurant(queryMap()).compose(singleAsync()).bindToLifecycle(this)
                .subscribe({
                    if (it.status == "SUCCESS") {
                        if (it.restaurants != null && it.restaurants.isNotEmpty()) {
                            restaurantList = it.restaurants
                            bottom_all_restaurant_size.text = restaurantList?.size.toString() + "개"
                            map_bottom_restaurant_all_list.setting(MapBottomRestaurantAdapter(this, restaurantList!!), overscroll = true)

                        } else {
                            restaurantList = null
                        }
                    } else {
                        restaurantList = null
                    }

                    drawRestaurantCluster()
                }, { it.printStackTrace() })
    }

    private fun drawRestaurantCluster() {
        mClusterManager.clearItems()
        restaurantList?.let {
            for (i in 0 until it.size) {
                val item = RestaurantClusterModel(it[i])
                mClusterManager.addItem(item)
            }
        }
        mClusterManager.cluster()
    }


    private fun setBottomSheetWithList() {
        map_bottom_sheet.setOnClickListener {
            if (isBottomSheetWithListExpand) {
                mBottomSheetWithList.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                mBottomSheetWithList.state = BottomSheetBehavior.STATE_EXPANDED
            }
            isBottomSheetWithListExpand = !isBottomSheetWithListExpand
        }
    }

    private fun setBottomSheetDialog() {
        mBottomSheetDialog = BottomSheetDialog(this)
        bottomSheetView = this.layoutInflater.inflate(R.layout.dialog_select_restaurant_box, null).apply {
            imageView = findViewById(R.id.img_restaurant_map_box)
            name = findViewById(R.id.txt_restaurant_map_box_name)
            ratingBar = findViewById(R.id.star_restaurant_map_box)
            reviewCnt = findViewById(R.id.txt_restaurant_map_box_reviewcnt)
            discount = findViewById(R.id.txt_restaurant_map_discount)
            feature = findViewById(R.id.txt_restaurant_map_bottom_feature)
        }
        mBottomSheetDialog.setContentView(bottomSheetView)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap.apply {
            uiSettings.isZoomControlsEnabled
            mClusterManager = ClusterManager(this@MapActivity, googleMap)
            setLatLngBoundsForCameraTarget(LatLngBounds(LatLng(35.0, 126.0), LatLng(38.0, 128.0)))
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(Location.lat, Location.lng), 14f)) //현재위치
            setMinZoomPreference(14f)
            setMaxZoomPreference(18f)
            if (ContextCompat.checkSelfPermission(this@MapActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                isMyLocationEnabled = true
            }
            setOnMapClickListener(this@MapActivity)
            setOnCameraMoveListener(this@MapActivity)
            setOnCameraIdleListener(mClusterManager)
            setOnMarkerClickListener(mClusterManager)
            mClusterManager.setOnClusterItemClickListener(this@MapActivity)
            mClusterManager.setOnClusterClickListener(this@MapActivity)

        }
    }

    //사용자가 최종적으로 지도의 위치를 확정할 때
    override fun onCameraMove() {
        if (!mapParent.isTouched) {
            val latLng = googleMap.cameraPosition.target
            lat = latLng.latitude
            lng = latLng.longitude
            getRestaurant()
            Location.getLocationName(latLng.latitude, latLng.longitude) { locationName ->
                justDong(locationName)
            }
        }
    }

    //툴바와 바텀시트 visible click listener
    override fun onMapClick(latLng: LatLng) {

        if (isClusterBottomSheetExpand) {
            hiddenBottomSheetList()
            return
        }

        if (isMenuCategoryOptionExpand) {
            isAddressOptionExpand = false
            YoYo.with(Techniques.FadeOutUp).duration(150).onEnd {
                header_option_menu_category.gone()
                header_option.gone()
            }.playOn(header_option)
            return
        }

        if (isAddressOptionExpand) {
            isAddressOptionExpand = false
            YoYo.with(Techniques.FadeOutUp).duration(150).onEnd {
                header_option_address.gone()
                header_option.gone()
            }.playOn(header_option)
            return
        }

        if (isToolbarVisible) {
            isToolbarVisible = false
            YoYo.with(Techniques.FadeOutUp)
                    .duration(300)
                    .playOn(header_restaurant_map)
            YoYo.with(Techniques.FadeOutDown)
                    .duration(300)
                    .playOn(map_all_bottom_sheet)
        } else {
            isToolbarVisible = true
            YoYo.with(Techniques.FadeInDown)
                    .duration(300)
                    .playOn(header_restaurant_map)

            YoYo.with(Techniques.FadeInUp)
                    .duration(300)
                    .playOn(map_all_bottom_sheet)
        }
    }

    //마커 클릭 리스너
    override fun onClusterItemClick(restaurantClusterModel: RestaurantClusterModel): Boolean {
        if (isClusterBottomSheetExpand) {
            isClusterBottomSheetExpand = false
            mBottomSheetWithList.state = BottomSheetBehavior.STATE_HIDDEN
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(restaurantClusterModel.lat, restaurantClusterModel.lng)), 300, object : GoogleMap.CancelableCallback {
            override fun onFinish() {
                val restaurant = Restaurant(restaurantClusterModel)
                bottomSheetView.setOnClickListener {
                    RealmUtil.insertData(restaurant)
                    StartActivity(RxBus.DetailRestaurantActivityData, restaurant, DetailRestaurantActivity::class.java)
                    mBottomSheetDialog.dismiss()
                }
                Picasso.with(this@MapActivity).load(restaurant.picture[0]).fit().into(bottomSheetView.findViewById<ImageView>(R.id.img_restaurant_map_box))
                name.text = restaurant.name
                ratingBar.rating = restaurant.rating
                reviewCnt.text = String.format(getString(R.string.reviewcnt), restaurant.reviewCnt.toString())

                mBottomSheetDialog.show()
            }

            override fun onCancel() {

            }
        })
        return true
    }

    override fun onClusterClick(clusterItems: Cluster<RestaurantClusterModel>?): Boolean {
        isClusterBottomSheetExpand = true
        val restaurants = ArrayList<Restaurant>()
        clusterItems?.items?.forEach {
            restaurants.add(Restaurant(it))
        }
        bottom_cluster_restaurant_size.text = restaurants.size.toString()
        rv_cluster.setting(MapBottomRestaurantAdapter(this, restaurants), overscroll = true, hasFixed = true)
        mBottomSheetWithList.state = BottomSheetBehavior.STATE_EXPANDED
        return true
    }

    private fun hiddenBottomSheetList() {
        isClusterBottomSheetExpand = false
        mBottomSheetWithList.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun queryMap(): HashMap<String, String> {
        return HashMap<String, String>().apply {
            put("curLat", lat.toString())
            put("curLng", lng.toString())
            put("maxDistance", maxDistance.toString())
            if (foodType == "아무거나") {
                put("foodType", "")
            } else {
                put("foodType", foodType)
            }
            put("filter", filter)
            put("restaurantName", restaurantName)
        }
    }

    private fun justDong(gudong: String) {
        var index = gudong.indexOf("구")
        map_restaurant_address.text = "#" + gudong.substring(index + 2)
    }

}


