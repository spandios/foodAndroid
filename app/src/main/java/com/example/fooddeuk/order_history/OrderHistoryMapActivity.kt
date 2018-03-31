package com.example.fooddeuk.order_history

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.fooddeuk.R
import com.example.fooddeuk.network.HTTP.httpService
import com.example.fooddeuk.network.HTTP.singleAsync
import com.example.fooddeuk.restaurant.detail.DetailRestaurantActivity
import com.example.fooddeuk.restaurant.model.Restaurant
import com.example.fooddeuk.rx.RxBus
import com.example.fooddeuk.util.StartActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.orhanobut.logger.Logger
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_order_history_map.*


/**
 * Created by heo on 2018. 1. 27..
 */

class OrderHistoryMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var _id: String
    private lateinit var restaurant: Restaurant

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history_map)
        order_list_map_back.setOnClickListener { finish() }
        (supportFragmentManager.findFragmentById(R.id.order_history_map) as SupportMapFragment).apply { getMapAsync(this@OrderHistoryMapActivity) }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.intentUnregister(this@OrderHistoryMapActivity.javaClass)
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.apply {
            RxBus.intentSubscribe(RxBus.OneRestaurantMapData, this@OrderHistoryMapActivity.javaClass, Consumer {
                if (it is String) {
                    Logger.d(it)
                    _id = it
                }

            httpService.getRestaurantById(_id).compose(singleAsync()).subscribe({
                if (it.status == "SUCCESS") {
                    Logger.d(it.restaurants[0])
                    restaurant = it.restaurants[0]
                    order_list_map_title.text = restaurant.name
                    val latLng = LatLng(restaurant.lat, restaurant.lng)
                    val restName = restaurant.name
                    val address = restaurant.address

                    setLatLngBoundsForCameraTarget(LatLngBounds(LatLng(35.0, 126.0), LatLng(38.0, 128.0)))
                    moveCamera(CameraUpdateFactory.newLatLng(latLng))

                    val marker = addMarker(MarkerOptions().position(latLng).title(restName).snippet(address)).apply {
                        setOnMarkerClickListener {
                            RxBus.publish(RxBus.DetailRestaurantActivityData, restaurant)
                            StartActivity(DetailRestaurantActivity::class.java)
                            true
                        }
                    }

                    setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                        override fun getInfoWindow(arg0: Marker): View? = null

                        override fun getInfoContents(marker: Marker): View {

                            val title = TextView(this@OrderHistoryMapActivity).apply {
                                setTextColor(Color.BLACK)
                                setTextSize(TypedValue.COMPLEX_UNIT_PX, this@OrderHistoryMapActivity.resources.getDimension(R.dimen.order_list_map_title))
                                gravity = Gravity.CENTER
                                setTypeface(null, Typeface.BOLD)
                                text = restName
                            }

                            val snippet = TextView(this@OrderHistoryMapActivity).apply {
                                setTextColor(Color.GRAY)
                                text = address
                                setTextSize(TypedValue.COMPLEX_UNIT_PX, this@OrderHistoryMapActivity.resources.getDimension(R.dimen.order_list_map_content))
                            }

                            return LinearLayout(this@OrderHistoryMapActivity).apply {
                                orientation = LinearLayout.VERTICAL
                                addView(title)
                                addView(snippet)
                            }
                        }
                    })

                    marker.showInfoWindow()

                    // Move the camera instantly to location with a zoom of 15.
                    moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
                    // Zoom in, animating the camera.
                    animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)
                    setMinZoomPreference(12.0f)
                    setMaxZoomPreference(21.0f)
                    if (ContextCompat.checkSelfPermission(this@OrderHistoryMapActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        isMyLocationEnabled = true
                        uiSettings.isZoomControlsEnabled = true
                    }
                }

            }, {
                it.printStackTrace()
            })

        })
        }


    }
}