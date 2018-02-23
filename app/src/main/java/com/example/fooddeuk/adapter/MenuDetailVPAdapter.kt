package com.example.fooddeuk.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.fooddeuk.R
import com.example.fooddeuk.model.menu.Menu
import com.squareup.picasso.Picasso


/**
 * Created by heo on 2018. 2. 22..
 */

class MenuDetailVPAdapter(var context: Context,  val menu: Menu) : PagerAdapter() {
    var layoutInflater: LayoutInflater
    var vpCount = 0

    init {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        vpCount=menu.picture.size
    }

    override fun getCount(): Int = vpCount

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if(position<menu.picture.size){
            val imageViewLayout = layoutInflater.inflate(R.layout.item_vp_menu_detail, container, false)
            Picasso.with(context).load(menu.picture[position]).fit().into(imageViewLayout.findViewById<ImageView>(R.id.layout_item_menu_detail_imageview))
            container.addView(imageViewLayout)
            return imageViewLayout
        }else{
            return ""
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}