package com.example.fooddeuk.menu.listview.viewholder

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import com.example.fooddeuk.R
import com.example.fooddeuk.GlobalApplication.httpService
import com.example.fooddeuk.`object`.Util
import com.example.fooddeuk.menu.listview.MenuDetailViewPagerAdapter
import com.example.fooddeuk.menu.model.Menu
import com.example.fooddeuk.network.HTTP
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_menu_have_picture.view.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

/**
 * Created by heo on 2018. 1. 20..
 */

class DoubleOptionViewHolder(var context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
    //Layout

    var vpMenuDetail: ViewPager = itemView.vp_menu_detail
    var menuOrder: Button = itemView.menu_detail_order

    fun bind(menu: Menu) {
        val menuPrice = menu.price + "원"
        if (menu.rating.length == 1) {
            menu.rating += ".0"
        }
        HTTP.Single(httpService.getReview(menu.menu_id)).subscribe({
            if(it.success){
                val menuDetailViewPager = MenuDetailViewPagerAdapter(context, menu, it.result)
                vpMenuDetail.adapter=menuDetailViewPager
                OverScrollDecoratorHelper.setUpOverScroll(vpMenuDetail)
                itemView.menu_viewpager_indicator.setViewPager(vpMenuDetail)
            }
        },{it.printStackTrace()})

        Picasso.with(context).load(menu.picture[0]).fit().transform(CropCircleTransformation()).into(itemView.menu_master_picture)
        itemView.txt_menu_name.text = menu.name
        itemView.menu_master_price.text = menuPrice
        menuOrder.text = Util.stringFormat(context, R.string.menu_order,menuPrice)

    }
}
