package com.example.fooddeuk.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.fooddeuk.R
import com.squareup.picasso.Picasso

/**
 * Created by heo on 2018. 2. 20..
 */

class MapBottomSheetVPAdapter(val context: Context, private val imageList : ArrayList<String>) : PagerAdapter() {
    private var layoutInflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val parentView = layoutInflater.inflate(R.layout.item_restaurant_map_box_image_viewpager, container, false)
        val imageView = parentView.findViewById<ImageView>(R.id.img_restaurant_map_box)
        Picasso.with(context).load(imageList[position]).fit().into(imageView)
        container.addView(parentView)

        return parentView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return imageList.size
    }



}
