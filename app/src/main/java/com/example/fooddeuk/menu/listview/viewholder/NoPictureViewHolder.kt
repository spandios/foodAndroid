package com.example.fooddeuk.menu.listview.viewholder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Util
import com.example.fooddeuk.menu.listview.MenuDetailViewPagerAdapter
import com.example.fooddeuk.menu.model.Menu
import com.example.fooddeuk.network.HTTP.httpService
import com.example.fooddeuk.network.HTTP.singleAsync
import kotlinx.android.synthetic.main.item_menu_have_picture.view.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

/**
 * Created by heo on 2018. 1. 20..
 */

class NoPictureViewHolder(var context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(menu: Menu) {
        val menuPrice = menu.price + "원"
        if (menu.rating.length == 1) { menu.rating += ".0" }
        httpService.getReview(menu.menu_id).compose(singleAsync()).subscribe({
            if(it.success){
                val menuDetailViewPager = MenuDetailViewPagerAdapter(context, menu, it.result,noPicture = true)
                itemView.vp_menu_detail.adapter=menuDetailViewPager
                OverScrollDecoratorHelper.setUpOverScroll(itemView.vp_menu_detail)
                itemView.menu_viewpager_indicator.setViewPager(itemView.vp_menu_detail)
            }
        },{it.printStackTrace()})
        itemView.txt_menu_name.text = menu.name
        itemView.menu_master_price.text = menuPrice
        itemView.menu_detail_order.text = Util.StringFormat(context, R.string.menu_order,menuPrice)
    }
}
