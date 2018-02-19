package com.example.fooddeuk.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.example.fooddeuk.GlobalApplication
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Location
import com.example.fooddeuk.model.restaurant.Restaurant
import com.example.fooddeuk.network.HTTP
import com.example.fooddeuk.util.IntentUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.orhanobut.logger.Logger
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.activity_map.*
import org.parceler.Parcels


class MapActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var filter: String
    var maxDistance: Int = 0
    private lateinit var menuType: String
    lateinit var googleMap: GoogleMap
    var restaurantList: ArrayList<Restaurant>? = null
    val restaurantName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.restaurant_list_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initData()
        setSupportActionBar(tool_restaurant_map)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(true)
            it.title = Location.locationName
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            tool_restaurant_map.setNavigationOnClickListener { finish() }
        }


    }

    fun initData() {

        val query = intent.extras.getSerializable("MapData") as HashMap<String, String>
        menuType = query["foodType"]!!
        filter = query["filter"]!!
        maxDistance = query["maxDistance"]!!.toInt()
        HTTP.Single(GlobalApplication.httpService.getCurrentLocationRestaurant(queryMap(restaurantName))).bindToLifecycle(this)
                .subscribe({
                    if (it.status == "SUCCESS")
                        restaurantList = it.restaurants
                        drawRestListMarker()

                }, { it.printStackTrace() })

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

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap.apply {
            setOnMarkerClickListener(this@MapActivity)
            setOnMapClickListener { layout_description_box.visibility = View.GONE }
            setLatLngBoundsForCameraTarget(LatLngBounds(LatLng(35.0, 126.0), LatLng(38.0, 128.0)))
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(Location.lat, Location.lng), 14f))
            setMinZoomPreference(12.0f)
            setMaxZoomPreference(21.0f)
            if (ContextCompat.checkSelfPermission(this@MapActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                isMyLocationEnabled = true
            }
            uiSettings.isZoomControlsEnabled
        }


    }

    fun queryMap(restaurantMenuType: String): HashMap<String, String> {
        return HashMap<String, String>().apply {
            put("curLat", Location.lat.toString())
            put("curLng", Location.lng.toString())
            put("maxDistance", maxDistance.toString())
            put("foodType", restaurantMenuType)
            put("filter", filter)
            put("restaurantName", restaurantName)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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


    override fun onMarkerClick(marker: Marker?): Boolean {
        var tag = marker!!.tag as Int
        var restaurant = restaurantList!![tag - 1]
        Logger.d(restaurant.name)
        rest_map_name.text = restaurant.name
        Picasso.with(this).load(restaurant.picture).into(rest_map_picture)
        rest_map_desc.text = restaurant.description
        layout_description_box.visibility = View.VISIBLE
        layout_description_box.setOnClickListener({
            val restaurantParcel = Parcels.wrap<Restaurant>(restaurant)
            val extra = Bundle()
            extra.putParcelable("restaurant", restaurantParcel)
            IntentUtil.startActivity(this, DetailRestaurantActivity::class.java, extra)

        })
        return false
    }
}


