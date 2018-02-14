package com.example.fooddeuk.util

import android.app.Activity
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.afollestad.materialdialogs.MaterialDialog
import com.example.fooddeuk.R

/**
 * Created by heojuyeong on 2017. 10. 8..
 */

class DialogUtil {
    internal var activity: Activity? = null


    interface FilterCallback {
        fun filterResult(distance: Int, filterValue: String)
    }

    companion object {
        fun setRestaurantFilter(activity: Activity, filterCallback: FilterCallback) {
            val filterDialog = MaterialDialog.Builder(activity).customView(R.layout.dialog_filter, true).build()
            val view = filterDialog.view
            val rest_filter_exit = view.findViewById<ImageView>(R.id.rest_filter_exit)
            val rest_filter_sort_distance = view.findViewById<TextView>(R.id.rest_filter_sort_distance)
            val rest_filter_sort_order = view.findViewById<TextView>(R.id.rest_filter_sort_order)
            val rest_filter_sort_rating = view.findViewById<TextView>(R.id.rest_filter_sort_rating)
            val rest_filter_sort_review = view.findViewById<TextView>(R.id.rest_filter_sort_review)
            val rest_filter_sort_dangol = view.findViewById<TextView>(R.id.rest_filter_sort_dangol)
            val rest_filter_sort_discount = view.findViewById<TextView>(R.id.rest_filter_sort_discount)
            val distance2km = view.findViewById<TextView>(R.id.rest_filter_distance2km)
            val distance4km = view.findViewById<TextView>(R.id.rest_filter_distance4km)
            val distance6km = view.findViewById<TextView>(R.id.rest_filter_distance6km)

            rest_filter_sort_distance.isSelected = true
            rest_filter_sort_distance.setTypeface(rest_filter_sort_distance.typeface, Typeface.BOLD)
            //TODO FILTER
            val onClickListener = View.OnClickListener { v ->
                //todo SORT
                when (v.id) {
                    R.id.rest_filter_exit -> {
                    }
                    R.id.rest_filter_sort_distance -> filterCallback.filterResult(0, "distance")
                    R.id.rest_filter_sort_order -> {
                    }
                    R.id.rest_filter_sort_rating -> filterCallback.filterResult(0, "rating")
                    R.id.rest_filter_sort_review -> {
                    }
                    R.id.rest_filter_sort_dangol -> {
                    }
                    R.id.rest_filter_sort_discount -> {
                    }
                    R.id.rest_filter_distance2km -> filterCallback.filterResult(8000, "")
                    R.id.rest_filter_distance4km -> filterCallback.filterResult(10000, "")
                    R.id.rest_filter_distance6km -> filterCallback.filterResult(10000, "")
                }//                        filterCallback.filterResult(0, "order"); is default
                //                        filterCallback.filterResult(0, "review");
                //                        filterCallback.filterResult(0, "dangol");
                //                        filterCallback.filterResult(0, "discount");
                filterDialog.hide()
            }
            rest_filter_exit.setOnClickListener(onClickListener)
            distance2km.setOnClickListener(onClickListener)
            distance4km.setOnClickListener(onClickListener)
            distance6km.setOnClickListener(onClickListener)
            rest_filter_sort_distance.setOnClickListener(onClickListener)
            rest_filter_sort_rating.setOnClickListener(onClickListener)
            filterDialog.show()
        }
    }
}
