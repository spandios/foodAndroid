package com.example.fooddeuk.menu.listview.viewholder

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.example.fooddeuk.R
import com.example.fooddeuk.`object`.Util
import com.example.fooddeuk.menu.listview.MenuDetailViewPagerAdapter
import com.example.fooddeuk.menu.model.Menu
import com.example.fooddeuk.network.HTTP.httpService
import com.example.fooddeuk.network.HTTP.singleAsync
import com.example.fooddeuk.util.WrapPager
import com.example.fooddeuk.util.gone
import com.example.fooddeuk.util.visible
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_menu_have_picture.view.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

/**
 * Created by heo on 2018. 1. 20..
 */


class HavePictureMenuHolder(var context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(menu: Menu) {
        itemView.review_all.setOnClickListener {
            Toast.makeText(context, "toast", Toast.LENGTH_SHORT).show()
        }
        val menuPrice = menu.price + "원"
        if (menu.rating.length == 1) {
            menu.rating += ".0"
        }
        httpService.getReview(menu.menu_id).compose(singleAsync()).subscribe({
            if (it.success) {
                val menuDetailViewPager = MenuDetailViewPagerAdapter(context, menu, it.result)
                itemView.vp_menu_detail.apply {
                    adapter = menuDetailViewPager
                    OverScrollDecoratorHelper.setUpOverScroll(this)
                    offscreenPageLimit = 8
                    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                        override fun onPageScrollStateChanged(state: Int) {}

                        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                        override fun onPageSelected(position: Int) {
                            ((this@apply) as WrapPager).onRefresh()
                            itemView.vp_menu_detail.findViewWithTag<View>("image-$position")?.let {
                                itemView.review_title.gone()
                            }

                            itemView.vp_menu_detail.findViewWithTag<View>("review-$position")?.let {
                                itemView.review_title.visible()
                            }
                        }
                    })
                    itemView.menu_viewpager_indicator.setViewPager(this)
                }
            }
        }, { it.printStackTrace() })
        
        Picasso.with(context).load(menu.picture[0]).fit().transform(CropCircleTransformation()).into(itemView.menu_master_picture)
        itemView.txt_menu_name.text = menu.name
        itemView.menu_master_price.text = menuPrice
        itemView.menu_detail_order.text = Util.stringFormat(context, R.string.menu_order, menuPrice)
    }

}
