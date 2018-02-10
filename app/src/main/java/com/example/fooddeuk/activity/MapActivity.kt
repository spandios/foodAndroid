package com.example.fooddeuk.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.example.fooddeuk.R
import com.example.fooddeuk.model.restaurant.Restaurant
import com.example.fooddeuk.model.restaurant.RestaurantResponse
import com.example.fooddeuk.network.RestaurantService
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
import kotlinx.android.synthetic.main.activity_map.*
import org.parceler.Parcels
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    lateinit var filter: String
    var maxDistance: Int? = null
    lateinit var menuType: String
    private var lat: Double? = null
    var lng: Double? = null
    lateinit var googleMap: GoogleMap
    var restaurantList: ArrayList<Restaurant>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.restaurant_list_map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.setOnMarkerClickListener(this)
        googleMap.setOnMapClickListener {   rest_description_box.visibility=View.GONE}
        val extra = intent.extras
        filter = extra.getString("filter")
        maxDistance = extra.getInt("maxDistance")
        menuType = extra.getString("menuType")
        lat = extra.getDouble("lat")
        lng = extra.getDouble("lng")

        googleMap.setLatLngBoundsForCameraTarget(LatLngBounds(LatLng(35.0, 126.0), LatLng(38.0, 128.0)))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(getLatLng()))
        googleMap.setMinZoomPreference(12.0f)
        googleMap.setMaxZoomPreference(21.0f)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        }
        googleMap.uiSettings.isMyLocationButtonEnabled
        googleMap.uiSettings.isZoomControlsEnabled


        RestaurantService.getCurrentLocationRestaurant(lat!!, lng!!, maxDistance!!, menuType, filter, "").enqueue(object:Callback<RestaurantResponse>{
            override fun onResponse(call: Call<RestaurantResponse>?, response: Response<RestaurantResponse>?) {
                if(response!!.isSuccessful){
                    if(response.body()!!.status==("SUCCESS")){
                        restaurantList=response.body()!!.restaurants
                        drawRestListMarker()
                    }
                }
            }
            override fun onFailure(call: Call<RestaurantResponse>?, t: Throwable?) {
                t!!.printStackTrace()
            }
        })

    }

    private fun getLatLng(): LatLng = LatLng(lat!!, lng!!)

    private fun drawRestListMarker() {
        googleMap.clear()
        for(i in 0 until restaurantList!!.size){
            val marker = googleMap.addMarker(MarkerOptions()
                    .position(LatLng(restaurantList!![i].lat, restaurantList!![i].lng))
                    .title(restaurantList!![i].name))
            marker.tag = i+1
        }
    }



    override fun onMarkerClick(marker: Marker?): Boolean {
        var tag = marker!!.tag as Int
        var restaurant = restaurantList!![tag-1]
        Logger.d(restaurant.name)
        rest_map_name.text = restaurant.name
        Picasso.with(this).load(restaurant.picture).into(rest_map_picture)
        rest_map_desc.text=restaurant.description
        rest_description_box.visibility=View.VISIBLE
        rest_description_box.setOnClickListener({
            val restaurantParcel = Parcels.wrap<Restaurant>(restaurant)
            val extra = Bundle()
            extra.putParcelable("restaurant", restaurantParcel)
            IntentUtil.startActivity(this, DetailRestaurantActivity::class.java, extra)

        })
        return false
    }
}


