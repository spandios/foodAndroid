package com.example.fooddeuk.map

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
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
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.RealmUtil
import com.example.fooddeuk.util.StartActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import com.orhanobut.logger.Logger
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_map.*


class MapActivity : BaseActivity(), OnMapReadyCallback {
    private lateinit var filter: String
    private var maxDistance: Int = 0
    private lateinit var menuType: String
    private var restaurantList: ArrayList<Restaurant>? = null
    private val restaurantName: String = ""
    private lateinit var googleMap: GoogleMap
    private lateinit var mClusterManager: ClusterManager<RestaurantClusterModel>
    private var toolbarVisible = true
    private var filterVisible = true

    private lateinit var mBottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetView: View
    private lateinit var imageView: ImageView
    private lateinit var name: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var reviewCnt: TextView
    private lateinit var description: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val behavior = BottomSheetBehavior.from(map_bottom_sheet)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.restaurant_list_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        RxBus.intentSubscribe(RxBus.MapActivityData, this::class.java, Consumer { queryMap ->
            with(queryMap as HashMap<String, String>) {
                menuType = get("foodType")!!
                filter = get("filter")!!
                maxDistance = 4000
                initData()
            }
        })
        setBottomSheetDialog()
        setSupportActionBar(tool_restaurant_map)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            tool_restaurant_map.setNavigationOnClickListener { finish() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.intentUnregister(this::class.java)
    }

    private fun initData() {
        //restaurant
        lat = Location.lat
        lng = Location.lng
        getRestaurant()

        //filter text
        txt_restaurant_map_address.text = Location.locationName
//        txt_restaurant_map_menutype.text = menuType
//        txt_restaurant_map_filter.text = when (filter) {
//            "0" -> getString(R.string.filter_sort0)
//            "1" -> getString(R.string.filter_sort1)
//            "2" -> getString(R.string.filter_sort2)
//            "3" -> getString(R.string.filter_sort3)
//            "4" -> getString(R.string.filter_sort4)
//            else -> getString(R.string.filter_sort0)
//        }

    }

    private fun getRestaurant() {
        httpService.getCurrentLocationRestaurant(queryMap()).compose(singleAsync()).bindToLifecycle(this)
                .subscribe({
                    if (it.status == "SUCCESS")
                        if (it.restaurants.isNotEmpty()) {
                            restaurantList = it.restaurants
                        }
                    drawRestListMarker()
                }, { it.printStackTrace() })
    }

    //TODO BottomSheetDialog With List
    private fun setBottomSheetDialog() {



        mBottomSheetDialog = BottomSheetDialog(this)
        bottomSheetView = this.layoutInflater.inflate(R.layout.dialog_select_restaurant_box, null).apply {
            imageView = findViewById(R.id.img_restaurant_map_box)
            name = findViewById(R.id.txt_restaurant_map_box_name)
            ratingBar = findViewById(R.id.star_restaurant_map_box)
            reviewCnt = findViewById(R.id.txt_restaurant_map_box_reviewcnt)
            description = findViewById(R.id.txt_restaurant_map_box_description)
        }
        mBottomSheetDialog.setContentView(bottomSheetView)
    }

    private fun drawRestListMarker() {
        mClusterManager.clearItems()
        restaurantList?.let {
            for (i in 0 until it.size) {
                val item = RestaurantClusterModel(it[i])
                mClusterManager.addItem(item)
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap.apply {
            uiSettings.isZoomControlsEnabled
            mClusterManager = ClusterManager(this@MapActivity, googleMap)
            setLatLngBoundsForCameraTarget(LatLngBounds(LatLng(35.0, 126.0), LatLng(38.0, 128.0)))
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(Location.lat, Location.lng), 14f)) //현재위치
            setMinZoomPreference(13f)
            setMaxZoomPreference(18f)
            if (ContextCompat.checkSelfPermission(this@MapActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                isMyLocationEnabled = true
            }

            //Toolbar Visible
            setOnMapClickListener {
                if (toolbarVisible) {
                    toolbarVisible = false
                    tool_restaurant_map.animate()
                            .translationY(-tool_restaurant_map.height.toFloat())
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator?) {
                                    tool_restaurant_map.visibility = View.INVISIBLE
                                }
                            })
                } else {
                    toolbarVisible = true
                    YoYo.with(Techniques.FadeInDown)
                            .duration(300)
                            .playOn(tool_restaurant_map)
                    tool_restaurant_map.visibility = View.VISIBLE
                }
            }

            setOnCameraIdleListener(mClusterManager)
            setOnCameraMoveListener {
                if (!mapParent.isTouched) {
                    val latLng = googleMap.cameraPosition.target
                    lat = latLng.latitude
                    lng = latLng.longitude
                    getRestaurant()
                    Location.getLocationName(latLng.latitude, latLng.longitude) { locationName ->
                        txt_restaurant_map_address.text = locationName
                    }
                }
            }

            mClusterManager.setOnClusterClickListener({
                it.items.forEach {
                    Logger.d(it.name)
                }
                return@setOnClusterClickListener true
            })

            //마커클릭
            setOnMarkerClickListener(mClusterManager)
            mClusterManager.setOnClusterItemClickListener { restaurantItem ->
                val restaurant = Restaurant(restaurantItem)
                bottomSheetView.setOnClickListener {
                    RealmUtil.insertData(restaurant)
                    StartActivity(RxBus.DetailRestaurantActivityData, restaurant, DetailRestaurantActivity::class.java)
                    mBottomSheetDialog.dismiss()
                }
                Picasso.with(this@MapActivity).load(restaurant.picture[0]).fit().into(bottomSheetView.findViewById<ImageView>(R.id.img_restaurant_map_box))
                name.text = restaurant.name
                ratingBar.rating = restaurant.rating
                reviewCnt.text = String.format(getString(R.string.reviewcnt), restaurant.reviewCnt.toString())
                description.text = restaurant.description
                mBottomSheetDialog.show()
                true
            }

            getRestaurant()
        }
    }


    private fun queryMap(): HashMap<String, String> {
        return HashMap<String, String>().apply {
            put("curLat", lat.toString())
            put("curLng", lng.toString())
            put("maxDistance", maxDistance.toString())
            if (menuType == "아무거나") {
                put("foodType", "")
            } else {
                put("foodType", menuType)
            }
            put("filter", filter)
            put("restaurantName", restaurantName)
        }
    }


}


