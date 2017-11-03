package com.example.fooddeuk.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.example.fooddeuk.R
import com.example.fooddeuk.http.RestaurantService
import com.example.fooddeuk.model.restaurant.RestaurantItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.orhanobut.logger.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapActivity : BaseActivity(), OnMapReadyCallback {
    lateinit var filter: String
    var maxDistance: Int? = null
    lateinit var menuType: String
    private var lat: Double? = null
    var lng: Double? = null
    lateinit var googleMap: GoogleMap
    var restaurantList: ArrayList<RestaurantItem.Restaurant>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.restaurant_list_map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
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

        MenuListAsnc().execute()


    }

    private fun getLatLng(): LatLng = LatLng(lat!!, lng!!)

    private fun drawRestListMarker() {
        googleMap.clear()

        for ((i, restaurant) in restaurantList!!.withIndex()) {
            Logger.d(restaurant.name + restaurant.latlng[1] + restaurant.latlng[0])
            if (restaurant.latlng[1] >= 1 || restaurant.latlng[0] >= 1) {

                // Get the icon for the feature
                val marker = googleMap.addMarker(MarkerOptions()
                        .position(LatLng(restaurant.latlng[1], restaurant.latlng[0]))
                        .title(restaurant.name))
                marker.tag = i
            }
        }
    }

    internal inner class MenuListAsnc : AsyncTask<Void, Int, Void>() {


        override fun onPreExecute() {
            super.onPreExecute()


        }

        override fun doInBackground(vararg voids: Void): Void? {
            RestaurantService.getCurrentLocationRestaurant(lat!!, lng!!, maxDistance!!, menuType, filter, "").enqueue(object : Callback<RestaurantItem> {
                override fun onResponse(call: Call<RestaurantItem>, response: Response<RestaurantItem>) {
                    if (response.isSuccessful) {
                        restaurantList = response.body()!!.restaurants
                        drawRestListMarker()

//                        restaurantAdapter.setRestaurantItemClickListener { restaurant ->
//                            val restaurantParcel = Parcels.wrap<Restaurant>(restaurant)
//                            val extra = Bundle()
//                            extra.putParcelable("restaurant", restaurantParcel)
//                            IntentUtil.startActivity(getActivity(), DetailRestaurantActivity::class.java, extra)
//                        }
                    }
                }


                override fun onFailure(call: Call<RestaurantItem>, t: Throwable) {
                    Logger.d(t)
                    Toast.makeText(this@MapActivity, "네트워크 연결에 실패했습니다", Toast.LENGTH_LONG).show()
                }
            })
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)


        }


    }

}


