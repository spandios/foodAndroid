package com.example.fooddeuk.activity

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.GlobalApplication
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.network.HTTP
import com.example.fooddeuk.restaurant.detail.DetailRestaurantActivity
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.rx.RxBus.intentSubscribe
import com.example.fooddeuk.util.StartActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_map.*


class MapActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var filter: String
    private var maxDistance: Int = 0
    private lateinit var menuType: String
    private var restaurantList: ArrayList<Restaurant>? = null
    private val restaurantName: String = ""
    private lateinit var googleMap: GoogleMap

    private var toolbarVisible = true
    private lateinit var mBottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheet: View
    private lateinit var imageView: ImageView
    private lateinit var name: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var reviewCnt: TextView
    private lateinit var description: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.restaurant_list_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        intentSubscribe(RxBus.MapActivityData, this::class.java, Consumer { queryMap ->
            with(queryMap as HashMap<String, String>) {
                menuType = get("foodType")!!
                filter = get("filter")!!
                maxDistance = get("maxDistance")!!.toInt()
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


    private fun initData() {
        //restaurant
        HTTP.Single(GlobalApplication.httpService.getCurrentLocationRestaurant(queryMap())).bindToLifecycle(this)
                .subscribe({
                    if (it.status == "SUCCESS")
                        restaurantList = it.restaurants
                    drawRestListMarker()

                }, { it.printStackTrace() })

        //filter text
        txt_restaurant_map_address.text = Location.locationName
        txt_restaurant_map_menutype.text = menuType
        txt_restaurant_map_filter.text = when (filter) {
            "0" -> getString(R.string.filter_sort0)
            "1" -> getString(R.string.filter_sort1)
            "2" -> getString(R.string.filter_sort2)
            "3" -> getString(R.string.filter_sort3)
            "4" -> getString(R.string.filter_sort4)
            else -> getString(R.string.filter_sort0)
        }

        txt_restaurarnt_map_maxdistance.text = when (maxDistance) {
            3000 -> getString(R.string.filter_distance_3km)
            6000 -> getString(R.string.filter_distance_6km)
            9000 -> getString(R.string.filter_distance_9km)
            else -> ""
        }
    }

    private fun setBottomSheetDialog() {
        mBottomSheetDialog = BottomSheetDialog(this)
        bottomSheet = this.layoutInflater.inflate(R.layout.dialog_select_restaurant_box, null).apply {
            imageView= findViewById(R.id.img_restaurant_map_box)
            name = findViewById(R.id.txt_restaurant_map_box_name)
            ratingBar = findViewById(R.id.star_restaurant_map_box)
            reviewCnt = findViewById(R.id.txt_restaurant_map_box_reviewcnt)
            description = findViewById(R.id.txt_restaurant_map_box_description)
        }
        mBottomSheetDialog.setContentView(bottomSheet)

    }

    private fun drawRestListMarker() {
        googleMap.clear()
        for (i in 0 until restaurantList!!.size) {
            val marker = googleMap.addMarker(MarkerOptions()
                    .position(LatLng(restaurantList!![i].lat, restaurantList!![i].lng))
                    .title(restaurantList!![i].name))
            marker.tag = i + 1
        }
    }

    //마커 클릭
    override fun onMarkerClick(marker: Marker?): Boolean {
        //TODO position title MArker
        val tag = marker!!.tag as Int
        val restaurant = restaurantList!![tag - 1]

        bottomSheet.setOnClickListener {
            RxBus.intentPublish(RxBus.DetailRestaurantActivityData,restaurant)
            StartActivity(DetailRestaurantActivity::class.java) }
        Picasso.with(this).load(restaurant.picture).fit().into(bottomSheet.findViewById<ImageView>(R.id.img_restaurant_map_box))
        name.text = restaurant.name
        ratingBar.rating = restaurant.rating
        reviewCnt.text = String.format(getString(R.string.reviewcnt), restaurant.reviewCnt.toString())
        description.text = restaurant.description
        mBottomSheetDialog.show()
        return false
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap.apply {
            setOnMarkerClickListener(this@MapActivity)
            setLatLngBoundsForCameraTarget(LatLngBounds(LatLng(35.0, 126.0), LatLng(38.0, 128.0)))
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(Location.lat, Location.lng), 14f))
            setMinZoomPreference(12.0f)
            setMaxZoomPreference(21.0f)
            if (ContextCompat.checkSelfPermission(this@MapActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                isMyLocationEnabled = true
            }
            setOnMapClickListener {
                if (toolbarVisible) {
                    toolbarVisible = false
                    tool_restaurant_map.animate()
                            .translationY(-tool_restaurant_map.getHeight().toFloat())
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

            uiSettings.isZoomControlsEnabled
            setOnCameraIdleListener {
                val latLng = googleMap.cameraPosition.target
                Location.getLocationName(latLng.latitude, latLng.longitude) { locationName ->
                    txt_restaurant_map_address.text = locationName

                }
            }
        }
    }


    private fun queryMap(): HashMap<String, String> {
        return HashMap<String, String>().apply {
            put("curLat", Location.lat.toString())
            put("curLng", Location.lng.toString())
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


    override fun onDestroy() {
        super.onDestroy()
        RxBus.intentUnregister(this::class.java)
    }
}


