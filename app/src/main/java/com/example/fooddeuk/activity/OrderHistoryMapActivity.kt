package com.example.fooddeuk.activity

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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_order_history_map.*


/**
 * Created by heo on 2018. 1. 27..
 */

class OrderHistoryMapActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history_map)
        order_list_map_title.text=intent.getStringExtra("rest_name")
        (supportFragmentManager.findFragmentById(R.id.order_history_map) as SupportMapFragment).apply {getMapAsync(this@OrderHistoryMapActivity) }
    }

    override fun onClick(p0: View?) {
        finish()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.apply {
            val latLng=intent.extras["latlng"] as LatLng
            val restName =intent.getStringExtra("rest_name")
            val address = intent.getStringExtra("address")
            setLatLngBoundsForCameraTarget(LatLngBounds(LatLng(35.0, 126.0), LatLng(38.0, 128.0)))
            moveCamera(CameraUpdateFactory.newLatLng(latLng))

            val Marker  =  addMarker(MarkerOptions().position(latLng).title(restName).snippet(address))
            setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                override fun getInfoWindow(arg0: Marker): View? {
                    return null
                }
                override fun getInfoContents(marker: Marker): View {

                    val info = LinearLayout(this@OrderHistoryMapActivity)
                    info.orientation = LinearLayout.VERTICAL

                    val title = TextView(this@OrderHistoryMapActivity)
                    title.setTextColor(Color.BLACK)
                    title.setTextSize(TypedValue.COMPLEX_UNIT_PX,this@OrderHistoryMapActivity.resources.getDimension(R.dimen.order_list_map_title))
                    title.gravity = Gravity.CENTER
                    title.setTypeface(null, Typeface.BOLD)
                    title.setText(restName)

                    val snippet = TextView(this@OrderHistoryMapActivity)
                    snippet.setTextColor(Color.GRAY)
                    snippet.setText(address)
                    snippet.setTextSize(TypedValue.COMPLEX_UNIT_PX,this@OrderHistoryMapActivity.resources.getDimension(R.dimen.order_list_map_content))
                    info.addView(title)
                    info.addView(snippet)
                    return info
                }
            })
            Marker.showInfoWindow()

            // Move the camera instantly to location with a zoom of 15.
            moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
            // Zoom in, animating the camera.
            animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)
            setMinZoomPreference(12.0f)
            setMaxZoomPreference(21.0f)
            if (ContextCompat.checkSelfPermission(this@OrderHistoryMapActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                isMyLocationEnabled = true
                uiSettings.isZoomControlsEnabled=true
            }


        }






    }
}